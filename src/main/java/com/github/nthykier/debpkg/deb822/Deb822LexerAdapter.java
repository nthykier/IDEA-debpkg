package com.github.nthykier.debpkg.deb822;

import com.intellij.lexer.FlexAdapter;
import org.intellij.sdk.language.Deb822Lexer;

public class Deb822LexerAdapter extends FlexAdapter {
    public Deb822LexerAdapter(Deb822LanguageSupport languageSupport) {
        super(new Deb822Lexer(null, languageSupport));
    }
}
