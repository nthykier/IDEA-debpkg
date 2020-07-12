package com.github.nthykier.debpkg.deb822;

import com.intellij.lang.Language;

public class Deb822Language extends Language {
    public static final Deb822Language INSTANCE = new Deb822Language();

    private Deb822Language() {
        super("Deb822");
    }
}
