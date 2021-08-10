package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.github.nthykier.debpkg.DebpkgIcons.FILE_ICON;

public class DchFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final DchFileType INSTANCE = new DchFileType();

    private DchFileType() {
        super(DchLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "Debian Changelog";
    }

    @Override
    public @NotNull String getDescription() {
        return "Debian Changelog";
    }


    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.dch.name");
    }


    @Override
    public @NotNull String getDefaultExtension() {
        return "dch";
    }

    @Override
    public @Nullable Icon getIcon() {
        return FILE_ICON;
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        return "changelog".equals(file.getName()) && "debian".equals(file.getParent().getName());
    }
}
