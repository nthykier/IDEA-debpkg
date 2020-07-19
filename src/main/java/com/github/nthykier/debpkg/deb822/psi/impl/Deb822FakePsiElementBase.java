package com.github.nthykier.debpkg.deb822.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.FakePsiElement;
import org.jetbrains.annotations.Nullable;

public abstract class Deb822FakePsiElementBase<T extends PsiElement> extends FakePsiElement {
    protected final T element;

    public Deb822FakePsiElementBase(T element) {
        this.element = element;
    }

    public T getOriginalElement() {
        return this.element;
    }

    @Override
    public PsiElement getParent() {
        return this.element;
    }

    /* Maybe implement along with a custom navigate function */
    @Override
    public boolean canNavigate() {
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }

    /**
     * @return The documentation of whatever we are referencing if present.
     */
    @Nullable
    public abstract String getDocumentation();
}
