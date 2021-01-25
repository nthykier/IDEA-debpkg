package com.github.nthykier.debpkg.util;

import com.intellij.lang.Language;
import com.intellij.openapi.paths.WebReference;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.io.URLUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommonPsiUtil {

    public static boolean isCorrectFileLanguage(@NotNull final PsiElement element, @NotNull Language language) {
        return element.getContainingFile().getLanguage().equals(language);
    }

    public static void addURLReferences(PsiElement psiElement, String text, List<PsiReference> references) {
        int startOffset = 0;
        int endOffset = text.length();
        do {
            TextRange range = URLUtil.findUrl(text, startOffset, endOffset);
            if (range == null) {
                break;
            }
            startOffset = range.getEndOffset() + 1;
            references.add(new WebReference(psiElement, range));
        } while (startOffset < endOffset);
    }
}
