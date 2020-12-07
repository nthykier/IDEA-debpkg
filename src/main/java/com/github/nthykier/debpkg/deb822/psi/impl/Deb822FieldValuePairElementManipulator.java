package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.psi.Deb822ElementFactory;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.util.ASTNodeStringConverter;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Deb822FieldValuePairElementManipulator extends AbstractElementManipulator<Deb822FieldValuePair> {

    @Override
    public @Nullable Deb822FieldValuePair handleContentChange(@NotNull Deb822FieldValuePair element,
                                                              @NotNull TextRange range,
                                                              String newContent) throws IncorrectOperationException {
        StringBuilder builder = new StringBuilder();
        ASTNodeStringConverter convert = new ASTNodeStringConverter(builder);
        Deb822FieldValuePair replacement;
        if (newContent.contains("\n")) {
            throw new IncorrectOperationException("Newlines in replacement not supported");
        }
        convert.readTextFromNode(element.getNode());
        if (range.getStartOffset() < 3) {
            throw new IncorrectOperationException("Cannot change field/before the separator");
        }
        if (builder.substring(range.getStartOffset(), range.getEndOffset()).contains("\n")) {
            throw new IncorrectOperationException("Newlines in original text not supported");
        }
        builder.replace(range.getStartOffset(), range.getEndOffset(), newContent);
        replacement = Deb822ElementFactory.createFieldValuePairFromText(element.getProject(), builder.toString());
        return (Deb822FieldValuePair)element.replace(replacement);
    }
}
