package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.github.nthykier.debpkg.DebpkgIcons.FILE_ICON;

public class Deb822DialectAptSourcesFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final Deb822DialectAptSourcesFileType INSTANCE = new Deb822DialectAptSourcesFileType();

    private Deb822DialectAptSourcesFileType() {
        super(Deb822DialectAptSourcesLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "apt.sources";
    }

    @Override
    public @NotNull String getDescription() {
        return "apt.sources (Debian)";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return ".sources";
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.apt.sources.name");
    }

    @Override
    public @Nullable Icon getIcon() {
        return FILE_ICON;
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        return file.getName().endsWith(".sources");
    }
}
