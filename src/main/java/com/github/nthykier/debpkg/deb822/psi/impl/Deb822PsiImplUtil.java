package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.*;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Substvar;
import com.github.nthykier.debpkg.deb822.psi.Deb822Value;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Deb822PsiImplUtil {

    public static PsiReference getReference(@NotNull Deb822Field field) {
        String fieldName = field.getText();
        Deb822KnownField knownField = Deb822KnownFieldsAndValues.lookupDeb822Field(fieldName);
        return new Deb822FieldPsiReference(field, TextRange.from(0, field.getTextLength()), knownField);
    }

    public static PsiReference getReference(@NotNull Deb822Substvar substvar) {
        String substvarName = substvar.getText();
        Deb822KnownSubstvar knownSubstvar = Deb822KnownSubstvars.lookupSubstvar(substvarName);
        return new Deb822SubstvarPsiReference(substvar, TextRange.from(0, substvar.getTextLength()), knownSubstvar);
    }

    public static @Nullable PsiReference getReference(@NotNull Deb822Value value) {
        PsiElement parent = value.getParent();
        while (parent != null && !(parent instanceof Deb822FieldValuePair)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            Deb822Field field = ((Deb822FieldValuePair)parent).getField();
            Deb822KnownField knownField = Deb822KnownFieldsAndValues.lookupDeb822Field(field.getText());
            Deb822KnownFieldKeyword keyword;
            if (knownField == null) {
                return null;
            }
            keyword = knownField.getKeyword(value.getText());
            if (keyword != null) {
                return new Deb822ValuePsiReference(value, TextRange.from(0, value.getTextLength()), keyword);
            }
        }
        return null;
    }
}
