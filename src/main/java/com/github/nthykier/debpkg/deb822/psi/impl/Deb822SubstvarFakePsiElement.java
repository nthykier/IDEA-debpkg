package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvar;
import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvars;
import com.github.nthykier.debpkg.deb822.psi.Deb822Substvar;

public class Deb822SubstvarFakePsiElement extends Deb822FakePsiElementBase<Deb822Substvar> {

    public Deb822SubstvarFakePsiElement(Deb822Substvar element) {
        super(element);
    }

    @Override
    public String getName() {
        String name = this.element.getText();
        Deb822KnownSubstvar knownSubstvar = Deb822KnownSubstvars.lookupSubstvar(name);
        if (knownSubstvar != null) {
            return knownSubstvar.getName();
        }
        return name + " (runtime defined or unknown)";
    }

}
