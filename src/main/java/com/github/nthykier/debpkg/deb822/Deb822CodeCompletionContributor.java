package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.ElementPatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Deb822CodeCompletionContributor extends CompletionContributor {

    public Deb822CodeCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(Deb822Types.FIELD_NAME),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        List<String> knownValues = Deb822KnownFieldsAndValues.getAllKnownFieldNames();
                        List<LookupElement> completions = new ArrayList<>(knownValues.size());
                        for (String name : knownValues) {
                            completions.add(LookupElementBuilder.create(name + ": "));
                        }
                        resultSet.addAllElements(completions);
                    }
                }
        );
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(Deb822Types.VALUE),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        PsiElement fieldValue = parameters.getPosition();
                        String fieldName;
                        Deb822KnownField knownField;
                        while (fieldValue != null && ! (fieldValue instanceof Deb822FieldValuePair)) {
                            fieldValue = fieldValue.getParent();
                        }
                        if (fieldValue == null) {
                            return;
                        }
                        fieldName = ((Deb822FieldValuePair)fieldValue).getField().getText().trim().toLowerCase();
                        knownField = Deb822KnownFieldsAndValues.lookupDeb822Field(fieldName);
                        if (knownField == null || !knownField.hasKnownValues()) {
                            return;
                        }
                        resultSet.addAllElements(knownField.getKnownKeywords().stream().map(LookupElementBuilder::create).collect(Collectors.toList()));
                    }
                }
        );
    }
}
