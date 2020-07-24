package com.github.nthykier.debpkg.deb822.deplang;

import com.intellij.lang.InjectableLanguage;
import com.intellij.lang.Language;

public class DependencyLanguage extends Language implements InjectableLanguage {
    public static final DependencyLanguage INSTANCE = new DependencyLanguage();

    private DependencyLanguage() {
        super("DependencyLanguage");
    }
}
