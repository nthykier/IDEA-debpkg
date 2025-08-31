package com.github.nthykier.debpkg.yaml;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

import static com.github.nthykier.debpkg.DebpkgIcons.DEFAULT_FILE_ICON;

public class DebputyManifestFileType extends LanguageFileType {

    public static final DebputyManifestFileType INSTANCE = new DebputyManifestFileType();

    private DebputyManifestFileType() {
        super(Objects.requireNonNull(Language.findLanguageByID("yaml")));
    }

    @Override
    public @NotNull String getName() {
        return "debian/debputy.manifest";
    }

    @Override
    public @NotNull String getDescription() {
        return "Manifest for the debputy packaging helper";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "";
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.debian/debputy.manifest.name");
    }

    @Override
    public @Nullable Icon getIcon() {
        return DEFAULT_FILE_ICON;
    }

}
