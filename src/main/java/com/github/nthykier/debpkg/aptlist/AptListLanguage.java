package com.github.nthykier.debpkg.aptlist;

import com.intellij.lang.Language;

public class AptListLanguage extends Language {
    public static final AptListLanguage INSTANCE = new AptListLanguage();

    private AptListLanguage() {
        super("DebianAptSourcesList");
    }
}
