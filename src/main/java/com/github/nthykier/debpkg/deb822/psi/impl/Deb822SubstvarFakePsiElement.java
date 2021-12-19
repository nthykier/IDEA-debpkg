package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvar;
import com.github.nthykier.debpkg.deb822.psi.Deb822SubstvarBase;
import com.intellij.lang.documentation.DocumentationMarkup;
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
        String docs = "[Custom substvar; no documentation available]";
        String substvarName;
        if (knownSubstvar != null) {
            substvarName = "<b>" + knownSubstvar.getName() + "</b>" + renderSubstvarValue(knownSubstvar.getPredefinedValue());
            docs = knownSubstvar.getDescription();
            if (docs == null) {
                docs = "[Standard substvar; no documentation available]";
            }
        } else {
            substvarName = this.element.getText();
        }
        return DocumentationMarkup.DEFINITION_START + "<b>" + substvarName + "</b>" + DocumentationMarkup.CONTENT_END +
                DocumentationMarkup.CONTENT_START + docs + DocumentationMarkup.CONTENT_END;
    }

    private static  @NotNull String renderSubstvarValue(@Nullable String value) {
        if (value == null) {
            return " -> <em>(Runtime defined value)</em>";
        }
        return " -> " + value.replace("\t", "\\t").replace("\n", "\\n");
    }

    @Override
    public @Nullable String getPresentableText() {
        return "Substitution variable (no target available)";
    }
}
