package com.github.nthykier.debpkg.deb822.field.impl;

import com.github.nthykier.debpkg.deb822.field.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Deb822KnownFieldImpl implements Deb822KnownField {
    private final String canonicalFieldName;
    private final Deb822KnownFieldValueType fieldValueType;
    private final boolean areAllKeywordsKnown;
    private final boolean hasKnownValues;
    private final Map<String, Deb822KnownFieldKeyword> allKnownKeywords;
    private final String docs;
    private final boolean supportsSubstvars;
    private final String defaultValue;
    private final boolean warnIfDefault;
    private final Set<String> supportedParagraphTypes;
    private final Deb822KnownFieldValueLanguage fieldValueLanguage;
    private final boolean isFoldedByDefault;

    public Deb822KnownFieldImpl(@NotNull String canonicalFieldName, @NotNull Deb822KnownFieldValueType fieldValueType,
                                @NotNull Deb822KnownFieldValueLanguage fieldValueLanguage, boolean areAllKeywordsKnown,
                                @NotNull Map<String, Deb822KnownFieldKeyword> allKnownKeywords,
                                String docs, boolean supportsSubstvars, String defaultValue, boolean warnIfDefault,
                                @NotNull Set<String> supportedParagraphTypes, boolean isFoldedByDefault
    ) {
        this.canonicalFieldName = canonicalFieldName;
        this.fieldValueType = fieldValueType;
        this.fieldValueLanguage = fieldValueLanguage;
        this.areAllKeywordsKnown = areAllKeywordsKnown;
        this.allKnownKeywords = Collections.unmodifiableMap(allKnownKeywords) ;
        this.hasKnownValues = areAllKeywordsKnown || !this.allKnownKeywords.isEmpty();
        this.docs = docs;
        this.supportsSubstvars = supportsSubstvars;
        this.defaultValue = defaultValue;
        this.warnIfDefault = warnIfDefault;
        this.supportedParagraphTypes = supportedParagraphTypes.isEmpty() ? KnownFields.ANY_PARAGRAPH_TYPES : Collections.unmodifiableSet(supportedParagraphTypes);
        this.isFoldedByDefault = isFoldedByDefault;
    }

    @NotNull
    @Override
    public String getCanonicalFieldName() {
        return canonicalFieldName;
    }

    @Override
    public boolean areAllKeywordsKnown() {
        return areAllKeywordsKnown;
    }


    @Override
    public boolean hasKnownValues() {
        return hasKnownValues;
    }

    @NotNull
    @Override
    public Set<String> getKnownKeywords() {
        return allKnownKeywords.keySet();
    }

    @Nullable
    public Deb822KnownFieldKeyword getKeyword(String name) {
        return allKnownKeywords.get(name);
    }

    @Nullable
    @Override
    public String getFieldDescription() {
        return this.docs;
    }

    @Override
    public boolean supportsSubstsvars() {
        return this.supportsSubstvars;
    }

    @NotNull
    @Override
    public Deb822KnownFieldValueType getFieldValueType() {
        return this.fieldValueType;
    }

    @NotNull
    @Override
    public Deb822KnownFieldValueLanguage getFieldValueLanguage() {
        return this.fieldValueLanguage;
    }

    @Override
    public @Nullable String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public boolean warnIfSetToDefault() {
        return this.warnIfDefault;
    }

    public String toString() {
        return "Deb822KnownField." + this.getCanonicalFieldName();
    }

    @Override
    public @NotNull Set<String> getSupportedParagraphTypes() {
        return this.supportedParagraphTypes;
    }

    @Override
    public boolean isFoldedByDefault() {
        return this.isFoldedByDefault;
    }
}
