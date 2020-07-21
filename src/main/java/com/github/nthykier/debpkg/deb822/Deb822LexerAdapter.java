package com.github.nthykier.debpkg.deb822;

import com.intellij.lexer.FlexAdapter;
import org.intellij.sdk.language.Deb822Lexer;

import java.io.Reader;

public class Deb822LexerAdapter extends FlexAdapter {
    public Deb822LexerAdapter() {
        super(new Deb822Lexer(null));
    }
}
