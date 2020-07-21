package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.dch.psi.DchTypes;
import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

public class DchSpellcheckingStrategy extends SpellcheckingStrategy {

    @NotNull
    public Tokenizer<?> getTokenizer(PsiElement element) {
        if (DchTypes.CHANGE_DETAILS.equals(element.getNode().getElementType())) {
            return TEXT_TOKENIZER;
        }
        return EMPTY_TOKENIZER;
    }
}
