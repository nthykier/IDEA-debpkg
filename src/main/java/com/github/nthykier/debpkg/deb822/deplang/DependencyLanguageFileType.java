package com.github.nthykier.debpkg.deb822.deplang;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.DebpkgIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DependencyLanguageFileType extends LanguageFileType {

    public static final DependencyLanguageFileType INSTANCE = new DependencyLanguageFileType();

    private DependencyLanguageFileType() {
        super(DependencyLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "Deb822DependencyLanguage File (internal injected format)";
    }

    @Override
    public @NotNull String getDescription() {
        return "Deb822DependencyLanguage file (internal injected format)";
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.internal/deplang.name");
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "";
    }

    @Override
    public @Nullable Icon getIcon() {
        return DebpkgIcons.FILE_ICON;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
