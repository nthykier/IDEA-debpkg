package com.github.nthykier.debpkg.util;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.psi.Deb822ElementFactory;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.MODULE)
class Deb822InsertFieldQuickFix implements LocalQuickFix {

    private final String fieldContent;
    private final List<String> beforeFields;

    @Override
    public @IntentionName @NotNull String getName() {
        return Deb822Bundle.message("deb822.quickfix.insertField.name", fieldContent);
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        return Deb822Bundle.message("deb822.quickfix.insertField.familyName");
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiElement newField = Deb822ElementFactory.createFieldValuePairFromText(project, fieldContent);
        Deb822Paragraph paragraph = Deb822PsiImplUtil.getAncestorOfType(descriptor.getPsiElement(), Deb822Paragraph.class);
        // A Deb822FieldValuePair does not contain a trailing newline; force it in to avoid breaking the next field
        // The "Foo: bar" part is only there to avoid a parser error from missing a field / paragraph.  Without it
        // we get a Psi Error element rather than the newline whitespace element that we want.
        PsiElement whitespace = Deb822ElementFactory.createFile(project, "\nFoo: bar\n").getFirstChild();
        Deb822FieldValuePair insertRelativeTo = null;

        if (paragraph == null) {
            throw new IncorrectOperationException("Insertion failed; could not determine which paragraph should have the new field");
        }

        for (String fieldName : beforeFields) {
            insertRelativeTo = paragraph.getFieldValuePair(fieldName);
            if (insertRelativeTo != null) {
                break;
            }
        }

        if (insertRelativeTo != null) {
            insertBefore(paragraph, insertRelativeTo, newField, whitespace);
        } else {
            List<Deb822FieldValuePair> fieldPairs = paragraph.getFieldValuePairList();
            Deb822FieldValuePair lastPair = fieldPairs.get(fieldPairs.size() - 1);
            PsiElement lastChild = paragraph.getLastChild();
            /* If the last element is not a PARAGRAPH_FINISH (happens at EOF) then this logic inserts the tokens
             * wrong.  Prefer insert it as the second last field instead of breaking the file.
             */
            boolean canInsertAtEnd = lastChild.getNode().getElementType() == Deb822Types.PARAGRAPH_FINISH;
            /* Prefer Description as the last field as it is conventionally in the end of d/control paragraphs */
            if (!canInsertAtEnd || lastPair.getField().getFieldName().equalsIgnoreCase("description")) {
                insertBefore(paragraph, lastPair, newField, whitespace);
            } else {
                insertBefore(paragraph, lastChild, newField, whitespace);
            }
        }
    }

    private static void insertBefore(PsiElement paragraph, PsiElement anchorElement, PsiElement newField, PsiElement newline) {
        paragraph.addBefore(newField, anchorElement);
        paragraph.addBefore(newline, anchorElement);
    }
}
