package com.github.nthykier.debpkg.dch.psi;

import com.github.nthykier.debpkg.dch.DchFileType;
import com.github.nthykier.debpkg.dch.DchLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class DchFile extends PsiFileBase {

    public DchFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, DchLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public FileType getFileType() {
        return DchFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Debian Changelog File";
    }
}
