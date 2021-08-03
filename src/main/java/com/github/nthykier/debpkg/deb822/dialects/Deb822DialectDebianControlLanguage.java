package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.Deb822ParagraphClassifier;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.intellij.lang.Language;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Deb822DialectDebianControlLanguage extends Language implements Deb822LanguageSupport {
    public static final Deb822DialectDebianControlLanguage INSTANCE = new Deb822DialectDebianControlLanguage();

    public final static String PARAGRAPH_TYPE_SOURCE = "Source";
    public final static String PARAGRAPH_TYPE_BINARY_PACKAGE = "Package";

    private static final Deb822ParagraphClassifier DEBIAN_CONTROL_PARAGRAPH_CLASSIFIER = paragraph ->
            paragraph.isFirstParagraph() ? PARAGRAPH_TYPE_SOURCE : PARAGRAPH_TYPE_BINARY_PACKAGE;

    private Deb822DialectDebianControlLanguage() {
        super(Deb822Language.INSTANCE, "DebianControl");
    }

    @Getter(lazy=true)
    private final KnownFieldTable knownFieldTable = Deb822KnownFieldsAndValues.getKnownFieldsFor(this);

    @Override
    public @NotNull Deb822ParagraphClassifier getParagraphClassifier() {
        return DEBIAN_CONTROL_PARAGRAPH_CLASSIFIER;
    }

}
