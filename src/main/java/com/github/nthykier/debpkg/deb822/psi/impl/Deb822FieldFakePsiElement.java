package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import org.jetbrains.annotations.Nullable;

public class Deb822FieldFakePsiElement extends Deb822FakePsiElementBase<Deb822Field> {

    private final Deb822KnownField knownField;

    public Deb822FieldFakePsiElement(Deb822Field element, @Nullable Deb822KnownField knownField) {
        super(element);
        this.knownField = knownField;
    }

    @Override
    public String getName() {
        String name = this.element.getText();
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
            return this.element.getText() + " (no documentation available)";
        }
    }
}
