package com.github.nthykier.debpkg.deb822.deplang.psi;

import com.github.nthykier.debpkg.deb822.deplang.DepLangFile;
import com.github.nthykier.debpkg.deb822.deplang.DependencyLanguageFileType;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;

public class DepLangElementFactory {

    public static DepLangFile createFile(Project project, String text) {
        String name = "dummy.deplang";
        return (DepLangFile)PsiFileFactory.getInstance(project).createFileFromText(name, DependencyLanguageFileType.INSTANCE, text);
    }

    public static DepLangDependencyInfo createDependencyInfo(Project project, String text) {
        return (DepLangDependencyInfo)(createFile(project, text).getFirstChild());
    }

    public static DepLangAndDependencyClause createAndDependencyClause(Project project, String text) {
        return createDependencyInfo(project, text).getAndDependencyClauseList().get(0);
    }


    public static DepLangOrDependencyClause createOrDependencyClause(Project project, String text) {
        return createAndDependencyClause(project, text).getOrDependencyClauseList().get(0);
    }

    public static DepLangDependency createDependency(Project project, String text) {
        return createOrDependencyClause(project, text).getDependencyList().get(0);
    }

    public static Deb822SubstvarBase createSubstvar(Project project, String text) {
        Deb822SubstvarBase substvar = createDependency(project, text).getPackageName().getSubstvar();
        assert substvar != null;
        return substvar;
    }

    public static DepLangVersionPart createVersionPart(Project project, String text) {
        DepLangVersionPart versionPart = createDependency(project, text).getVersionPart();
        assert versionPart != null;
        return versionPart;
    }
}
