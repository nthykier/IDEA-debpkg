package com.github.nthykier.debpkg.dch;

import com.intellij.lang.Language;

public class DchLanguage extends Language {
    public static final DchLanguage INSTANCE = new DchLanguage();

    private DchLanguage() {
        super("DebianChangelog");
    }
}
