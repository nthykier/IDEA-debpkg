package com.github.nthykier.debpkg.dch;

import com.intellij.lexer.FlexAdapter;
import org.intellij.sdk.language.DchLexer;

import java.io.Reader;

public class DchLexerAdapter extends FlexAdapter {
    public DchLexerAdapter() {
        super(new DchLexer(null));
    }
}
