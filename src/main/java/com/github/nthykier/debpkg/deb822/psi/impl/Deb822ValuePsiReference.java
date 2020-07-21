package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldKeyword;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Deb822ValuePsiReference extends PsiReferenceBase<PsiElement> {

    private final Deb822KnownFieldKeyword keyword;

    public Deb822ValuePsiReference(PsiElement element, TextRange textRange, @NotNull Deb822KnownFieldKeyword keyword) {
        super(element, textRange, true);
        this.keyword = keyword;
    }

    @Override
    public @Nullable PsiElement resolve() {
        return new Deb822ValueFakePsiElement(this.myElement, keyword);
    }
}
