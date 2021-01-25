package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822File;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.deb822.psi.Deb822Visitor;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import static com.github.nthykier.debpkg.util.AnnotatorUtil.fieldInsertionQuickFix;

public class DCtrlUsesDeprecatedImportantFieldWithoutProtectedInspection extends LocalInspectionTool {

    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        /* This only makes sense for debian/control files */
        if (holder.getFile().getLanguage() != Deb822DialectDebianControlLanguage.INSTANCE) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        if (!(holder.getFile() instanceof Deb822File)) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        return new Deb822Visitor() {

            @Override
            public void visitParagraph(@NotNull Deb822Paragraph o) {
                ProgressIndicatorProvider.checkCanceled();
                checkParagraph(holder, o);
            }
        };
    }

    private void checkParagraph(@NotNull final ProblemsHolder holder, @NotNull Deb822Paragraph paragraph) {
        Deb822FieldValuePair importantField = paragraph.getFieldValuePair("XB-Important");
        Deb822FieldValuePair protectedField;
        LocalQuickFix[] fixes;
        if (importantField == null) {
            return;
        }
        protectedField = paragraph.getFieldValuePair("Protected");
        if (protectedField != null) {
            return;
        }
        if (importantField.getValueParts() == null || importantField.getValueParts().getText().equalsIgnoreCase("no")) {
            return;
        }
        fixes = new LocalQuickFix[] {
                fieldInsertionQuickFix("Protected: yes", "XB-Important"),
        };
        holder.registerProblem(importantField,
                Deb822Bundle.message("deb822.files.inspection.dctrl-uses-deprecated-important-field-without-protected"),
                fixes
        );
    }

}
