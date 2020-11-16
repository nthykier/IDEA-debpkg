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

    private static final Set<String> SPELL_CHECK_UNKNOWN_FIELDS_BY_NAME = new HashSet<>(Arrays.asList(
            "description", "comment", "disclaimer"
    ));

    @NotNull
    public Tokenizer<?> getTokenizer(PsiElement element) {
        if (element instanceof Deb822ValueParts) {
            Deb822FieldValuePair parent = Deb822PsiImplUtil.getAncestorOfType(element, Deb822FieldValuePair.class);
             if (parent != null) {
                Deb822Field field = parent.getField();
                Deb822KnownField knownField = field.getDeb822KnownField();
                if (knownField == null) {
                    if (SPELL_CHECK_UNKNOWN_FIELDS_BY_NAME.contains(field.getFieldName().toLowerCase())) {
                        return TEXT_TOKENIZER;
                    }
                    return EMPTY_TOKENIZER;
                }
                if (!knownField.isSpellcheckForValueEnabled()) {
                    return EMPTY_TOKENIZER;
                }
            }
            return TEXT_TOKENIZER;
        }
        return super.getTokenizer(element);
    }
}
