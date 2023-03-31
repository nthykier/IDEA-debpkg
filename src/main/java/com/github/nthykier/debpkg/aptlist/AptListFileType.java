package com.github.nthykier.debpkg.aptlist;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.github.nthykier.debpkg.DebpkgIcons.FILE_ICON;

public class AptListFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final AptListFileType INSTANCE = new AptListFileType();

    private AptListFileType() {
        super(AptListLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "apt.sources.list";
    }

    @Override
    public @NotNull String getDescription() {
        return "Apt Sources List file";
    }


    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.apt.sources.list.name");
    }


    @Override
    public @NotNull String getDefaultExtension() {
        return "list";
    }

    @Override
    public @Nullable Icon getIcon() {
        return FILE_ICON;
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        final String name = file.getName();
        return name.endsWith(".list") && !name.equalsIgnoreCase("index.list");
    }
}
