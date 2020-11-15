package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.Deb822ParagraphClassifier;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;


public class Deb822DialectDebianCopyrightLanguage extends Language implements Deb822LanguageSupport {
    public static final Deb822DialectDebianCopyrightLanguage INSTANCE = new Deb822DialectDebianCopyrightLanguage();


    public final static String PARAGRAPH_TYPE_HEADER = "Header";
    public final static String PARAGRAPH_TYPE_FILES = "Files";
    public final static String PARAGRAPH_TYPE_LICENSE = "License";


    private static final Deb822ParagraphClassifier DEBIAN_COPYRIGHT_PARAGRAPH_CLASSIFIER = paragraph -> {
        if (paragraph.isFirstParagraph()) {
            return PARAGRAPH_TYPE_HEADER;
        }
        return paragraph.getFieldValuePair("Files") != null ? PARAGRAPH_TYPE_FILES : PARAGRAPH_TYPE_LICENSE;
    };

    private Deb822DialectDebianCopyrightLanguage() {
        super(Deb822Language.INSTANCE, "DebianCopyright");
    }


    @Override
    public @NotNull Deb822ParagraphClassifier getParagraphClassifier() {
        return DEBIAN_COPYRIGHT_PARAGRAPH_CLASSIFIER;
    }

    @Override
    public @NotNull KnownFieldTable getKnownFieldTable() {
        return Deb822KnownFieldsAndValues.getKnownFieldsFor(this);
    }
}
