package com.github.nthykier.debpkg.deb822;

import com.intellij.spellchecker.BundledDictionaryProvider;

public class Deb822BundledDictionaryProvider implements BundledDictionaryProvider {
    @Override
    public String[] getBundledDictionaries() {
        return new String[]{
                "debian-packaging-terms.dic"
        };
    }
}
