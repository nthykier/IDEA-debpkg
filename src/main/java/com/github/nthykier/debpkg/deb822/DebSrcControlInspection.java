package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlAnnotator;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
                /* We use getFieldValuePairList here because we want to see duplicates and getFieldMap cannot help with
                 *  that
                 */
                for (Deb822FieldValuePair pair : deb822Paragraph.getFieldValuePairList()) {
                    String fieldName = pair.getField().getFieldName();
                    Deb822FieldValuePair existingValue = seen.get(fieldName);
                    seen.putIfAbsent(fieldName, pair);
                    if (existingValue != null) {
                        holder.registerProblem(pair, Deb822Bundle.message("deb822.files.inspection.issue.duplicate.field_names", fieldName));
                    }
                }
                if (Deb822DialectDebianControlAnnotator.guessParagraphType(deb822Paragraph).equals(Deb822DialectDebianControlAnnotator.PARAGRAPH_TYPE_SOURCE)) {
                    checkSourceParagraph(holder, deb822Paragraph);
                }
            }
        };
    }

    private static Function<Project, Deb822FieldValuePair> fieldGenerator(final String content) {
        return (Project project) -> Deb822ElementFactory.createFieldValuePairFromText(project, content);
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
                    AnnotatorUtil.fieldInsertionQuickFix(fieldGenerator("Rules-Requires-Root: no"), placeRelativeTo).apply("source-add-rules-requires-root-to-no"),
                    AnnotatorUtil.fieldInsertionQuickFix(fieldGenerator("Rules-Requires-Root: binary-targets"), placeRelativeTo).apply("source-add-rules-requires-root-to-binary-targets"),
            };
            holder.registerProblem(src,
                    Deb822Bundle.message("deb822.files.suggested-field.source-missing-rules-requires-root"),
                    ProblemHighlightType.WEAK_WARNING,
                    fixes
            );
        }
    }


}
