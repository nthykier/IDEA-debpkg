package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.psi.impl.Deb822FakePsiElementBase;
import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public class Deb822DialectDebianControlDocumentationProvider extends AbstractDocumentationProvider {
    @Override
    public @Nullable String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        if (element instanceof Deb822FakePsiElementBase) {
            return ((Deb822FakePsiElementBase)element).getDocumentation();
        }
        return null;
    }

}
