package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822ValueParts;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Deb822SpellcheckingStrategy extends SpellcheckingStrategy {

    private static final Set<String> SKIP_SPELL_CHECK = new HashSet<>(Arrays.asList(
            "package", "architecture", "depends", "recommends", "suggests", "breaks", "replaces",
            "pre-depends", "build-depends", "build-depends-indep", "build-depends-arch", "provides", "section",
            "testsuite", "maintainer", "uploaders", "source"
            ));

    @NotNull
    public Tokenizer<?> getTokenizer(PsiElement element) {
        if (element instanceof Deb822ValueParts) {
            Deb822FieldValuePair parent = Deb822PsiImplUtil.getAncestorOfType(element, Deb822FieldValuePair.class);
             if (parent != null) {
                Deb822Field field = parent.getField();
                Deb822KnownField knownField = field.getDeb822KnownField();
                if (knownField != null) {
                    String name;
                    /*
                     * In such a case, it seems awkward to have a spell-check highlight the value when the user cannot
                     * change the actual value (as it is mandated else where and "correcting" the spelling will cause
                     * a hard error).
                     */
                    if (knownField.hasKnownValues() && knownField.areAllKeywordsKnown()) {
                        return EMPTY_TOKENIZER;
                    }
                    name = field.getFieldName();
                    if (SKIP_SPELL_CHECK.contains(name.toLowerCase())) {
                        return EMPTY_TOKENIZER;
                    }
                }
            }
            return TEXT_TOKENIZER;
        }
        return super.getTokenizer(element);
    }
}
