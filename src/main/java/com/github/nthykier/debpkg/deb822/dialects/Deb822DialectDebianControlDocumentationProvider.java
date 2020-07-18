package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822FieldFakePsiElement;
import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public class Deb822DialectDebianControlDocumentationProvider extends AbstractDocumentationProvider {
    @Override
    public @Nullable String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        if (element instanceof Deb822FieldFakePsiElement) {
            Deb822Field field = ((Deb822FieldFakePsiElement)element).getField();
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
        }
        return null;
    }
}
