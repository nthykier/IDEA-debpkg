package com.github.nthykier.debpkg.rules;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.lang.makefile.MakefileLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.github.nthykier.debpkg.DebpkgIcons.FILE_ICON;

public class DebianRulesFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final DebianRulesFileType INSTANCE = new DebianRulesFileType();

    private DebianRulesFileType() {
        super(MakefileLanguage.INSTANCE);
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
        if (!basename.equals("rules")) {
            return false;
        }
        VirtualFile parent = file.getParent();
        return parent == null || parent.getName().equals("debian");
    }
}
