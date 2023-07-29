package com.github.nthykier.debpkg.rules;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.github.nthykier.debpkg.DebpkgIcons.DEFAULT_FILE_ICON;

public class DebianRulesFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final DebianRulesFileType INSTANCE = new DebianRulesFileType();

    private DebianRulesFileType() {
        //super(MakefileLanguage.INSTANCE);
        super(Deb822Language.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "debian/rules file (generic)";
    }

    @Override
    public @NotNull String getDescription() {
        return "Debian debian/rules file";
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.debian/rules.name");
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
        if (!basename.equals("rules")) {
            return false;
        }
        VirtualFile parent = file.getParent();
        return parent == null || parent.getName().equals("debian");
    }
}
