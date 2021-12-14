package com.github.nthykier.debpkg.deb822.field.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.Deb822ParagraphSupport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class KnownFieldTableImpl implements KnownFieldTable {
    private final Map<String, Deb822KnownField> knownFieldMap;
    private final List<String> knownFieldNames;
    private final boolean autoStripXPrefix;
    private final BiFunction<KnownFieldTable, Deb822ParagraphSupport, Deb822KnownField> paragraphNamingField;

    public KnownFieldTableImpl(@NotNull Map<String, Deb822KnownField> knownFieldMap) {
        this(knownFieldMap, false, null);
    }

    public KnownFieldTableImpl(@NotNull Map<String, Deb822KnownField> knownFieldMap, boolean autoStripXPrefix, BiFunction<KnownFieldTable, Deb822ParagraphSupport, Deb822KnownField> paragraphNamingField) {
        this.knownFieldMap = Collections.unmodifiableMap(knownFieldMap);
        // When 1.10 can be assumed; use ".collect(Collectors.toUnmodifiableList())"
        this.knownFieldNames = Collections.unmodifiableList(knownFieldMap.values()
                .stream()
                .map(Deb822KnownField::getCanonicalFieldName)
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList())
        );
        this.autoStripXPrefix = autoStripXPrefix;
        this.paragraphNamingField = paragraphNamingField;
    }

    @Override
    public Deb822KnownField getParagraphNamingField(Deb822ParagraphSupport paragraph) {
        if (paragraphNamingField == null) {
            return null;
        }
        return paragraphNamingField.apply(this, paragraph);
    }

    @Override
    public boolean getAutoStripXPrefix() {
        return this.autoStripXPrefix;
    }

    @Override
    public @Nullable Deb822KnownField getField(@NotNull String fieldName) {
        String fieldNameLC = fieldName.toLowerCase();
        Deb822KnownField knownField = this.knownFieldMap.get(fieldNameLC);
        if (knownField == null && autoStripXPrefix && fieldNameLC.startsWith("x")) {
            String stripped = KnownFieldTable.withXPrefixStripped(fieldNameLC);
            if (stripped == null) {
                return null;
            }
            knownField = this.knownFieldMap.get("x-" + stripped);
            if (knownField == null) {
                knownField = this.knownFieldMap.get(stripped);
            }
        }
        return knownField;
    }

    @NotNull
    public Collection<String> getAllFieldNames() {
        return knownFieldNames;
    }

    @Override
    public @NotNull Collection<Deb822KnownField> getAllFields() {
        return this.knownFieldMap.values();
    }
}
