package com.github.nthykier.debpkg.deb822.dialects;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.util.io.ByteSequence;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class Deb822DialectDebianCopyrightFileTypeDetector implements FileTypeRegistry.FileTypeDetector {

    private static final Pattern DETECT_DEB822_FORMAT = Pattern.compile(
            "^Format\\s*:\\s*https?://www[.]debian[.]org/doc/packaging-manuals/copyright-format/\\d+[.]\\d+"
    );

    @Override
    public @Nullable FileType detect(@NotNull VirtualFile file, @NotNull ByteSequence firstBytes, @Nullable CharSequence firstCharsIfText) {
        /* It must definitely be a text file and be called "copyright" */
        if (firstCharsIfText == null || !"copyright".equals(file.getName())) {
            return null;
        }
        /* Ensure it is a debian/copyright */
        if (file.getParent() != null && !"debian".equals(file.getParent().getName())) {
            return null;
        }

        /* Now we are ready to work with the actual content */
        if (!DETECT_DEB822_FORMAT.matcher(firstCharsIfText).find()) {
            return null;
        }
        return Deb822DialectDebianCopyrightFileType.INSTANCE;
    }

    @SuppressWarnings({"deprecated"})
    @Override
    public int getVersion() {
        return 0;
    }
}
