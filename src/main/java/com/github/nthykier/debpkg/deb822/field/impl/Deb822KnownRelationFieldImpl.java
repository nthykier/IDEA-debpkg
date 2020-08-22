package com.github.nthykier.debpkg.deb822.field.impl;

import com.github.nthykier.debpkg.deb822.field.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Deb822KnownRelationFieldImpl extends Deb822KnownFieldImpl implements Deb822KnownRelationField {

    private final Set<String> supportedVersionOperators;
    private final boolean supportsBuildProfileRestriction;

    public Deb822KnownRelationFieldImpl(@NotNull String canonicalFieldName, @NotNull Deb822KnownFieldValueType fieldValueType,
                                        @NotNull Deb822KnownFieldValueLanguage fieldValueLanguage, boolean areAllKeywordsKnown,
                                        @NotNull Map<String, Deb822KnownFieldKeyword> allKnownKeywords,
                                        String docs, boolean supportsSubstvars, String defaultValue, boolean warnIfDefault,
                                        @NotNull Set<String> supportedParagraphTypes, boolean isFoldedByDefault,
                                        @NotNull Set<String> supportedVersionOperators, boolean supportsBuildProfileRestriction
    ) {
        super(canonicalFieldName, fieldValueType, fieldValueLanguage, areAllKeywordsKnown, allKnownKeywords, docs,
              supportsSubstvars, defaultValue, warnIfDefault, supportedParagraphTypes, isFoldedByDefault);
        this.supportedVersionOperators = supportedVersionOperators;
        this.supportsBuildProfileRestriction = supportsBuildProfileRestriction;
    }

    @Override
    public @NotNull Set<String> supportedVersionOperators() {
        return supportedVersionOperators;
    }

    @Override
    public boolean supportsBuildProfileRestriction() {
        return supportsBuildProfileRestriction;
    }
}
