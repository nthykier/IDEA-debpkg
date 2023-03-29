package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.deb822.psi.Deb822Visitor;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.Language;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import static com.github.nthykier.debpkg.util.AnnotatorUtil.fieldInsertionQuickFix;

public class DCtrlMissingRulesRequiresRootInspection extends AbstractDctrlInspection {

    public DCtrlMissingRulesRequiresRootInspection() {
        super(Deb822DialectDebianControlLanguage.INSTANCE);
    }

    @NotNull
    public PsiElementVisitor inspectionVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
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
        if (src == null) {
            /* Ignore if the first paragraph does not have a source field. As wrong as that is,
             * it is for another check to handle that.  We should just gracefully back out here.
             */
            return;
        }
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
