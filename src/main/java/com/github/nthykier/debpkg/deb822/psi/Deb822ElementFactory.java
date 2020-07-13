package com.github.nthykier.debpkg.deb822.psi;

import com.github.nthykier.debpkg.deb822.Deb822FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;

public class Deb822ElementFactory {

    public static Deb822File createFile(Project project, String text) {
        String name = "dummy.deb822";
        return (Deb822File)PsiFileFactory.getInstance(project).createFileFromText(name, Deb822FileType.INSTANCE, text);
    }

    public static Deb822FieldValuePair createFieldValuePairFromText(Project project, String text) {
        Deb822AllParagraphs allParagraphs = (Deb822AllParagraphs)createFile(project, text).getFirstChild();
        return allParagraphs.getParagraphList().get(0).getFieldValuePairList().get(0);
    }
}
