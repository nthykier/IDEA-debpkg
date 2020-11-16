package com.github.nthykier.debpkg.deb822.deplang;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvar;
import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvars;
import com.github.nthykier.debpkg.deb822.deplang.psi.*;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownRelationField;
import com.github.nthykier.debpkg.deb822.psi.Deb822SubstvarBase;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.github.nthykier.debpkg.util.Deb822TypeSafeLocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DependencyLanguageAnnotator implements Annotator {

    private static final Map<String, String> INVALID_VERSION_OPERATORS;
    private static final Pattern PACKAGE_NAME_REGEX = Pattern.compile("[a-z0-9][a-z0-9.+-]+");
    private static final Pattern VERSION_NUMBER_REGEX = Pattern.compile("(?:[0-9]+:)?[0-9][a-z0-9.+~]*([-][a-z0-9.+~-]+)?");

    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof DepLangVersionOperator) {
            checkVersionOperator(holder, (DepLangVersionOperator)element);
        } else if (element instanceof DepLangVersion) {
            checkVersion(holder, (DepLangVersion)element);
        } else if (element instanceof DepLangPackageName) {
            checkPackageName(holder, (DepLangPackageName)element);
        } else if (element instanceof Deb822SubstvarBase) {
            checkSubstvar(holder, (Deb822SubstvarBase)element);
        } else if (element instanceof DepLangDependency) {
            checkDependeny(holder, (DepLangDependency)element);
        }
    }

    private static @Nullable DepLangLanguageDefinition getLanguageInfo(@NotNull PsiElement e) {
        while ( e != null && !(e instanceof DepLangDependencyInfo)) {
            e = e.getParent();
        }
        if (e != null) {
            return ((DepLangDependencyInfo)e).getLanguageDefinition();
        }
        return null;
    }

    private static @Nullable Deb822KnownRelationField lookupContainingDeb822KnownField(@NotNull PsiElement e) {
        DepLangLanguageDefinition languageDefinition = getLanguageInfo(e);
        Deb822KnownField knownField;
        if (languageDefinition == null) {
            return null;
        }
        knownField = Deb822DialectDebianControlLanguage.INSTANCE.getKnownFieldTable().getField(languageDefinition.getText());
        if (knownField == null || knownField.getFieldValueLanguage().getLanguage() != DependencyLanguage.INSTANCE) {
            return null;
        }
        return (Deb822KnownRelationField)knownField;
    }


    private void checkDependeny(@NotNull AnnotationHolder holder, @NotNull DepLangDependency element) {
        if (element.getBuildProfileRestrictionPart() != null) {
            Deb822KnownRelationField knownRelationField = lookupContainingDeb822KnownField(element);
            if (knownRelationField != null && !knownRelationField.isBuildProfileRestrictionSupported()) {
                holder.newAnnotation(HighlightSeverity.ERROR,
                        Deb822Bundle.message("deb822.files.annotator.fields.dependency-field-does-not-support-build-profile", knownRelationField.getCanonicalFieldName())
                )
                        .range(element)
                        .create();
            }
        }
    }


    private void checkSubstvar(@NotNull AnnotationHolder holder, @NotNull Deb822SubstvarBase element) {
        String name = element.getText();
        if (!name.endsWith("}") || !name.startsWith("${")) {
            holder.newSilentAnnotation(HighlightSeverity.ERROR)
                    .range(element)
                    .create();
        }
    }

    private void checkPackageName(@NotNull AnnotationHolder holder, @NotNull DepLangPackageName element) {
        Deb822SubstvarBase substvar = element.getSubstvar();
        String packageName;
        int colonIndex;
        if (substvar != null) {
            // Not checked for now; the substvars allowed depends on whether there are restrictions or not
            // e.g. ${misc:Depends} is ok only when it is on its own.
            /*
            String substvarName = substvar.getText();
            Deb822KnownSubstvar knownSubstvar = Deb822KnownSubstvars.lookupSubstvar(substvarName);
            if (knownSubstvar != null && !substvarName.contains("Package")) {
                holder.createWarningAnnotation(substvar,
                        Deb822Bundle.message("deb822.probably-incorrect-substvar-used-as-package-name", substvarName));
            }
            */
            return;
        }
        packageName = element.getText();
        colonIndex = packageName.indexOf(':');
        if (colonIndex > -1 && colonIndex != packageName.length() - 1) {
            // Ignore ":any" and other architecture qualifier
            packageName = packageName.substring(0, colonIndex);
        }
        checkStringAgainstPattern(PACKAGE_NAME_REGEX, element, packageName, holder, "deb822.invalid-package-name");
    }

    private void checkStringAgainstPattern(@NotNull Pattern pattern,
                                           @NotNull PsiElement element,
                                           @NotNull String tokenText,
                                           @NotNull AnnotationHolder holder,
                                           @NotNull @PropertyKey(resourceBundle=Deb822Bundle.BUNDLE_NAME) String annotationTextKey) {
        if (!pattern.matcher(tokenText).matches()) {
            Matcher matcher = pattern.matcher(tokenText);
            TextRange brokenRange = textRangeOfBrokenTailBit(matcher, tokenText.length());
            AnnotationBuilder annoBuilder = holder.newAnnotation(HighlightSeverity.ERROR, Deb822Bundle.message(annotationTextKey, tokenText));

            if (brokenRange != null) {
                int startOffset = element.getTextOffset();
                annoBuilder = annoBuilder.range(brokenRange.shiftRight(startOffset));
            } else {
                annoBuilder = annoBuilder.range(element);
            }
            annoBuilder.create();
        }
    }

    private void checkVersion(@NotNull AnnotationHolder holder, @NotNull DepLangVersion versionToken) {
        Deb822SubstvarBase substvar = versionToken.getSubstvar();
        String versionNumber;
        if (substvar != null) {
            String substvarName = substvar.getText();
            Deb822KnownSubstvar knownSubstvar = Deb822KnownSubstvars.lookupSubstvar(substvarName);
            if (knownSubstvar != null && !substvarName.contains("Version")) {
                holder.newAnnotation(HighlightSeverity.WARNING, Deb822Bundle.message("deb822.probably-incorrect-substvar-used-as-version", substvarName))
                        .range(substvar)
                        .create();
            }
            return;
        }
        versionNumber = versionToken.getText();
        checkStringAgainstPattern(VERSION_NUMBER_REGEX, versionToken, versionNumber, holder, "deb822.invalid-version-number");
    }

    @Nullable
    private static TextRange textRangeOfBrokenTailBit(@NotNull Matcher matcher, int tokenLength) {
        if (matcher.find() && matcher.start() == 0) {
            return new TextRange(
                    matcher.end(),
                    tokenLength
            );
        }
        return null;
    }

    private void checkVersionOperator(@NotNull AnnotationHolder holder, @NotNull DepLangVersionOperator operatorToken) {
        String operator = operatorToken.getText();
        String replacement = INVALID_VERSION_OPERATORS.get(operator);
        Deb822KnownRelationField knownField = lookupContainingDeb822KnownField(operatorToken);

        if (replacement != null) {
            if (replacement.equals("")) {
                // Invalid without replacement
                holder.newAnnotation(HighlightSeverity.ERROR,
                        Deb822Bundle.message("deb822.files.annotator.fields.unknown-version-operator-in-dependency", operator))
                    .range(operatorToken)
                    .create();
            } else {
                Function<String, Deb822TypeSafeLocalQuickFix<DepLangVersionOperator>> quickfixer =
                        AnnotatorUtil.replacementQuickFixer(
                                (Project p) -> DepLangElementFactory.createVersionPart(p, "debhelper (" + replacement + " 1.0)").getVersionOperator()
                        );

                AnnotatorUtil.createAnnotationWithQuickFix(
                        holder,
                        HighlightSeverity.ERROR,
                        quickfixer,
                        "incorrect-version-operator-in-dependency-with-known-replacement",
                        operatorToken,
                        ProblemHighlightType.ERROR,
                        operator, replacement
                );
                /* We know what the user intended, so use the replacement operator from here on */
                operator = replacement;
            }

        }
        if (knownField != null) {
            if (!knownField.getSupportedVersionOperators().contains(operator)) {
                String supportedOperators = knownField.getSupportedVersionOperators().stream().map(it -> "\"" + it + "\"").collect(Collectors.joining(", "));
                holder.newAnnotation(HighlightSeverity.ERROR,
                        Deb822Bundle.message("deb822.files.annotator.fields.unsupported-version-operator-in-field",
                                operator, knownField.getCanonicalFieldName(), supportedOperators)
                )
                        .range(operatorToken)
                        .create();
            }
        }
    }

    static {
        Map<String, String> invalidVersionOperators = new HashMap<>();
        invalidVersionOperators.put("<", "<=");
        invalidVersionOperators.put(">", ">=");
        invalidVersionOperators.put("==", "=");
        invalidVersionOperators.put("!=", "");
        /* SQL variant of "!=" */
        invalidVersionOperators.put("<>", "");
        INVALID_VERSION_OPERATORS = Collections.unmodifiableMap(invalidVersionOperators);
    }
}
