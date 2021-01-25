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

public class DCtrlMissingRulesRequiresRootInspection extends LocalInspectionTool {

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
            public void visitParagraph(@NotNull Deb822Paragraph deb822Paragraph) {
                ProgressIndicatorProvider.checkCanceled();
                if (deb822Paragraph.classifyParagraph().equals(Deb822DialectDebianControlLanguage.PARAGRAPH_TYPE_SOURCE)) {
                    checkSourceParagraph(holder, deb822Paragraph);
                }
            }
        };
    }

    private void checkSourceParagraph(@NotNull final ProblemsHolder holder, @NotNull Deb822Paragraph paragraph) {
        Deb822FieldValuePair src = paragraph.getFieldValuePair("source");
        Deb822FieldValuePair rrr = paragraph.getFieldValuePair("rules-requires-root");
        assert src != null; /* It cannot be a source paragraph without this field */
        if (rrr == null) {
            String[] placeRelativeTo = new String[] {
                    "Standards-Version",
                    "Testsuite",
            };
            LocalQuickFix[] fixes = new LocalQuickFix[] {
                    fieldInsertionQuickFix("Rules-Requires-Root: no", placeRelativeTo),
                    fieldInsertionQuickFix("Rules-Requires-Root: binary-targets", placeRelativeTo),
            };
            holder.registerProblem(src,
                    Deb822Bundle.message("deb822.files.suggested-field.source-missing-rules-requires-root"),
                    fixes
            );
        }
    }


}
