package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.Deb822ParagraphClassifier;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.intellij.lang.Language;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Deb822DialectDebianTestsControlLanguage extends Language implements Deb822LanguageSupport {
    public static final Deb822DialectDebianTestsControlLanguage INSTANCE = new Deb822DialectDebianTestsControlLanguage();

    public final static String PARAGRAPH_TYPE_TEST = "Test";

    private static final Deb822ParagraphClassifier DEBIAN_TESTS_CONTROL_PARAGRAPH_CLASSIFIER = paragraph -> PARAGRAPH_TYPE_TEST;

    private Deb822DialectDebianTestsControlLanguage() {
        super(Deb822Language.INSTANCE, "DebianTestsControl");
    }

    @Getter(lazy=true)
    private final KnownFieldTable knownFieldTable = Deb822KnownFieldsAndValues.getKnownFieldsFor(this);

    @Override
    public @NotNull Deb822ParagraphClassifier getParagraphClassifier() {
        return DEBIAN_TESTS_CONTROL_PARAGRAPH_CLASSIFIER;
    }

    @Override
    public boolean supportsEmptyFields() {
        return true;
    }

}
