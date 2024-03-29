package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.regex.Pattern;

import static com.github.nthykier.debpkg.DebpkgIcons.DEFAULT_FILE_ICON;

public class Deb822DialectBuildinfoFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    private static final Pattern CHANGES_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9][a-zA-Z0-9.+\\-~:_]+_[a-zA-Z0-9][a-zA-Z0-9.+\\-~:_]+_[a-zA-Z0-9][a-zA-Z0-9.+\\-~:_]*[.]buildinfo$"
    );

    public static final Deb822DialectBuildinfoFileType INSTANCE = new Deb822DialectBuildinfoFileType();

    private Deb822DialectBuildinfoFileType() {
        super(Deb822Language.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "Debian buildinfo File (.buildinfo)";
    }

    @Override
    public @NotNull String getDescription() {
        return "Debian buildinfo files";
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.buildinfo.name");
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "";
    }

    @Override
    public @Nullable Icon getIcon() {
        return DEFAULT_FILE_ICON;
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        String basename = file.getName();
        if (!basename.endsWith(".buildinfo")) {
            return false;
        }
        return CHANGES_PATTERN.matcher(file.getName()).matches();
    }
}
