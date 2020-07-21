package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldKeyword;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Deb822ValueFakePsiElement extends Deb822FakePsiElementBase<PsiElement> {

    private final Deb822KnownFieldKeyword keyword;

    public Deb822ValueFakePsiElement(PsiElement element, @NotNull Deb822KnownFieldKeyword keyword) {
        super(element);
        this.keyword = keyword;
    }

    @Override
    public String getName() {
        return keyword.getValueName();
    }

    @Override
    public @Nullable String getDocumentation() {
        if (keyword != null) {
            String name = "<b>" + keyword.getValueName() + "</b>";
            String docs = keyword.getValueDescription();
            if (keyword.isExclusive()) {
                name = name + " (can only be used on its own)";
            }
            if (docs != null) {
                return name  + "<br><br>" + docs;
            } else {
                return name + "<br><br>[Standard value; no documentation available]";
            }
        }
        return null;
    }
}
