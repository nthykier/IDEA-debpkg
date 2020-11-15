package com.github.nthykier.debpkg.deb822.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class Deb822File extends PsiFileBase {

    private final FileType fileType;

    public Deb822File(@NotNull FileViewProvider viewProvider, LanguageFileType fileType) {
        super(viewProvider, fileType.getLanguage());
        this.fileType = fileType;
    }

    @Override
    @NotNull
    public FileType getFileType() {
        return fileType;
    }

    @Override
    public String toString() {
        return fileType.getDescription() + " File";
    }
}
