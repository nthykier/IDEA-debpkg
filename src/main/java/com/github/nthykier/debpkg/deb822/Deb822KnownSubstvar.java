package com.github.nthykier.debpkg.deb822;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NavigableSet;

public interface Deb822KnownSubstvar {

    @NotNull String getName();
/*
    boolean hasKnownValues();

    boolean areAllKeywordsKnown();

    @NotNull NavigableSet<String> getKnownKeywords();
*/

    @Nullable String getPredefinedValue();
    @Nullable String getDescription();
}
