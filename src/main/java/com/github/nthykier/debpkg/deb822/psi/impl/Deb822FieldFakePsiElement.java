package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldBase;
import com.intellij.lang.documentation.DocumentationMarkup;
import org.jetbrains.annotations.Nullable;

public class Deb822FieldFakePsiElement extends Deb822FakePsiElementBase<Deb822FieldBase> {

    private final Deb822KnownField knownField;

    public Deb822FieldFakePsiElement(Deb822FieldBase element, @Nullable Deb822KnownField knownField) {
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
        String docs = "No documentation available";
        String fieldName = element.getFieldName();
        if (knownField != null) {
            docs = knownField.getFieldDescription();
            if (docs == null) {
                docs = "<i>standard field, but no documentation is available</i>";
            }
        }
        return DocumentationMarkup.DEFINITION_START + "<b>" + fieldName + "</b>" + DocumentationMarkup.CONTENT_END +
                DocumentationMarkup.CONTENT_START + docs + DocumentationMarkup.CONTENT_END;
    }

    @Override
    public @Nullable String getPresentableText() {
        return "Field (no target available)";
    }
}
