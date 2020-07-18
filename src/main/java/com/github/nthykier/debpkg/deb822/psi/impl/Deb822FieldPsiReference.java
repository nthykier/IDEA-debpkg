package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.Nullable;

public class Deb822FieldPsiReference extends PsiReferenceBase<Deb822Field> {

    private final Deb822FieldFakePsiElement target;

    public Deb822FieldPsiReference(Deb822Field element, TextRange textRange) {
        super(element, textRange, true);
        this.target = new Deb822FieldFakePsiElement(element);
    }

    @Override
    public @Nullable PsiElement resolve() {
        return this.target;
    }
}
