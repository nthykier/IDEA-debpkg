package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianTestsControlLanguage;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.deb822.psi.Deb822Visitor;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class DTCtrlDuplicateTestDefinitionInspection extends AbstractDctrlInspection {

    public DTCtrlDuplicateTestDefinitionInspection() {
        super(Deb822DialectDebianTestsControlLanguage.INSTANCE);
    }

    @Override
    protected PsiElementVisitor inspectionVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new Deb822Visitor() {

            @Override
            public void visitParagraph(@NotNull Deb822Paragraph o) {
                ProgressIndicatorProvider.checkCanceled();
                checkParagraph(holder, o);
            }
        };
    }

    private void checkParagraph(@NotNull ProblemsHolder holder, @NotNull Deb822Paragraph paragraphs) {
        Deb822FieldValuePair testsFields = paragraphs.getFieldValuePair("Tests");
        Deb822FieldValuePair testCommandFields = paragraphs.getFieldValuePair("Test-Command");

        if (testsFields != null && testCommandFields != null) {
            holder.registerProblem(
                    testCommandFields,
                    Deb822Bundle.message("deb822.files.inspection.dtctrl-tests-and-test-commands-mutually-exclusive.description"),
                    AnnotatorUtil.elementRemovalQuickfixer(
                            Deb822Bundle.message("deb822.files.quickfix.remove-field.name", testCommandFields.getField().getFieldName()),
                            Deb822Bundle.message("deb822.files.quickfix.remove-field.familyName"),
                            Deb822FieldValuePair.class
                    )
            );

            holder.registerProblem(
                    testsFields,
                    Deb822Bundle.message("deb822.files.inspection.dtctrl-tests-and-test-commands-mutually-exclusive.description"),
                    AnnotatorUtil.elementRemovalQuickfixer(
                            Deb822Bundle.message("deb822.files.quickfix.remove-field.name", testsFields.getField().getFieldName()),
                            Deb822Bundle.message("deb822.files.quickfix.remove-field.familyName"),
                            Deb822FieldValuePair.class
                    )
            );
        }
    }
}
