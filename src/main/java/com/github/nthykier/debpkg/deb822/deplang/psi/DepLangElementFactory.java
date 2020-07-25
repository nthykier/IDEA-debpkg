package com.github.nthykier.debpkg.deb822.deplang.psi;

import com.github.nthykier.debpkg.deb822.Deb822FileType;
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

    public static DepLangAndDependencyClause createAndDependencyClause(Project project, String text) {
        return (DepLangAndDependencyClause)(createFile(project, text).getFirstChild());
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
