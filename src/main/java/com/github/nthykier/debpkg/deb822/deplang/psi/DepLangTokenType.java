package com.github.nthykier.debpkg.deb822.deplang.psi;

import com.github.nthykier.debpkg.deb822.deplang.DependencyLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DepLangTokenType extends IElementType {
    public DepLangTokenType(@NotNull @NonNls String debugName) {
        super(debugName, DependencyLanguage.INSTANCE);
    }
}