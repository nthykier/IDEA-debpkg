package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

import static com.github.nthykier.debpkg.DebpkgIcons.FILE_ICON;

public class Deb822FileType extends LanguageFileType {

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
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.deb822.name");
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "deb822";
    }

    @Override
    public @Nullable Icon getIcon() {
        return FILE_ICON;
    }

}
