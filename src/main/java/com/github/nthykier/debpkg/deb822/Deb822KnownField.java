package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NavigableSet;

public interface Deb822KnownField {

    @NotNull String getCanonicalFieldName();

    boolean hasKnownValues();

    boolean areAllKeywordsKnown();

    @NotNull NavigableSet<String> getKnownKeywords();

    @Nullable String getFieldDescription();

    boolean supportsSubstsvars();

    @NotNull
    Deb822KnownFieldValueType getFieldValueType();
}
