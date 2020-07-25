package com.github.nthykier.debpkg.deb822.deplang;

import com.intellij.lexer.FlexAdapter;
import org.intellij.sdk.language.DependencyLanguageLexer;

public class DependencyLanguageLexerAdapter extends FlexAdapter {
    public DependencyLanguageLexerAdapter() {
        super(new DependencyLanguageLexer(null));
    }
}
