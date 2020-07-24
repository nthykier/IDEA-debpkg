package com.github.nthykier.debpkg.util;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/* The Type var is used as a tagging to help detect type issues with createAnnotationWithQuickFix */
@SuppressWarnings({"unused"})
public interface Deb822TypeSafeLocalQuickFix<T extends PsiElement> extends LocalQuickFix {

    @NotNull
    String getBaseName();

    @Override
    default @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
        return Deb822Bundle.message("deb822.files.quickfix.fields." + getBaseName() + ".name");
    }
}
