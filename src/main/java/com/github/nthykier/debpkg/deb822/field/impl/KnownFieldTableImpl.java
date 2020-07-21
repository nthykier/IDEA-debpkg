package com.github.nthykier.debpkg.deb822.field.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.intellij.lang.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KnownFieldTableImpl implements KnownFieldTable {
    private final Language language;
    private final Map<String, Deb822KnownField> knownFieldMap;
    private List<String> knownFieldNames;

    public KnownFieldTableImpl(@NotNull Language language, @NotNull Map<String, Deb822KnownField> knownFieldMap) {
        this.language = language;
        this.knownFieldMap = Collections.unmodifiableMap(knownFieldMap);
        // When 1.10 can be assumed; use ".collect(Collectors.toUnmodifiableList())"
        this.knownFieldNames = Collections.unmodifiableList(knownFieldMap.values()
                .stream()
                .map(Deb822KnownField::getCanonicalFieldName)
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList())
        );
    }

    @Override
    public @NotNull Language getLanguage() {
        return this.language;
    }

    @Override
    public @Nullable Deb822KnownField getField(@NotNull String fieldName) {
        return this.knownFieldMap.get(fieldName.toLowerCase());
    }

    @NotNull
    public Collection<String> getAllFieldNames() {
        return knownFieldNames;
    }

    @Override
    public @NotNull Collection<Deb822KnownField> getAllFields() {
        return this.knownFieldMap.values();
    }

    @Contract("null, _ -> param2")
    @Override
    public Deb822KnownField getField(@NotNull String fieldName, Deb822KnownField defaultValue) {
        return this.knownFieldMap.getOrDefault(fieldName, defaultValue);
    }
}
