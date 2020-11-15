package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

public class Deb822Language extends Language implements Deb822LanguageSupport {
    public static final Deb822Language INSTANCE = new Deb822Language();

    private Deb822Language() {
        super("Deb822");
    }

    @Override
    public @NotNull Deb822ParagraphClassifier getParagraphClassifier() {
        return Deb822ParagraphClassifier.NULL_CLASSIFIER;
    }

    @Override
    public @NotNull KnownFieldTable getKnownFieldTable() {
        return KnownFieldTable.NULL_TABLE;
    }
}
