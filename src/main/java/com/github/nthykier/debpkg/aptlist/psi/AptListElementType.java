package com.github.nthykier.debpkg.aptlist.psi;

import com.github.nthykier.debpkg.aptlist.AptListLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class AptListElementType extends IElementType {
    public AptListElementType(@NotNull @NonNls String debugName) {
        super(debugName, AptListLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "AptListElementType." + super.toString();
    }
}