package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.github.nthykier.debpkg.DebpkgIcons.FILE_ICON;

public class Deb822DialectDebianCopyrightFileType extends LanguageFileType {

    public static final Deb822DialectDebianCopyrightFileType INSTANCE = new Deb822DialectDebianCopyrightFileType();

    private Deb822DialectDebianCopyrightFileType() {
        super(Deb822Language.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "Debian deb822 copyright File";
    }

    @Override
    public @NotNull String getDescription() {
        return "Debian machine parsable copyright (deb822) files";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "";
    }

    @Override
    public @Nullable Icon getIcon() {
        return FILE_ICON;
    }

}
