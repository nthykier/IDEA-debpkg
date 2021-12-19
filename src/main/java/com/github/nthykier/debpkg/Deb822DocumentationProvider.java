package com.github.nthykier.debpkg;

import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822FakePsiElementBase;
import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class Deb822DocumentationProvider extends AbstractDocumentationProvider {

    @Override
    public @Nullable @Nls String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        if (element instanceof Deb822FakePsiElementBase) {
            return ((Deb822FakePsiElementBase<?>)element).getPresentableText();
        }
        if (element instanceof Deb822Paragraph) {
            Deb822Paragraph target = (Deb822Paragraph)element;
            return "Package " + target.getName();
        }
        return super.getQuickNavigateInfo(element, originalElement);
    }

    @Override
    public @Nullable @Nls String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        if (element instanceof Deb822FakePsiElementBase) {
            return ((Deb822FakePsiElementBase<?>)element).getDocumentation();
        }
        return null;
    }
}
