package com.github.nthykier.debpkg.deb822.dialects.dep5;

import com.github.nthykier.debpkg.deb822.Deb822LexerAdapter;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Dep5FindUsagesProvider implements FindUsagesProvider {

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(new Deb822LexerAdapter(),
                TokenSet.EMPTY,
                TokenSet.create(Deb822Types.COMMENT),
                TokenSet.create(Deb822Types.VALUE_TOKEN));
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiFileSystemItem;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof PsiFileSystemItem) {
            PsiFileSystemItem item = (PsiFileSystemItem)element;
            return item.isDirectory() ? "directory" : "file";
        } else {
            return "";
        }
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof PsiFileSystemItem) {
            PsiFileSystemItem item = (PsiFileSystemItem)element;
            return item.getName();
        }
        return "";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof PsiFileSystemItem) {
            PsiFileSystemItem item = (PsiFileSystemItem)element;
            return item.getName();
        }
        return "";
    }
}
