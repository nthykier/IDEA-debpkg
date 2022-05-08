package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.deb822.psi.Deb822Visitor;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import static com.github.nthykier.debpkg.util.AnnotatorUtil.fieldInsertionQuickFix;

public class DCtrlUsesObsoleteFieldInspection extends AbstractDctrlInspection {

    private static final String[] DEPRECATED_FIELDS_NO_REPLACEMENT = {
            "DM-Upload-Allowed"
    };

    @NotNull
    public PsiElementVisitor inspectionVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new Deb822Visitor() {

            @Override
            public void visitParagraph(@NotNull Deb822Paragraph o) {
                ProgressIndicatorProvider.checkCanceled();
                checkParagraph(holder, o);
            }
        };
    }

    private void checkParagraph(@NotNull final ProblemsHolder holder, @NotNull Deb822Paragraph paragraph) {
        for (String fieldName : DEPRECATED_FIELDS_NO_REPLACEMENT) {
            Deb822FieldValuePair field = paragraph.getFieldValuePair(fieldName);
            if (field != null) {
                holder.registerProblem(field,
                        Deb822Bundle.message("deb822.files.inspection.dctrl-obsolete-field.description"),
                        AnnotatorUtil.elementRemovalQuickfixer(
                                Deb822Bundle.message("deb822.files.quickfix.remove-field.name", field.getField().getFieldName()),
                                Deb822Bundle.message("deb822.files.quickfix.remove-field.familyName"),
                                Deb822FieldValuePair.class
                        )
                );
            }
        }
    }

}
