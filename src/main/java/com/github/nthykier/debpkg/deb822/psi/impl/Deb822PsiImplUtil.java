package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.*;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldKeyword;
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

    public static PsiReference getReference(@NotNull Deb822Substvar substvar) {
        String substvarName = substvar.getText();
        Deb822KnownSubstvar knownSubstvar = Deb822KnownSubstvars.lookupSubstvar(substvarName);
        return new Deb822SubstvarPsiReference(substvar, TextRange.from(0, substvar.getTextLength()), knownSubstvar);
    }

    public static @Nullable PsiReference getReference(@NotNull Deb822Value value) {
        Deb822FieldValuePair parent = getAncestorOfType(value, Deb822FieldValuePair.class);
        if (parent != null) {
            Deb822Field field = parent.getField();
            Deb822KnownField knownField = field.getDeb822KnownField();
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

    @Nullable
    public static <T extends PsiElement> T getAncestorOfType(@NotNull PsiElement element,
                                                             @NotNull Class<T> clazz) {
        PsiElement parent = element.getParent();
        while (parent != null && !clazz.isAssignableFrom(parent.getClass())) {
            parent = parent.getParent();
        }
        return clazz.cast(parent);
    }
}
