package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822ValueParts;
import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.inspections.PlainTextSplitter;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.TokenConsumer;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import com.intellij.spellchecker.tokenizer.TokenizerBase;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Deb822SpellcheckingStrategy extends SpellcheckingStrategy {

    private final Tokenizer<Deb822ValueParts> deb822ValueTokenizer = TokenizerBase.create(PlainTextSplitter.getInstance());
    private final Tokenizer<Deb822ValueParts> nullTokenizer = new Tokenizer<Deb822ValueParts>() {
        @Override
        public void tokenize(@NotNull Deb822ValueParts element, TokenConsumer consumer) {

        }
    };
    private static final Set<String> SKIP_SPELL_CHECK = new HashSet<>(Arrays.asList(
            "package", "architecture", "multi-arch", "depends", "recommends", "suggests", "breaks", "replaces",
            "pre-depends", "build-depends", "build-depends-indep", "build-depends-arch", "provides", "section"));

    public Tokenizer getTokenizer(PsiElement element) {
        if (element instanceof Deb822ValueParts) {
            PsiElement parent = element.getParent();
            while (parent != null && !(parent instanceof Deb822FieldValuePair)) {
                parent = parent.getParent();
            }
            if (parent != null) {
                String name = ((Deb822FieldValuePair)parent).getField().getText();
                if (name != null && SKIP_SPELL_CHECK.contains(name.toLowerCase())) {
                    return nullTokenizer;
                }
            }
            return deb822ValueTokenizer;
        }
        return super.getTokenizer(element);
    }
}
