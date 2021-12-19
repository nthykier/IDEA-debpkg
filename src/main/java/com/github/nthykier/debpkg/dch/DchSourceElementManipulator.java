package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.dch.psi.DchSource;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DchSourceElementManipulator extends AbstractElementManipulator<DchSource> {

    @Override
    public @Nullable DchSource handleContentChange(@NotNull DchSource element,
                                                   @NotNull TextRange range,
                                                   String newContent) throws IncorrectOperationException {
        StringBuilder builder = new StringBuilder(element.getText());
        builder.replace(range.getStartOffset(), range.getEndOffset(), newContent);
        DchSource replacement = DchElementFactory.createSource(element.getProject(), builder.toString());
        return (DchSource)element.replace(replacement);
    }
}
