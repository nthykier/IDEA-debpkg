package com.github.nthykier.debpkg.dch;

import com.intellij.lexer.FlexAdapter;
import org.intellij.sdk.language.DchLexer;
import org.intellij.sdk.language.Deb822Lexer;

import java.io.Reader;

public class DchLexerAdapter extends FlexAdapter {
    public DchLexerAdapter() {
        super(new DchLexer((Reader)null));
    }
}
