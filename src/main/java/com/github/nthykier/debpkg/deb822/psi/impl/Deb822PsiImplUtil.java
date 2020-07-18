package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.github.nthykier.debpkg.deb822.psi.Deb822Substvar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;

public class Deb822PsiImplUtil {

    public static PsiReference getReference(Deb822Field field) {
        return new Deb822FieldPsiReference(field, TextRange.from(0, field.getTextLength()));
    }

    public static PsiReference getReference(Deb822Substvar substvar) {
        return new Deb822SubstvarPsiReference(substvar, TextRange.from(0, substvar.getTextLength()));
    }
}
