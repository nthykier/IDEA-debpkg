package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvar;
import com.github.nthykier.debpkg.deb822.psi.Deb822SubstvarBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Deb822SubstvarFakePsiElement extends Deb822FakePsiElementBase<Deb822SubstvarBase> {

    private final Deb822KnownSubstvar knownSubstvar;

    public Deb822SubstvarFakePsiElement(Deb822SubstvarBase element, @Nullable Deb822KnownSubstvar knownSubstvar) {
        super(element);
        this.knownSubstvar = knownSubstvar;
    }

    @Override
    public String getName() {
        if (knownSubstvar != null) {
            return knownSubstvar.getName();
        }
        return this.element.getText() + " (runtime defined or unknown)";
    }

    @Override
    public @Nullable String getDocumentation() {
        if (knownSubstvar != null) {
            String name = knownSubstvar.getName();
            String docs = knownSubstvar.getDescription();
            String value = renderSubstvarValue(knownSubstvar.getPredefinedValue());
            if (docs != null) {
                return name  + value + "<br><br>" + docs;
            } else {
                return name + value + "<br><br>[Standard substvar; no documentation available]";
            }
        } else {
            return this.element.getText() + "<br><br>[Custom substvar; no documentation available]";
        }
    }

    private static  @NotNull String renderSubstvarValue(@Nullable String value) {
        if (value == null) {
            return " -> <em>(Runtime defined value)</em>";
        }
        return " -> " + value.replace("\t", "\\t").replace("\n", "\\n");
    }
}
