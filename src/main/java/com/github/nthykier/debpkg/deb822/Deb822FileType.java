package com.github.nthykier.debpkg.deb822;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class Deb822FileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final Deb822FileType INSTANCE = new Deb822FileType();

    private Deb822FileType() {
        super(Deb822Language.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "Deb822 File (generic)";
    }

    @Override
    public @NotNull String getDescription() {
        return "Deb822 file (any)";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "deb822";
    }

    @Override
    public @Nullable Icon getIcon() {
        return null;
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        return "control".equals(file.getName()) && "debian".equals(file.getParent().getName());
    }
}
