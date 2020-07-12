package com.github.nthykier.debpkg.deb822.psi;

public class Deb822TokenType extends IElementType {
    public Deb822TokenType(@NotNull @NonNls String debugName) {
        super(debugName, SimpleLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "Deb822TokenType." + super.toString();
    }
}