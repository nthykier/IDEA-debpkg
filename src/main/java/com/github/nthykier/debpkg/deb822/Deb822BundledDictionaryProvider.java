package com.github.nthykier.debpkg.deb822;

import com.intellij.spellchecker.BundledDictionaryProvider;

public class Deb822BundledDictionaryProvider implements BundledDictionaryProvider {
    @Override
    public String[] getBundledDictionaries() {
        return new String[]{
                /* These are imported from `debputy` */
                "debian-wordlist.dic",
                "logins-and-people.dic"
        };
    }
}
