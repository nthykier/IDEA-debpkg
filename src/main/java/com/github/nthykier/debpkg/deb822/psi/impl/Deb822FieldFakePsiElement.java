package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldBase;
import org.jetbrains.annotations.Nullable;

public class Deb822FieldFakePsiElement extends Deb822FakePsiElementBase<Deb822FieldBase> {

    private final Deb822KnownField knownField;

    private Deb822FieldFakePsiElement(Deb822FieldBase element, @Nullable Deb822KnownField knownField) {
        super(element);
        this.knownField = knownField;
    }

    @Override
    public String getName() {
        if (knownField != null) {
            return knownField.getCanonicalFieldName();
        }
        return this.element.getFieldName() + " (not recognized)";
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

    public static Deb822FieldFakePsiElement newInstance(Deb822FieldBase element, @Nullable Deb822KnownField knownField) {
        return new Field(element, knownField);
    }

    /* Jump though a hoop to ensure a better hover text for unrecognised fields */
    private static class Field extends Deb822FieldFakePsiElement {
        Field(Deb822FieldBase element, @Nullable Deb822KnownField knownField) {
            super(element, knownField);
        }
    }
}
