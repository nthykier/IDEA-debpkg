package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.intellij.codeInspection.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DebSrcControlInspection extends LocalInspectionTool {

    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        if (!(holder.getFile() instanceof Deb822File)) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        return new Deb822Visitor() {
            public void visitParagraph(@NotNull Deb822Paragraph deb822Paragraph) {
                Map<String, Deb822FieldValuePair> seen = new HashMap<>();
                super.visitParagraph(deb822Paragraph);
                for (Deb822FieldValuePair pair : deb822Paragraph.getFieldValuePairList()) {
                    String fieldName = pair.getField().getText();
                    Deb822FieldValuePair existingValue = seen.get(fieldName);
                    seen.putIfAbsent(fieldName, pair);
                    if (existingValue != null) {
                        holder.registerProblem(pair, Deb822Bundle.message("deb822.files.inspection.issue.duplicate.field_names", fieldName));
                    }
                }
            }
        };
    }
}
