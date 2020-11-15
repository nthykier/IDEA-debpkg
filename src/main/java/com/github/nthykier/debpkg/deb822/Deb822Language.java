package com.github.nthykier.debpkg.deb822;

import com.intellij.lang.Language;

public class Deb822Language extends Language implements Deb822LanguageSupport {
    public static final Deb822Language INSTANCE = new Deb822Language();

    private Deb822Language() {
        super("Deb822");
    }

    @Override
    public Deb822ParagraphClassifier getParagraphClassifier() {
        return Deb822ParagraphClassifier.NULL_CLASSIFIER;
    }
}
