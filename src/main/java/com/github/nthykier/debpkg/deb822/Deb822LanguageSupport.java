package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface Deb822LanguageSupport {

    Deb822LanguageSupport FALLBACK_INSTANCE = new Deb822LanguageSupport() {
        @Override
        public @NotNull Deb822ParagraphClassifier getParagraphClassifier() {
            return Deb822ParagraphClassifier.NULL_CLASSIFIER;
        }

        @Override
        public @NotNull KnownFieldTable getKnownFieldTable() {
            return KnownFieldTable.NULL_TABLE;
        }
    };

    @NotNull
    Deb822ParagraphClassifier getParagraphClassifier();

    @NotNull
    KnownFieldTable getKnownFieldTable();

    default boolean supportsEmptyFields() {
        return false;
    }

    static @NotNull Deb822LanguageSupport fromPsiElement(@NotNull PsiElement element) {
        return fromDeb822Language(element.getContainingFile().getLanguage());
    }

    static @NotNull Deb822LanguageSupport fromDeb822Language(@NotNull Language language) {
        if (language instanceof Deb822LanguageSupport) {
            return (Deb822LanguageSupport)language;
        }
        if (language.isKindOf(Deb822Language.INSTANCE)) {
            return FALLBACK_INSTANCE;
        }
        throw new IllegalArgumentException("Language must be a variant of Deb822Language");
    }
}
