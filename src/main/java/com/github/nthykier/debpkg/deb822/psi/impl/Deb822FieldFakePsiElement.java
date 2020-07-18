package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;

public class Deb822FieldFakePsiElement extends Deb822FakePsiElementBase<Deb822Field> {

    public Deb822FieldFakePsiElement(Deb822Field element) {
        super(element);
    }

    @Override
    public String getName() {
        String name = this.element.getText();
        Deb822KnownField knownField = Deb822KnownFieldsAndValues.lookupDeb822Field(name);
        if (knownField != null) {
            return knownField.getCanonicalFieldName();
        }
        return name + " (not recognized)";
    }

}
