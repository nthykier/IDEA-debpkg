package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.intellij.lang.Language;

public class Deb822DialectDebianControlLanguage extends Language {
    public static final Deb822DialectDebianControlLanguage INSTANCE = new Deb822DialectDebianControlLanguage();

    private Deb822DialectDebianControlLanguage() {
        super(Deb822Language.INSTANCE, "DebianControl");
    }
}
