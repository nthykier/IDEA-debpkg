package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class Deb822CanonicalFieldVariantInspection extends LocalInspectionTool {

    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        if (!(holder.getFile() instanceof Deb822File)) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        return new Deb822Visitor() {
            public void visitParagraph(@NotNull Deb822Paragraph deb822Paragraph) {
                ProgressIndicatorProvider.checkCanceled();
                checkParagraph(holder, deb822Paragraph);
            }
        };
    }

    private void checkParagraph(ProblemsHolder holder, @NotNull Deb822Paragraph deb822Paragraph) {
        for (Deb822FieldValuePair valuePair : deb822Paragraph.getFieldValuePairList()) {
            Deb822Field field = valuePair.getField();
            Deb822KnownField knownField = field.getDeb822KnownField();
            if (knownField == null || knownField.getCanonicalFieldName().equals(field.getFieldName())) {
                continue;
            }
            holder.registerProblem(
                    field,
                    Deb822Bundle.message("deb822.files.inspection.canonical-field-variant.description"),
                    ProblemHighlightType.WEAK_WARNING,
                    AnnotatorUtil.replaceFieldNameFix(knownField.getCanonicalFieldName())
            );
        }
    }
}
