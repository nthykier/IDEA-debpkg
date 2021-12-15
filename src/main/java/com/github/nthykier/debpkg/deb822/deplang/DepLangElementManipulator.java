package com.github.nthykier.debpkg.deb822.deplang;

import com.github.nthykier.debpkg.deb822.deplang.psi.DepLangElementFactory;
import com.github.nthykier.debpkg.deb822.deplang.psi.DepLangPackageName;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DepLangElementManipulator extends AbstractElementManipulator<DepLangPackageName> {

    @Override
    public @Nullable DepLangPackageName handleContentChange(@NotNull DepLangPackageName element,
                                                            @NotNull TextRange range,
                                                            String newContent) throws IncorrectOperationException {
        StringBuilder builder = new StringBuilder(element.getText());
        builder.replace(range.getStartOffset(), range.getEndOffset(), newContent);
        DepLangPackageName replacement = DepLangElementFactory.createPackageName(element.getProject(), builder.toString());
        return (DepLangPackageName)element.replace(replacement);
    }
}
