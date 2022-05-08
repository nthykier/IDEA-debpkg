package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822HangingContValue;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Deb822Annotator implements Annotator, DumbAware {

    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof Deb822Paragraph) {
            checkParagraph(holder, (Deb822Paragraph)element);
        } else if (element instanceof Deb822HangingContValue) {
            handleBrokenContinuation(holder, (Deb822HangingContValue)element);
        }
    }

    private void handleBrokenContinuation(@NotNull AnnotationHolder holder, @NotNull Deb822HangingContValue hangingContValue) {
        AnnotatorUtil.createAnnotationWithQuickFixForBrokenContinuationLine(holder, hangingContValue);
    }

    private void checkParagraph(@NotNull AnnotationHolder holder, @NotNull Deb822Paragraph paragraph) {
        /* We use getFieldValuePairList here because we want to see duplicates and getFieldMap cannot help with
         * that
         */
        Map<String, Deb822FieldValuePair> seen = new HashMap<>();
        Deb822LanguageSupport deb822LanguageSupport = Deb822LanguageSupport.fromPsiElement(paragraph);
        KnownFieldTable knownFieldTable = deb822LanguageSupport.getKnownFieldTable();
        String paragraphType = paragraph.classifyParagraph();
        for (Deb822FieldValuePair pair : paragraph.getFieldValuePairList()) {
            String fieldName = pair.getField().getFieldName();
            Deb822FieldValuePair existingValue = seen.get(fieldName);
            Deb822KnownField knownField = knownFieldTable.getField(fieldName);
            seen.putIfAbsent(fieldName, pair);
            if (existingValue != null) {
                holder.newAnnotation(HighlightSeverity.ERROR,
                        Deb822Bundle.message("deb822.files.inspection.issue.duplicate.field_names", fieldName)
                )
                    .range(pair)
                    .create();
            } else if (knownField != null && !knownField.isSupportedInParagraphType(paragraphType)) {
                AnnotatorUtil.createAnnotationWithQuickFix(
                        holder,
                        HighlightSeverity.ERROR,
                        AnnotatorUtil.elementRemovalQuickfixer(
                                Deb822Bundle.message("deb822.files.quickfix.fields.field-does-not-belong-in-paragraph.name"),
                                Deb822FieldValuePair.class
                        ),
                        "field-does-not-belong-in-paragraph",
                        pair,
                        ProblemHighlightType.ERROR,
                        knownField.getCanonicalFieldName(), paragraphType
                );
            }
        }
    }
}
