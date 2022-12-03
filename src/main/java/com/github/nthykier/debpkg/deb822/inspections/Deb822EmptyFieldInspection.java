package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822File;
import com.github.nthykier.debpkg.deb822.psi.Deb822HangingContValue;
import com.github.nthykier.debpkg.deb822.psi.Deb822Visitor;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiWhiteSpace;
import org.jetbrains.annotations.NotNull;

public class Deb822EmptyFieldInspection extends LocalInspectionTool {

    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        if (!(holder.getFile() instanceof Deb822File)) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        boolean supportsEmptyFields = Deb822LanguageSupport.fromDeb822Language(holder.getFile().getLanguage()).supportsEmptyFields();
        return new Deb822Visitor() {
            @Override
            public void visitFieldValuePair(@NotNull Deb822FieldValuePair fieldValuePair) {
                ProgressIndicatorProvider.checkCanceled();
                checkParagraph(holder, fieldValuePair, supportsEmptyFields);
            }
        };
    }

    private void checkParagraph(ProblemsHolder holder, @NotNull Deb822FieldValuePair field, boolean supportsEmptyFields) {
        if (field.getValueParts() != null) {
            return;
        }

        // A missing ValueParts can mean many things - including parse errors
        PsiElement lastChild = field.getLastChild();
        if (lastChild instanceof PsiErrorElement) {
            return;
        }
        while (lastChild instanceof PsiWhiteSpace || lastChild instanceof Deb822HangingContValue) {
            lastChild = lastChild.getPrevSibling();
        }
        if (lastChild.getTextLength() == 1 && lastChild.getText().equals(":")) {
            holder.registerProblem(field,
                    supportsEmptyFields
                            ? Deb822Bundle.message("deb822.files.inspection.eb822-empty-field-inspection-weak.description")
                            : Deb822Bundle.message("deb822.files.inspection.eb822-empty-field-inspection-strong.description"),
                    supportsEmptyFields ? ProblemHighlightType.WARNING : ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                    AnnotatorUtil.elementRemovalQuickfixer(
                            Deb822Bundle.message("deb822.files.quickfix.remove-field.name", field.getField().getFieldName()),
                            Deb822Bundle.message("deb822.files.quickfix.remove-field.familyName"),
                            Deb822FieldValuePair.class
                    )
            );
        }
    }
}
