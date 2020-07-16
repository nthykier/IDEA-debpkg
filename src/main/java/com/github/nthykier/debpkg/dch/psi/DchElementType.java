package com.github.nthykier.debpkg.dch.psi;

import com.github.nthykier.debpkg.dch.DchLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DchElementType extends IElementType {
    public DchElementType(@NotNull @NonNls String debugName) {
        super(debugName, DchLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "DchElementType." + super.toString();
    }
}