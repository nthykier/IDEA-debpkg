package com.github.nthykier.debpkg.deb822.psi;

import com.github.nthykier.debpkg.deb822.Deb822FileType;
import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class Deb822File extends PsiFileBase {

    public Deb822File(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, Deb822Language.INSTANCE);
    }

    @Override
    @NotNull
    public FileType getFileType() {
        return Deb822FileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Deb822 (generic) File";
    }
}
