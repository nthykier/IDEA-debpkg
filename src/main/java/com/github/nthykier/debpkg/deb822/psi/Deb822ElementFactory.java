package com.github.nthykier.debpkg.deb822.psi;

import com.github.nthykier.debpkg.deb822.Deb822FileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.NotNull;

public class Deb822ElementFactory {

    public static Deb822File createFile(Project project, FileType fileType, String text) {
        String name = "dummy.deb822";
        return (Deb822File)PsiFileFactory.getInstance(project).createFileFromText(name, fileType, text);
    }

    public static Deb822FieldValuePair createFieldValuePairFromText(Project project, FileType fileType, String text) {
        Deb822AllParagraphs allParagraphs = (Deb822AllParagraphs)createFile(project, fileType, text).getFirstChild();
        return allParagraphs.getParagraphList().get(0).getFieldValuePairList().get(0);
    }

    @NotNull
    public static Deb822ValueParts createValuePartsFromText(Project project, FileType fileType, String text) {
        Deb822ValueParts parts = createFieldValuePairFromText(project, fileType, text).getValueParts();
        assert parts != null;
        return parts;
    }

    public static Deb822Substvar createSubstvarsFromText(Project project, FileType fileType, String text) {
        Deb822FieldValuePair fieldValuePair = createFieldValuePairFromText(project, fileType, text);
        Deb822ValueParts valueParts = fieldValuePair.getValueParts();
        assert valueParts != null : "Bad input to createSubstvarsFromText (Did not create a valid field)";
        return valueParts.getSubstvarList().get(0);
    }

    public static Deb822BuildProfile createBuildProfileFromText(Project project, FileType fileType, String text) {
        Deb822FieldValuePair fieldValuePair = createFieldValuePairFromText(project, fileType, text);
        Deb822ValueParts valueParts = fieldValuePair.getValueParts();
        assert valueParts != null : "Bad input to createBuildProfileFromText (Did not create a valid field)";
        assert !valueParts.getBuildProfileGroupList().isEmpty() : "Bad input to createBuildProfileFromText (Did not create a build profile - check fileType is DebianControl or similar)";
        return valueParts.getBuildProfileGroupList().get(0).getBuildProfileList().get(0);
    }
}
