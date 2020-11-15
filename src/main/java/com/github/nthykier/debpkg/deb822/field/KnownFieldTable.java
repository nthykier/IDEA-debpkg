package com.github.nthykier.debpkg.deb822.field;

import com.github.nthykier.debpkg.deb822.field.impl.KnownFieldTableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public interface KnownFieldTable {

    KnownFieldTable NULL_TABLE = new KnownFieldTableImpl(Collections.emptyMap());

    @Nullable
    Deb822KnownField getField(@NotNull String fieldName);

    @NotNull
    Collection<String> getAllFieldNames();

    @NotNull
    Collection<Deb822KnownField> getAllFields();

    default Deb822KnownField getField(@NotNull String fieldName, Deb822KnownField defaultValue) {
        Deb822KnownField value = getField(fieldName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
