package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.github.nthykier.debpkg.deb822.psi.Deb822Substvar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.Nullable;

public class Deb822SubstvarPsiReference extends PsiReferenceBase<Deb822Substvar> {

    public Deb822SubstvarPsiReference(Deb822Substvar element, TextRange textRange) {
        super(element, textRange, true);
    }

    @Override
    public @Nullable PsiElement resolve() {
        // TODO: Implement substvar lookup against other fields in the paragraph or the source section
        return new Deb822SubstvarFakePsiElement(this.myElement);
    }
}
