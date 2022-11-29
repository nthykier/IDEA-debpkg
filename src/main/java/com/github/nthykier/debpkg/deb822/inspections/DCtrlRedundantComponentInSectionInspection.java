package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822File;
import com.github.nthykier.debpkg.deb822.psi.Deb822ValueParts;
import com.github.nthykier.debpkg.deb822.psi.Deb822Visitor;
import com.intellij.codeInspection.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DCtrlRedundantComponentInSectionInspection extends AbstractDctrlInspection {

    public DCtrlRedundantComponentInSectionInspection() {
        super(Deb822DialectDebianControlLanguage.INSTANCE);
    }

    @NotNull
    public PsiElementVisitor inspectionVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new Deb822Visitor() {
            @Override
            public void visitFieldValuePair(@NotNull Deb822FieldValuePair o) {
                ProgressIndicatorProvider.checkCanceled();
                checkField(holder, o, isOnTheFly);
            }
        };
    }

    private void checkField(@NotNull final ProblemsHolder holder, @NotNull Deb822FieldValuePair fieldValuePair, boolean isOnTheFly) {
        Deb822KnownField knownField = fieldValuePair.getField().getDeb822KnownField();
        Deb822ValueParts valueParts = fieldValuePair.getValueParts();
        ASTNode wordNode;
        String value;
        TextRange range;
        if (knownField == null || !knownField.getCanonicalFieldName().equals("Section")) {
            return;
        }
        if (valueParts == null || !valueParts.textContains('/')) {
            return;
        }
        wordNode = getValueNode(knownField, valueParts);
        if (wordNode == null) {
            return;
        }
        value = wordNode.getText();
        if (!value.startsWith("main/")) {
            return;
        }
        range = TextRange.from(0, 5);
        holder.registerProblem(new ProblemDescriptorBase(
                valueParts,
                valueParts,
                Deb822Bundle.message("deb822.files.inspection.dctrl-section-field-has-redundant-prefix"),
                LocalQuickFix.EMPTY_ARRAY,
                ProblemHighlightType.WEAK_WARNING,
                false,
                range,
                true,
                isOnTheFly
        ));
    }

    private static ASTNode getValueNode(@NotNull Deb822KnownField knownField, @NotNull Deb822ValueParts valueParts) {
        List<List<ASTNode>> valueSplit = knownField.getFieldValueType().splitValue(valueParts);
        List<ASTNode> values;
        if (valueSplit.size() != 1) {
            // If it is correctly filled out, then there will be exactly one "word" in it.
            // If it is not, then something else will already give a warning about that.
            return null;
        }
        values = valueSplit.get(0);
        if (values.size() != 1) {
            // If it is correctly filled out, then there will be exactly one "word" in it.
            // If it is not, then something else will already give a warning about that.
            return null;
        }
        return values.get(0);
    }

}
