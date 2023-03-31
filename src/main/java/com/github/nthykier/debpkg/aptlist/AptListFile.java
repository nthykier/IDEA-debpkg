package com.github.nthykier.debpkg.aptlist;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class AptListFile extends PsiFileBase {

    public AptListFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, AptListLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public FileType getFileType() {
        return AptListFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Apt Sources List File";
    }
}
