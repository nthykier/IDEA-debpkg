package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.FakePsiElement;

public class Deb822FieldFakePsiElement extends FakePsiElement {

    private final Deb822Field element;

    public Deb822FieldFakePsiElement(Deb822Field element) {
        this.element = element;
    }

    public Deb822Field getField() {
        return this.element;
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

    @Override
    public PsiElement getParent() {
        return this.element;
    }

    /* Maybe implement along with a custom navigate function */
    @Override
    public boolean canNavigate() {
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }

    @Override
    public String toString() {
        return "Foo";
    }
}
