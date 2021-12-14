package com.github.nthykier.debpkg.deb822.field;

import com.github.nthykier.debpkg.deb822.field.impl.KnownFieldTableImpl;
import com.github.nthykier.debpkg.deb822.psi.Deb822ParagraphSupport;
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

    boolean getAutoStripXPrefix();

    Deb822KnownField getParagraphNamingField(Deb822ParagraphSupport paragraph);

    static @Nullable String withXPrefixStripped(String fieldName) {
        int firstDash = fieldName.indexOf('-');
        char firstChar;
        if (firstDash == -1 || firstDash > 5) {
            return null;
        }
        firstChar = fieldName.charAt(0);
        if (firstChar != 'X' && firstChar != 'x') {
            return null;
        }

        for (int i = 1 ; i < firstDash ; i++) {
            char c = Character.toUpperCase(fieldName.charAt(i));
            if (c != 'B' && c != 'C' && c != 'S') {
                return null;
            }
        }
        return fieldName.substring(firstDash + 1);
    }
}
