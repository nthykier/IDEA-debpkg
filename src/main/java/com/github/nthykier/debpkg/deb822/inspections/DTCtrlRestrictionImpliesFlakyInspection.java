package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianTestsControlLanguage;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class DTCtrlRestrictionImpliesFlakyInspection extends AbstractDctrlInspection {

    public DTCtrlRestrictionImpliesFlakyInspection() {
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
        Set<String> restrictions = paragraphs.readFieldAsAutopkgtestSet("Restrictions");
        if (restrictions.isEmpty() || restrictions.contains("flaky")) {
            return;
        }
        if (restrictions.contains("needs-internet")) {
            Deb822FieldValuePair restrictionsFVPair = paragraphs.getFieldValuePair("Restrictions");
            assert restrictionsFVPair != null;
            holder.registerProblem(
                    restrictionsFVPair.getField(),
                    Deb822Bundle.message("deb822.files.inspection.dtctrl-restriction-needs-flaky-tests.description")
            );
        }
    }
}
