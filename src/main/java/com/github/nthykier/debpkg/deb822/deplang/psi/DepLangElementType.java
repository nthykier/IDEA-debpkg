package com.github.nthykier.debpkg.deb822.deplang.psi;

import com.github.nthykier.debpkg.deb822.deplang.DependencyLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DepLangElementType extends IElementType {
    public DepLangElementType(@NotNull @NonNls String debugName) {
        super(debugName, DependencyLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "DepLangElementType." + super.toString();
    }
}