package com.github.nthykier.debpkg.deb822.field.impl;

import com.github.nthykier.debpkg.deb822.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.Deb822KnownFieldKeyword;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;
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

    public Deb822KnownFieldImpl(@NotNull String canonicalFieldName, @NotNull Deb822KnownFieldValueType fieldValueType,
                                boolean areAllKeywordsKnown, @NotNull Map<String, Deb822KnownFieldKeyword> allKnownKeywords,
                                String docs, boolean supportsSubstvars) {
        this.canonicalFieldName = canonicalFieldName;
        this.fieldValueType = fieldValueType;
        this.areAllKeywordsKnown = areAllKeywordsKnown;
        this.allKnownKeywords = Collections.unmodifiableMap(allKnownKeywords) ;
        this.hasKnownValues = areAllKeywordsKnown || !this.allKnownKeywords.isEmpty();
        this.docs = docs;
        this.supportsSubstvars = supportsSubstvars;
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
    public Deb822KnownFieldValueType getFieldValueType() {
        return this.fieldValueType;
    }
}
