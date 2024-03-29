package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.github.nthykier.debpkg.DebpkgIcons.DEP5_FILE_ICON;

public class Deb822DialectDebianCopyrightFileType extends LanguageFileType {

    public static final Deb822DialectDebianCopyrightFileType INSTANCE = new Deb822DialectDebianCopyrightFileType();

    private Deb822DialectDebianCopyrightFileType() {
        super(Deb822DialectDebianCopyrightLanguage.INSTANCE);
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
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.debian/copyright.name");
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "";
    }

    @Override
    public @Nullable Icon getIcon() {
        return DEP5_FILE_ICON;
    }

}
