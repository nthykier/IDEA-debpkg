package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvar;
import com.github.nthykier.debpkg.deb822.psi.Deb822SubstvarBase;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.Nullable;

public class Deb822SubstvarPsiReference extends PsiReferenceBase<Deb822SubstvarBase> {

    private final Deb822KnownSubstvar substvar;

    public Deb822SubstvarPsiReference(Deb822SubstvarBase element, TextRange textRange,
                                      @Nullable Deb822KnownSubstvar substvar) {
        super(element, textRange, true);
        this.substvar = substvar;
    }

    @Override
    public @Nullable PsiElement resolve() {
        // TODO: Implement substvar lookup against other fields in the paragraph or the source section
        return new Deb822SubstvarFakePsiElement(this.myElement, substvar);
    }
}
