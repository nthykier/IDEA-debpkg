package com.github.nthykier.debpkg.deb822.field.impl;

import com.github.nthykier.debpkg.deb822.field.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Getter
public class Deb822KnownFieldImpl implements Deb822KnownField {
    private final String canonicalFieldName;
    private final Deb822KnownFieldValueType fieldValueType;
    private final boolean areAllKeywordsKnown;
    private final boolean hasKnownValues;
    private final Map<String, Deb822KnownFieldKeyword> allKnownKeywords;
    private final String fieldDescription;
    private final boolean supportsSubstvars;
    private final String defaultValue;
    private final boolean warnIfDefault;
    private final Set<String> supportedParagraphTypes;
    private final Deb822KnownFieldValueLanguage fieldValueLanguage;
    private final boolean isFoldedByDefault;
    private final boolean isSpellcheckForValueEnabled;

    public Deb822KnownFieldImpl(@NotNull String canonicalFieldName, @NotNull Deb822KnownFieldValueType fieldValueType,
                                @NotNull Deb822KnownFieldValueLanguage fieldValueLanguage, boolean areAllKeywordsKnown,
                                @NotNull Map<String, Deb822KnownFieldKeyword> allKnownKeywords,
                                String fieldDescription, boolean supportsSubstvars, String defaultValue, boolean warnIfDefault,
                                @NotNull Set<String> supportedParagraphTypes, boolean isFoldedByDefault,
                                boolean isSpellcheckForValueEnabled
    ) {
        this.canonicalFieldName = canonicalFieldName;
        this.fieldValueType = fieldValueType;
        this.fieldValueLanguage = fieldValueLanguage;
        this.areAllKeywordsKnown = areAllKeywordsKnown;
        this.allKnownKeywords = Collections.unmodifiableMap(allKnownKeywords) ;
        this.hasKnownValues = areAllKeywordsKnown || !this.allKnownKeywords.isEmpty();
        this.fieldDescription = fieldDescription;
        this.supportsSubstvars = supportsSubstvars;
        this.defaultValue = defaultValue;
        this.warnIfDefault = warnIfDefault;
        this.supportedParagraphTypes = supportedParagraphTypes.isEmpty() ? KnownFields.ANY_PARAGRAPH_TYPES : Collections.unmodifiableSet(supportedParagraphTypes);
        this.isFoldedByDefault = isFoldedByDefault;
        this.isSpellcheckForValueEnabled = isSpellcheckForValueEnabled;
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

    @Override
    public boolean supportsSubstsvars() {
        return this.supportsSubstvars;
    }

    @Override
    public boolean warnIfSetToDefault() {
        return this.warnIfDefault;
    }

    public String toString() {
        return "Deb822KnownField." + this.getCanonicalFieldName();
    }
}
