package com.github.nthykier.debpkg.deb822.field.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldKeyword;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Deb822KnownFieldKeywordImpl implements Deb822KnownFieldKeyword {
    private final String valueName;
    private final String docs;
    private final boolean isExclusive;

    public Deb822KnownFieldKeywordImpl(@NotNull String valueName,
                                       String docs,
                                       boolean isExclusive) {
        this.valueName = valueName;
        this.docs = docs;
        this.isExclusive = isExclusive;
    }

    @NotNull
    @Override
    public String getValueName() {
        return this.valueName;
    }

    @Override
    public boolean isExclusive() {
        return this.isExclusive;
    }

    @Nullable
    @Override
    public String getValueDescription() {
        return this.docs;
    }
}
