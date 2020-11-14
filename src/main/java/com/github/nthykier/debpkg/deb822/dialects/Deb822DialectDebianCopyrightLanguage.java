package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.intellij.lang.Language;

public class Deb822DialectDebianCopyrightLanguage extends Language {
    public static final Deb822DialectDebianCopyrightLanguage INSTANCE = new Deb822DialectDebianCopyrightLanguage();

    private Deb822DialectDebianCopyrightLanguage() {
        super(Deb822Language.INSTANCE, "DebianCopyright");
    }
}
