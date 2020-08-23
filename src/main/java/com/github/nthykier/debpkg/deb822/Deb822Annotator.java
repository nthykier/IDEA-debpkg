package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldKeyword;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.github.nthykier.debpkg.util.Deb822TypeSafeLocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Deb822Annotator implements Annotator {

    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof Deb822Paragraph) {
            checkParagraph(holder, (Deb822Paragraph)element);
        }
    }

    private void checkParagraph(@NotNull AnnotationHolder holder, @NotNull Deb822Paragraph paragraph) {
        /* We use getFieldValuePairList here because we want to see duplicates and getFieldMap cannot help with
         *  that
         */
        Map<String, Deb822FieldValuePair> seen = new HashMap<>();
        for (Deb822FieldValuePair pair : paragraph.getFieldValuePairList()) {
            String fieldName = pair.getField().getFieldName();
            Deb822FieldValuePair existingValue = seen.get(fieldName);
            seen.putIfAbsent(fieldName, pair);
            if (existingValue != null) {
                holder.newAnnotation(HighlightSeverity.ERROR,
                        Deb822Bundle.message("deb822.files.inspection.issue.duplicate.field_names", fieldName)
                )
                    .range(pair)
                    .create();
            }
        }
    }
}
