package com.github.nthykier.debpkg.dch.psi.impl;

import com.github.nthykier.debpkg.dch.psi.DchSource;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.psi.Deb822AllParagraphs;
import com.github.nthykier.debpkg.deb822.psi.Deb822File;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DchDCtrlReference extends PsiReferenceBase<DchSource> {

    public DchDCtrlReference(@NotNull DchSource element) {
        super(element, true);
    }

    @Override
    public @Nullable PsiElement resolve() {
        PsiDirectory directory = this.myElement.getContainingFile().getContainingDirectory();
        PsiFile psiFile = directory.findFile("control");
        if (!(psiFile instanceof Deb822File)) {
            return null;
        }
        if (!psiFile.getLanguage().is(Deb822DialectDebianControlLanguage.INSTANCE)) {
            return null;
        }
        Deb822File deb822File = (Deb822File)psiFile;
        Deb822AllParagraphs allParagraphs = PsiTreeUtil.findChildOfType(deb822File, Deb822AllParagraphs.class);
        if (allParagraphs == null) {
            return null;
        }
        Deb822Paragraph paragraph = allParagraphs.getParagraphList().get(0);
        if (!myElement.getText().equals(paragraph.getName()) || !Deb822DialectDebianControlLanguage.PARAGRAPH_TYPE_SOURCE.equals(paragraph.classifyParagraph())) {
            return null;
        }
        return paragraph;
    }
}
