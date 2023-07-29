package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.github.nthykier.debpkg.DebpkgIcons.DTESTS_FILE_ICON;

public class Deb822DialectDebianTestsControlFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {

    public static final Deb822DialectDebianTestsControlFileType INSTANCE = new Deb822DialectDebianTestsControlFileType();

    private Deb822DialectDebianTestsControlFileType() {
        super(Deb822DialectDebianTestsControlLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "debian/tests/control";
    }

    @Override
    public @NotNull String getDescription() {
        return "debian/tests/control";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "";
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return Deb822Bundle.message("file.type.debian/tests/control.name");
    }

    @Override
    public @Nullable Icon getIcon() {
        return DTESTS_FILE_ICON;
    }

    @Override
    public boolean isMyFileType(@NotNull VirtualFile file) {
        if (!"control".equals(file.getName())) {
            return false;
        }
        VirtualFile parent = file.getParent();
        VirtualFile grandParent = parent != null ? parent.getParent() : null;
        if (parent == null) {
            // Prefer letting debian/control win in this case
            return false;
        }
        return  "tests".equals(parent.getName()) && (grandParent != null && grandParent.getName().equals("debian"));
    }
}
