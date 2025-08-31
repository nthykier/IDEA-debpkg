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

public class UpstreamMetadataFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final UpstreamMetadataFileType INSTANCE = new UpstreamMetadataFileType();

    private UpstreamMetadataFileType() {
        super(Objects.requireNonNull(Language.findLanguageByID("yaml")));
    }

    @Override
    public @NotNull String getName() {
        return "debian/upstream/metadata";
    }

    @Override
    public @NotNull String getDescription() {
        return "Upstream Metadata file for Debian packaging";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "";
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.debian/upstream/metadata.name");
    }

    @Override
    public @Nullable Icon getIcon() {
        return DEFAULT_FILE_ICON;
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        if (! file.getName().equals("metadata")) {
            return false;
        }
        var parent = file.getParent();
        if (parent == null || !parent.getName().equals("upstream")) {
            return false;
        }
        parent = parent.getParent();
        return parent != null && parent.getName().equals("debian");
    }
}
