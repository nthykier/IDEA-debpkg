package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.Deb822ParagraphClassifier;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.intellij.lang.Language;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Deb822DialectAptSourcesLanguage extends Language implements Deb822LanguageSupport {
    public static final Deb822DialectAptSourcesLanguage INSTANCE = new Deb822DialectAptSourcesLanguage();

    private Deb822DialectAptSourcesLanguage() {
        super(Deb822Language.INSTANCE, "DebianAptSources");
    }

    @Getter(lazy=true)
    private final KnownFieldTable knownFieldTable = Deb822KnownFieldsAndValues.getKnownFieldsFor(this);

    @Override
    public boolean supportsEmptyFields() {
        return true;
    }

    @Override
    public @NotNull Deb822ParagraphClassifier getParagraphClassifier() {
        return Deb822ParagraphClassifier.NULL_CLASSIFIER;
    }
}
