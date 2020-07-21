package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldBase;
import org.jetbrains.annotations.Nullable;

public class Deb822FieldFakePsiElement extends Deb822FakePsiElementBase<Deb822FieldBase> {

    private final Deb822KnownField knownField;

    public Deb822FieldFakePsiElement(Deb822FieldBase element, @Nullable Deb822KnownField knownField) {
        super(element);
        this.knownField = knownField;
    }

    @Override
    public String getName() {
        String name = this.element.getFieldName();
        if (knownField != null) {
            return knownField.getCanonicalFieldName();
        }
        return name + " (not recognized)";
    }

    @Override
    public @Nullable String getDocumentation() {
        if (knownField != null) {
            String docs = knownField.getFieldDescription();
            if (docs == null) {
                docs = knownField.getCanonicalFieldName() + " (standard field; no documentation available)";
            }
            return docs;
        } else {
            return this.element.getFieldName() + " (no documentation available)";
        }
    }
}
