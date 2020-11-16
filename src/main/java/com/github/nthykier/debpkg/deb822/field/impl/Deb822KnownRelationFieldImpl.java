package com.github.nthykier.debpkg.deb822.field.impl;

import com.github.nthykier.debpkg.deb822.field.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

@Getter
public class Deb822KnownRelationFieldImpl extends Deb822KnownFieldImpl implements Deb822KnownRelationField {

    private final Set<String> supportedVersionOperators;
    private final boolean buildProfileRestrictionSupported;

    public Deb822KnownRelationFieldImpl(@NotNull String canonicalFieldName, @NotNull Deb822KnownFieldValueType fieldValueType,
                                        @NotNull Deb822KnownFieldValueLanguage fieldValueLanguage, boolean areAllKeywordsKnown,
                                        @NotNull Map<String, Deb822KnownFieldKeyword> allKnownKeywords,
                                        String docs, boolean supportsSubstvars, String defaultValue, boolean warnIfDefault,
                                        @NotNull Set<String> supportedParagraphTypes, boolean isFoldedByDefault,
                                        @NotNull Set<String> supportedVersionOperators, boolean buildProfileRestrictionSupported
    ) {
        super(canonicalFieldName, fieldValueType, fieldValueLanguage, areAllKeywordsKnown, allKnownKeywords, docs,
              supportsSubstvars, defaultValue, warnIfDefault, supportedParagraphTypes, isFoldedByDefault, false);
        this.supportedVersionOperators = supportedVersionOperators;
        this.buildProfileRestrictionSupported = buildProfileRestrictionSupported;
    }
}
