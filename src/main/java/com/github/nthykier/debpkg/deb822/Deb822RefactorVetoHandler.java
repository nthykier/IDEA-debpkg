package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.psi.impl.Deb822FakePsiElementBase;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;

public class Deb822RefactorVetoHandler implements Condition<PsiElement> {
    @Override
    public boolean value(PsiElement psiElement) {
        return psiElement instanceof Deb822FakePsiElementBase;
    }
}
