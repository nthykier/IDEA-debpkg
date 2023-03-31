package com.github.nthykier.debpkg.aptlist;

import com.intellij.lexer.FlexAdapter;
import org.intellij.sdk.language.AptListLexer;

public class AptListLexerAdapter extends FlexAdapter {
    public AptListLexerAdapter() {
        super(new AptListLexer(null));
    }
}
