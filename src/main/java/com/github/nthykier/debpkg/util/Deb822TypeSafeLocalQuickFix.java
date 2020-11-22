package com.github.nthykier.debpkg.util;

import com.intellij.psi.PsiElement;

/* The Type var is used as a tagging to help detect type issues with createAnnotationWithQuickFix */
@SuppressWarnings({"unused"})
public interface Deb822TypeSafeLocalQuickFix<T extends PsiElement> extends Deb822LocalQuickFix {

}
