package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFields;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldBase;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues.getKnownFieldsFor;

public class AbstractDeb822Field extends ASTWrapperPsiElement implements Deb822FieldBase {

    /* We use NULL_FIELD as a dummy value for "not-set/recompute" because "null" is a valid return value */
    private Deb822KnownField knownField = KnownFields.NULL_FIELD;
    private String fieldName;
    public AbstractDeb822Field(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        this.knownField = KnownFields.NULL_FIELD;
        this.fieldName = null;
    }

    @Override
    public String getText() {
        /* Guide lookup and cache the field name; we know there is exactly one leaf beneath this node containing the
         * text we want.  The default class structure does not and have to copy the text around, which is expensive.
         */
        if (fieldName == null) {
            ASTNode childNode = this.getNode().getFirstChildNode();
            assert childNode.getElementType().equals(Deb822Types.FIELD_NAME);
            fieldName = childNode.getText();
        }
        return fieldName;
    }

    @NotNull
    public String getFieldName() {
        return this.getText();
    }

    @Nullable
    public Deb822KnownField getDeb822KnownField() {
        if (knownField == KnownFields.NULL_FIELD) {
            knownField = getKnownFieldsFor(this.getContainingFile().getLanguage()).getField(this.getFieldName());
        }
        return knownField;
    }


    public PsiReference getReference() {
        Deb822KnownField knownField = this.getDeb822KnownField();
        return new Deb822FieldPsiReference(this, TextRange.from(0, this.getTextLength()), knownField);
    }

}
