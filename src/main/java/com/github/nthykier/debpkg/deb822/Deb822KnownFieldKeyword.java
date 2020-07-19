package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NavigableSet;

public interface Deb822KnownFieldKeyword {

    /**
     * @return The textual value of this keyword (e.g. "all" for the keyword in the Architecture field)
     */
    @NotNull String getValueName();

    /**
     *
     * @return true if the keyword is exclusive and can only appear on its own.
     */
    boolean isExclusive();


    @Nullable String getValueDescription();
}
