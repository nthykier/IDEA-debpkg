package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Deb822CodeCompletionContributor extends CompletionContributor {

    public Deb822CodeCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(Deb822Types.FIELD_NAME),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        PsiElement fieldValue = parameters.getPosition();
                        KnownFieldTable knownFieldTable = Deb822KnownFieldsAndValues.getKnownFieldsFor(fieldValue.getContainingFile().getLanguage());
                        Collection<String> knownValues = knownFieldTable.getAllFieldNames();
                        List<LookupElement> completions = new ArrayList<>(knownValues.size());
                        for (String name : knownValues) {
                            completions.add(LookupElementBuilder.create(name + ": "));
                        }
                        resultSet.addAllElements(completions);
                    }
                }
        );
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(Deb822Types.VALUE_TOKEN),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        PsiElement fieldValue = parameters.getPosition();
                        Deb822Field field;
                        Deb822KnownField knownField;
                        if (handleSubstvarCompletion(parameters, resultSet)) {
                            /* User is going for a substitution variable */
                            return;
                        }
                        while (fieldValue != null && ! (fieldValue instanceof Deb822FieldValuePair)) {
                            fieldValue = fieldValue.getParent();
                        }
                        if (fieldValue == null) {
                            return;
                        }
                        field = ((Deb822FieldValuePair)fieldValue).getField();
                        knownField = field.getDeb822KnownField();
                        if (knownField == null || !knownField.hasKnownValues()) {
                            return;
                        }
                        resultSet.addAllElements(knownField.getKnownKeywords().stream().map(LookupElementBuilder::create).collect(Collectors.toList()));
                    }
                }
        );
    }

    protected boolean handleSubstvarCompletion(CompletionParameters parameters, @NotNull CompletionResultSet resultSet) {
        PsiElement element = parameters.getPosition().getParent();
        PsiElement prevElement = element.getPrevSibling();
        if (prevElement != null && prevElement.getText().equals("$")) {
            String value = element.getText();
            if (value.equals("") || value.startsWith("{") || value.startsWith("\n")) {
                List<String> knownNames = Deb822KnownSubstvars.getAllKnownSubstvarNames();
                /* TODO: Probably has issues with "bla blah ${foo} blah ${bar}" */
                boolean removeTrailingBrace = value.contains("}");
                for (String name : knownNames) {
                    /*
                     * Trim off leading ${ and possibly trailing } to avoid leaving duplicates.  These have been
                     * observed when completing:
                     *
                     *   * "${<HERE>" -> "${<CHOSEN_VAR>}" (this lead to a "${${"-prefix).
                     *   * "${shlibs:D<HERE>}trailing garbage" -> "${shlibs:Depends}trailing garbage" (this lead to a
                     *     an extra trailing "}" before "trailing garbage").
                     *
                     * (etc.)
                     */
                    String partialName = removeTrailingBrace ? name.substring(2, name.length() - 1) : name.substring(2);
                    resultSet.addElement(LookupElementBuilder.create(partialName).withPresentableText(name));
                }
                return true;
            }
        }
        return false;
    }

}
