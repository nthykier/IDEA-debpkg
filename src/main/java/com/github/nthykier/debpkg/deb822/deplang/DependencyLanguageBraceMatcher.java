package com.github.nthykier.debpkg.deb822.deplang;

import com.github.nthykier.debpkg.deb822.deplang.psi.DependencyLanguageTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DependencyLanguageBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] BRACE_PAIRS = new BracePair[]{
            new BracePair(DependencyLanguageTypes.PARANTHESES_OPEN, DependencyLanguageTypes.PARANTHESES_CLOSE, true),
            new BracePair(DependencyLanguageTypes.BRACKETS_OPEN, DependencyLanguageTypes.BRACKETS_CLOSE, true),
            new BracePair(DependencyLanguageTypes.ANGLE_BRACKET_OPEN, DependencyLanguageTypes.ANGLE_BRACKET_CLOSE, true),
    };

    public BracePair @NotNull [] getPairs() {
        return BRACE_PAIRS;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    public int getCodeConstructStart(final PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }
}
