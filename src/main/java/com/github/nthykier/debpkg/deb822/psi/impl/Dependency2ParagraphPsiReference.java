package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.DebpkgIcons;
import com.github.nthykier.debpkg.deb822.deplang.psi.DepLangPackageName;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueLanguage;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.util.ASTNodeStringConverter;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.*;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage.PARAGRAPH_TYPE_BINARY_PACKAGE;

public class Dependency2ParagraphPsiReference extends PsiReferenceBase.Poly<DepLangPackageName> implements PsiPolyVariantReference {

    private static final Pattern SPLIT_VERSION = Pattern.compile("\\s*[(].*");

    public Dependency2ParagraphPsiReference(@NotNull DepLangPackageName element) {
        super(element, true);
    }

    private <E> void loadParagraphs(Predicate<Deb822Paragraph> paragraphFilter, Consumer<Deb822Paragraph> onMatch) {
        PsiElement deb822Container = myElement.getContainingFile().getContext();
        if (deb822Container == null) {
            return;
        }
        Deb822AllParagraphs allParagraphs = Deb822PsiImplUtil.getAncestorOfType(deb822Container, Deb822AllParagraphs.class);
        if (allParagraphs == null) {
            return;
        }
        for (Deb822Paragraph paragraph : allParagraphs.getParagraphList()) {
            if (!PARAGRAPH_TYPE_BINARY_PACKAGE.equals(paragraph.classifyParagraph())) {
                continue;
            }
            if (paragraphFilter == null || paragraphFilter.test(paragraph)) {
                onMatch.accept(paragraph);
            }
        }
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        List<ResolveResult> result = new SmartList<>();
        String packageName = myElement.getText();
        loadParagraphs(p -> packageName.equals(p.getName()), p -> result.add(new PsiElementResolveResult(p)));
        return result.toArray(ResolveResult.EMPTY_ARRAY);
    }

    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    private static boolean isArchAll(Deb822Paragraph paragraph) {
        Deb822FieldValuePair pair = paragraph.getFieldValuePair("Architecture");
        if (pair != null)  {
            Deb822ValueParts valueParts = pair.getValueParts();
            return valueParts != null && "all".equals(valueParts.getText());
        }
        return false;
    }

    @Override
    public Object @NotNull [] getVariants() {
        List<Object> result = new SmartList<>();
        PsiElement deb822Container = myElement.getContainingFile().getContext();

        Deb822Paragraph owningParagraph = deb822Container != null
                ? Deb822PsiImplUtil.getAncestorOfType(deb822Container, Deb822Paragraph.class)
                : null;
        Deb822FieldValuePair containingPair = deb822Container != null
                ? Deb822PsiImplUtil.getAncestorOfType(deb822Container, Deb822FieldValuePair.class)
                : null;
        boolean isOwningArchAll = owningParagraph != null && isArchAll(owningParagraph);
        Deb822KnownFieldValueLanguage depLang = Deb822KnownFieldValueLanguage.REGULAR_FIELD_VALUE;
        if (containingPair != null) {
            Deb822KnownField knownField = containingPair.getField().getDeb822KnownField();
            if (knownField != null) {
                depLang = knownField.getFieldValueLanguage();
            }
        }
        final Deb822KnownFieldValueLanguage dependencyLanguage = depLang;

        loadParagraphs(
                // We do not want to suggest a dependency on the package itself.  Strictly speaking, it is useful for
                // Conflicts (+ Replaces) in the "Exclusively provide X" case, but it is rare enough that it does not
                // matter.
                p -> p != owningParagraph,
                p -> {
                    boolean isProviderArchAll = isArchAll(p);
                    String dependency = p.getName();
                    String relation;
                    if (dependency == null) {
                        return;
                    }
                    relation = determineNonVirtualRelation(dependency, dependencyLanguage, isOwningArchAll, isProviderArchAll);
                    result.add(LookupElementBuilder
                            .create(p, relation).withIcon(DebpkgIcons.DEFAULT_FILE_ICON)
                            .withTypeText("Binary Package"));
                    Deb822FieldValuePair provides = p.getFieldValuePair("Provides");
                    if (provides == null) {
                        return;
                    }
                    Deb822ValueParts valueParts = provides.getValueParts();
                    if (valueParts == null) {
                        return;
                    }
                    StringBuilder builder = new StringBuilder();
                    ASTNodeStringConverter convert = new ASTNodeStringConverter(builder);
                    convert.readTextFromNode(valueParts.getNode());
                    String providingPackage = p.getName();
                    for (String provideClause : builder.toString().split(",")) {
                        provideClause = provideClause.trim();
                        String depRelation;
                        if (!dependencyLanguage.isPositiveDependency()) {
                            depRelation = SPLIT_VERSION.matcher(provideClause).replaceFirst("");
                        } else {
                            depRelation = provideClause;
                        }
                        // Ignore substvars
                        if (depRelation.contains("$")) {
                            continue;
                        }
                        result.add(LookupElementBuilder
                                .create(depRelation).withIcon(DebpkgIcons.DEFAULT_FILE_ICON)
                                .withTypeText("Provided by " + providingPackage));
                    }
                }
        );
        return result.toArray();
    }

    private static String determineNonVirtualRelation(String packageName, Deb822KnownFieldValueLanguage dependencyLanguage, boolean isOwningArchAll, boolean isProviderArchAll) {
        String relation;
        switch (dependencyLanguage) {

            // Pick the best (or least bad) version dependency accounting for binNMUs.
            // Prefer strictly equals (it is kosher for "same-source" dependencies and are less likely to break)

            case DEPENDENCY_LANGUAGE_BINARY_DEPENDENCY:
                // Positive dependencies
                if (isOwningArchAll ^ isProviderArchAll) {
                    if (isProviderArchAll) {
                        relation = " (= ${source:Version})";
                    } else {
                        relation = " (>= ${source:Version})";
                    }
                } else {
                    relation = isProviderArchAll ? " (= ${source:Version})" : " (= ${binary:Version})";
                }
                break;
            // substvars does not work in source fields in the first place, so they have use something without.
            case DEPENDENCY_LANGUAGE_SOURCE_DEPENDENCY:
            case DEPENDENCY_LANGUAGE_SOURCE_NEGATIVE_DEPENDENCY:
            // Negative could have a version, but if we do, it should be << and Conflicts should be special-cases to
            // unversioned by default.
            case DEPENDENCY_LANGUAGE_BINARY_NEGATIVE_DEPENDENCY:
            case DEPENDENCY_LANGUAGE_BINARY_PROVIDES:
            case REGULAR_FIELD_VALUE:  // <-- Should not happen, but it is the fallback value
                relation = "";
                break;
            default:
                assert false : "Missing case";
                relation = "";
        }

        return packageName + relation;
    }
}
