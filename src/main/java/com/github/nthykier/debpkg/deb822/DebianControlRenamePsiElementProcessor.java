package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.psi.Deb822AllParagraphs;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.SearchScope;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.intellij.util.containers.MultiMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage.PARAGRAPH_TYPE_BINARY_PACKAGE;

public class DebianControlRenamePsiElementProcessor extends RenamePsiElementProcessor {

    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return element instanceof Deb822Paragraph
                && element.getContainingFile().getLanguage().equals(Deb822DialectDebianControlLanguage.INSTANCE)
                && PARAGRAPH_TYPE_BINARY_PACKAGE.equals(((Deb822Paragraph)element).classifyParagraph());
    }

    public void prepareRenaming(@NotNull PsiElement element,
                                @NotNull String newName,
                                @NotNull Map<PsiElement, String> allRenames,
                                @NotNull SearchScope scope) {

        PsiFile dctrl = element.getContainingFile();
        PsiDirectory debianDir = null;
        if (dctrl != null) {
            debianDir = dctrl.getParent();
        }
        if (debianDir != null) {
            assert element instanceof Deb822Paragraph;
            String prefix = ((Deb822Paragraph)element).getName();
            assert prefix != null;
            prefix += ".";
            final int prefixLen = prefix.length();
            for (PsiFile file : debianDir.getFiles()) {
                if (file.isDirectory()) {
                    continue;
                }
                String filename = file.getName();
                if (filename.startsWith(prefix) && filename.length() + 1 > prefixLen) {
                    allRenames.put(file, newName + "." + filename.substring(prefixLen));
                }
            }
        }
    }

    @Override
    public void findExistingNameConflicts(@NotNull final PsiElement element, @NotNull final String newName, @NotNull final MultiMap<PsiElement, String> conflicts) {
        assert element instanceof Deb822Paragraph;

        Deb822Paragraph containingParagraph = (Deb822Paragraph) element;
        Deb822AllParagraphs allParagraphs = Deb822PsiImplUtil.getAncestorOfType(containingParagraph, Deb822AllParagraphs.class);
        if (allParagraphs == null) {
            return;
        }
        assert PARAGRAPH_TYPE_BINARY_PACKAGE.equals(containingParagraph.classifyParagraph());
        for (Deb822Paragraph paragraph : allParagraphs.getParagraphList()) {
            if (paragraph == containingParagraph || !PARAGRAPH_TYPE_BINARY_PACKAGE.equals(paragraph.classifyParagraph())) {
                continue;
            }
            String packageName = paragraph.getName();
            if (newName.equals(packageName)) {
                conflicts.put(paragraph, Collections.singletonList(Deb822Bundle.message("dctrl.refactor.name.already.taken", packageName)));
            }
        }
    }
}
