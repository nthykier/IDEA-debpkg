package com.github.nthykier.debpkg.deb822;

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
}
