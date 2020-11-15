package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

public interface Deb822LanguageSupport {

    @NotNull
    Deb822ParagraphClassifier getParagraphClassifier();

    @NotNull
    KnownFieldTable getKnownFieldTable();

    static @NotNull KnownFieldTable getKnownFieldTableForLanguage(@NotNull Language language) {
        if (language instanceof Deb822LanguageSupport) {
            return ((Deb822LanguageSupport) language).getKnownFieldTable();
        }
        if (language.isKindOf(Deb822Language.INSTANCE)) {
            return KnownFieldTable.NULL_TABLE;
        }
        throw new IllegalArgumentException("Language must be a variant of Deb822Language");
    }
}
