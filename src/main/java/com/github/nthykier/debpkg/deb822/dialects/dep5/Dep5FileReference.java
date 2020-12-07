package com.github.nthykier.debpkg.deb822.dialects.dep5;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class Dep5FileReference extends PsiPolyVariantReferenceBase<PsiElement> {

    private final PsiFileSystemItem path;

    public Dep5FileReference(@NotNull PsiElement element, TextRange range, PsiFileSystemItem path) {
        super(element, range, false);
        this.path = path;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return new ResolveResult[] {
                new PsiElementResolveResult(path),
        };
    }

}