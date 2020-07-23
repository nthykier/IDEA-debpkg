package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.*;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldKeyword;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

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

    @NotNull
    public static String getTextFromCompositeWrappingAToken(@NotNull PsiElement element,
                                                            @NotNull TokenSet tokenTypes) {
        ASTNode childNode = element.getNode().getFirstChildNode();
        String text;
        assert tokenTypes.contains(childNode.getElementType());
        text = childNode.getText();
        assert childNode.getTreeNext() == null;
        return text;
    }


    @Nullable
    public static <T extends PsiElement> T getAncestorOfType(@NotNull PsiElement element,
                                                             @NotNull Class<T> clazz) {
        return iteratePsiElements(element, PsiElement::getParent, clazz);
    }

    private static <T extends PsiElement> T iteratePsiElements(@NotNull PsiElement element,
                                                               @NotNull Function<PsiElement, PsiElement> nextElement,
                                                               @NotNull Class<T> clazz) {
        PsiElement parent = element.getParent();
        while (parent != null && !clazz.isAssignableFrom(parent.getClass())) {
            parent = nextElement.apply(parent);
        }
        return clazz.cast(parent);
    }
}
