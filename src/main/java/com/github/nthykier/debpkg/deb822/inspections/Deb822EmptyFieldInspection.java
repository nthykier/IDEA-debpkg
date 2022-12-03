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
        if (field.getValueParts() == null) {
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
