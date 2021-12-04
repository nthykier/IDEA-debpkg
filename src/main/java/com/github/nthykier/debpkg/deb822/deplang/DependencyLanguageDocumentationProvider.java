package com.github.nthykier.debpkg.deb822.deplang;

import com.github.nthykier.debpkg.deb822.psi.impl.Deb822FakePsiElementBase;
import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class DependencyLanguageDocumentationProvider extends AbstractDocumentationProvider {
    @Override
    public @Nullable @Nls String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        if (element instanceof Deb822FakePsiElementBase) {
            return ((Deb822FakePsiElementBase<?>)element).getDocumentation();
        }
        return null;
    }
}
