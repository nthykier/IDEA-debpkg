package com.github.nthykier.debpkg.deb822.deplang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class DepLangFile extends PsiFileBase {

    private final FileType fileType;

    public DepLangFile(@NotNull FileViewProvider viewProvider, Language language, FileType fileType) {
        super(viewProvider, language);
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
