package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvar;
import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvars;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.github.nthykier.debpkg.deb822.psi.Deb822Substvar;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822FieldFakePsiElement;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822SubstvarFakePsiElement;
import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Deb822DialectDebianControlDocumentationProvider extends AbstractDocumentationProvider {
    @Override
    public @Nullable String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        if (element instanceof Deb822FieldFakePsiElement) {
            Deb822Field field = ((Deb822FieldFakePsiElement)element).getOriginalElement();
            String fieldName = field.getText().toLowerCase();
            Deb822KnownField knownField = Deb822KnownFieldsAndValues.lookupDeb822Field(fieldName);
            if (knownField != null) {
                String docs = knownField.getFieldDescription();
                if (docs == null) {
                    docs = knownField.getCanonicalFieldName() + " (standard field; no documentation available)";
                }
                return docs;
            } else {
                return fieldName + " (no documentation available)";
            }
        } else if (element instanceof Deb822SubstvarFakePsiElement) {
            Deb822Substvar substvar = ((Deb822SubstvarFakePsiElement)element).getOriginalElement();
            String name = substvar.getText();
            Deb822KnownSubstvar knownSubstvar = Deb822KnownSubstvars.lookupSubstvar(name);
            if (knownSubstvar != null) {
                String docs = knownSubstvar.getDescription();
                String value = renderSubstvarValue(knownSubstvar.getPredefinedValue());
                if (docs != null) {
                   return name  + value + "<br><br>" + docs;
                } else {
                    return name + value + "<br><br>[Standard substvar; no documentation available]";
                }
            } else {
                return name + "<br><br>[Custom substvar; no documentation available]";
            }
        }
        return null;
    }

    private @NotNull String renderSubstvarValue(@Nullable String value) {
        if (value == null) {
            return " -> <em>(Runtime defined value)</em>";
        }
        return " -> " + value.replace("\t", "\\t").replace("\n", "\\n");
    }
}
