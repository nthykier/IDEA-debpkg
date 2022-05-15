package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.github.nthykier.debpkg.util.StringUtil;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class Deb822MisspelledFieldsInspection extends LocalInspectionTool {

    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        if (!(holder.getFile() instanceof Deb822File)) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        KnownFieldTable knownFieldTable = Deb822LanguageSupport.fromDeb822Language(holder.getFile().getLanguage()).getKnownFieldTable();
        List<String> knownFields = knownFieldTable.getAllFieldNames().stream().map(String::toLowerCase).collect(Collectors.toList());
        return new Deb822Visitor() {
            public void visitParagraph(@NotNull Deb822Paragraph deb822Paragraph) {
                ProgressIndicatorProvider.checkCanceled();
                checkParagraph(holder, deb822Paragraph, knownFieldTable, knownFields);
            }
        };
    }

    private void checkParagraph(ProblemsHolder holder, @NotNull Deb822Paragraph deb822Paragraph, KnownFieldTable knownFieldTable, List<String> knownFieldNames) {
        for (Deb822FieldValuePair valuePair : deb822Paragraph.getFieldValuePairList()) {
            Deb822Field field = valuePair.getField();
            if (field.getDeb822KnownField() != null) {
                // Already known, no need to check for typos
                continue;
            }
            String fieldName = field.getFieldName().toLowerCase();
            if (knownFieldTable.getAutoStripXPrefix()) {
                // Strip the "X-" prefix to avoid it hiding a match (e.g. the typo in "XC-Pakcage-Type" should be detected as Package-Type)
                String stripped = KnownFieldTable.withXPrefixStripped(fieldName);
                if (stripped != null) {
                    fieldName = stripped;
                }
            }
            List<String> matches = StringUtil.possibleMisspellingOf(fieldName, knownFieldNames, false);
            if (!matches.isEmpty()) {
                holder.registerProblem(
                        field,
                        Deb822Bundle.message("deb822.files.inspection.dctrl-misspelled-fields.description"),
                        ProblemHighlightType.WARNING,
                        matches.stream().map(n -> {
                            Deb822KnownField knownField = knownFieldTable.getField(n);
                            assert knownField != null;
                            return AnnotatorUtil.replaceFieldNameFix(knownField.getCanonicalFieldName());
                        }).toArray(LocalQuickFix[]::new)

                );
            }
        }
    }
}
