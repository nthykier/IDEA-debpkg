package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822ValueParts;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.lang.Language;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;

public class Deb822DependencyLanguageInjector implements LanguageInjector {

    @Override
    public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host instanceof Deb822ValueParts) {
            Deb822ValueParts parts = (Deb822ValueParts)host;
            Deb822FieldValuePair fieldValuePair;
            Deb822KnownField knownField;
            Language languageInstance;
            if (parts.getContainingFile().getLanguage() != Deb822DialectDebianControlLanguage.INSTANCE) {
                return;
            }

            fieldValuePair = Deb822PsiImplUtil.getAncestorOfType(parts, Deb822FieldValuePair.class);

            if (fieldValuePair == null) {
                return;
            }
            knownField = fieldValuePair.getField().getDeb822KnownField();
            if (knownField == null) {
                return;
            }
            languageInstance = knownField.getFieldValueLanguage().getLanguage();
            if (languageInstance == null) {
                return;
            }

            injectionPlacesRegistrar.addPlace(
                    languageInstance,
                    new TextRange(0, host.getTextLength()),
                    fieldValuePair.getField().getFieldName().toLowerCase() + " -- ",
                    null);
        }
    }
}
