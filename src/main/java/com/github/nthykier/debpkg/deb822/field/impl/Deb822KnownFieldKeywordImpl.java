package com.github.nthykier.debpkg.deb822.field.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldKeyword;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Getter
public class Deb822KnownFieldKeywordImpl implements Deb822KnownFieldKeyword {
    private final String valueName;
    private final String valueDescription;
    private final boolean isExclusive;
}
