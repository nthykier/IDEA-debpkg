package com.github.nthykier.debpkg.deb822.psi;

import com.github.nthykier.debpkg.deb822.Deb822FileType;
import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class Deb822File extends PsiFileBase {

    private final FileType fileType;

    public Deb822File(@NotNull FileViewProvider viewProvider) {
        this(viewProvider, Deb822Language.INSTANCE, Deb822FileType.INSTANCE);
    }

    public Deb822File(@NotNull FileViewProvider viewProvider, Language language, FileType fileType) {
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
