package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValueElement;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public final class Deb822PsiImplUtil {
    private static final TokenSet VALUE_TOKENS = TokenSet.create(Deb822Types.VALUE);

    private Deb822PsiImplUtil() {}

    private static ASTNode getKeyNode(final Deb822FieldValueElement element) {
        return element.getNode().findChildByType(Deb822Types.FIELD_NAME);
    }

    public static String getFieldName(final Deb822FieldValueElement element) {
        ASTNode keyNode = getKeyNode(element);
        return keyNode != null ? keyNode.getText() : null;
    }


    public static String getFieldValue(final Deb822FieldValueElement element) {
        ASTNode[] valueNodes = element.getNode().getChildren(VALUE_TOKENS);
        StringBuilder valueBuilder = new StringBuilder();
        for (ASTNode valueNode : valueNodes) {
            valueBuilder.append(valueNode.getText());
        }
        return valueBuilder.toString();
    }


    public static String getName(final Deb822FieldValueElement element) {
        return getFieldName(element);
    }

    public static PsiElement setName(final Deb822FieldValueElement element, String newName) {
       throw new IncorrectOperationException();
    }

    public static PsiElement getNameIdentifier(final Deb822FieldValueElement element) {
        ASTNode keyNode = getKeyNode(element);
        return keyNode != null ? keyNode.getPsi() : null;
    }

    public static ItemPresentation getPresentation(final Deb822FieldValueElement element) {
        return new ItemPresentation() {
            @Override
            public @Nullable String getPresentableText() {
                return null;
            }

            @Override
            public @Nullable String getLocationString() {
                PsiFile containingFile = element.getContainingFile();
                return containingFile == null ? null : containingFile.getName();
            }

            @Override
            public @Nullable Icon getIcon(boolean unused) {
                return null;
            }
        };
    }
}
