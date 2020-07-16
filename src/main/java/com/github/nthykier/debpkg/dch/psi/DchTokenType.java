package com.github.nthykier.debpkg.dch.psi;

import com.github.nthykier.debpkg.dch.DchLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DchTokenType extends IElementType {
    public DchTokenType(@NotNull @NonNls String debugName) {
        super(debugName, DchLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "DchTokenType." + super.toString();
    }
}