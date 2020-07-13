package org.intellij.sdk.language;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class Deb822LexerAdapter extends FlexAdapter {
    public Deb822LexerAdapter() {
        super(new Deb822Lexer((Reader)null));
    }
}
