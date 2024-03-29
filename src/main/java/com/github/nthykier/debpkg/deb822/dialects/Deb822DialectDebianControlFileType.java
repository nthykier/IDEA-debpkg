package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.github.nthykier.debpkg.DebpkgIcons.DEFAULT_FILE_ICON;

public class Deb822DialectDebianControlFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final Deb822DialectDebianControlFileType INSTANCE = new Deb822DialectDebianControlFileType();

    private Deb822DialectDebianControlFileType() {
        super(Deb822DialectDebianControlLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "debian/control";
    }

    @Override
    public @NotNull String getDescription() {
        return "debian/control";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "";
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.debian/control.name");
    }

    @Override
    public @Nullable Icon getIcon() {
        return DEFAULT_FILE_ICON;
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        return "control".equals(file.getName()) && (file.getParent() == null || "debian".equals(file.getParent().getName()));
    }
}
