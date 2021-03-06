package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.field.KnownFields;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldBase;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractDeb822Field extends ASTWrapperPsiElement implements Deb822FieldBase {

    private static final TokenSet FIELD_NAME_TOKEN_SET = TokenSet.create(Deb822Types.FIELD_NAME);

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
            fieldName = Deb822PsiImplUtil.getTextFromCompositeWrappingAToken(this, FIELD_NAME_TOKEN_SET);
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
            KnownFieldTable fieldTable = Deb822LanguageSupport.fromPsiElement(this).getKnownFieldTable();
            knownField = fieldTable.getField(this.getFieldName());
        }
        return knownField;
    }


    public PsiReference getReference() {
        Deb822KnownField knownField = this.getDeb822KnownField();
        return new Deb822FieldPsiReference(this, TextRange.from(0, this.getTextLength()), knownField);
    }

}
