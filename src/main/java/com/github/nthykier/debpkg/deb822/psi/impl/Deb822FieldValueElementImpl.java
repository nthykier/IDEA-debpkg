package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValueElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class Deb822FieldValueElementImpl extends ASTWrapperPsiElement implements Deb822FieldValueElement {
    public Deb822FieldValueElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}
