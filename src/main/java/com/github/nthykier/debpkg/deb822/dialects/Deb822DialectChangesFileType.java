package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.regex.Pattern;

import static com.github.nthykier.debpkg.DebpkgIcons.FILE_ICON;

public class Deb822DialectChangesFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    private static final Pattern CHANGES_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9][a-zA-Z0-9.+\\-~:_]+_[a-zA-Z0-9][a-zA-Z0-9.+\\-~:_]+_[a-zA-Z0-9][a-zA-Z0-9.+\\-~:_]*[.]changes$"
    );

    public static final Deb822DialectChangesFileType INSTANCE = new Deb822DialectChangesFileType();

    private Deb822DialectChangesFileType() {
        super(Deb822Language.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "Debian changes File (.changes)";
    }

    @Override
    public @NotNull String getDescription() {
        return "Debian changes (upload) files";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "";
    }

    @Override
    public @Nullable Icon getIcon() {
        return FILE_ICON;
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        String basename = file.getName();
        if (!basename.endsWith(".changes")) {
            return false;
        }
        return CHANGES_PATTERN.matcher(file.getName()).matches();
    }
}
