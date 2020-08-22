package com.github.nthykier.debpkg.deb822.deplang;

import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvars;
import com.github.nthykier.debpkg.deb822.deplang.psi.DepLangVersion;
import com.github.nthykier.debpkg.deb822.deplang.psi.DepLangVersionOperator;
import com.github.nthykier.debpkg.deb822.deplang.psi.DependencyLanguageTypes;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.DumbAware;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DepLangCodeCompletionContributor extends CompletionContributor implements DumbAware {

    private static final String[] KNOWN_VERSION_OPERATORS = new String[] {"<<", "<=", "=", ">=", ">>"};

    public DepLangCodeCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement(DependencyLanguageTypes.VERSION)).afterLeaf("("),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        for (String s : KNOWN_VERSION_OPERATORS) {
                            resultSet.addElement(LookupElementBuilder.create(s + " "));
                        }
                    }
                }
        );

        extend(CompletionType.BASIC, PlatformPatterns.psiElement().withAncestor(3, PlatformPatterns.psiElement(DependencyLanguageTypes.PACKAGE_NAME)),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        List<String> knownNames = Deb822KnownSubstvars.getAllKnownSubstvarNames();
                        for (String name : knownNames) {
                            if (name.startsWith("${shlibs:") || name.startsWith("${misc:") ||name.startsWith("${perl:")) {
                                resultSet.addElement(LookupElementBuilder.create(name));
                            }
                        }
                    }
                }
        );


        extend(CompletionType.BASIC, shouldCompleteVersion(),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        List<String> knownNames = Deb822KnownSubstvars.getAllKnownSubstvarNames();
                        for (String name : knownNames) {
                            if (name.contains("Version")) {
                                resultSet.addElement(LookupElementBuilder.create(name));
                            }
                        }
                    }
                }
        );

    }

    private static @NotNull ElementPattern<PsiElement> shouldCompleteVersion() {
        return PlatformPatterns.psiElement().with(new PatternCondition<PsiElement>("shouldCompleteVersion") {
            @Override
            public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                PsiElement parent = element.getParent();
                if (parent instanceof DepLangVersion) {
                    PsiElement sibling = parent.getPrevSibling();
                    while (sibling instanceof PsiWhiteSpace) {
                        sibling = sibling.getPrevSibling();
                    }
                    return sibling instanceof DepLangVersionOperator;
                }
                return false;
            }
        });
    }

}
