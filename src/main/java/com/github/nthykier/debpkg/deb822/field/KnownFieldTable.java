package com.github.nthykier.debpkg.deb822.field;

import com.intellij.lang.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface KnownFieldTable {

    @NotNull
    Language getLanguage();

    @Nullable
    Deb822KnownField getField(@NotNull String fieldName);

    @NotNull
    Collection<String> getAllFieldNames();

    @NotNull
    Collection<Deb822KnownField> getAllFields();

    @Contract("null, _ -> param2")
    default Deb822KnownField getField(@NotNull String fieldName, Deb822KnownField defaultValue) {
        Deb822KnownField value = getField(fieldName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
