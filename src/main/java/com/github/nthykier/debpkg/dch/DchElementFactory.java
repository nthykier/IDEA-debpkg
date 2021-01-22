package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.dch.psi.DchChangelogEntry;
import com.github.nthykier.debpkg.dch.psi.DchFile;
import com.github.nthykier.debpkg.dch.psi.DchSignoff;
import com.github.nthykier.debpkg.dch.psi.DchSignoffDate;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Substvar;
import com.github.nthykier.debpkg.deb822.psi.Deb822ValueParts;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.github.nthykier.debpkg.deb822.psi.Deb822ElementFactory.createFieldValuePairFromText;

public class DchElementFactory {

    public static DchFile createFile(Project project, String text) {
        String name = "dummy.dch";
        return (DchFile)PsiFileFactory.getInstance(project).createFileFromText(name, DchFileType.INSTANCE, text);
    }

    public static DchSignoff createSignoff(Project project, String text) {
        DchChangelogEntry changelogEntry = (DchChangelogEntry)createFile(project, text).getFirstChild();
        return changelogEntry.getSignoff();
    }

    @NotNull
    public static DchSignoffDate createSignoffDate(Project project, String date) {
        String text = "foo (1.0) unstable; urgency=low\n * foo\n -- Someone <somewhare@example.com>  " + date;
        return Objects.requireNonNull(createSignoff(project, text).getSignoffDate());
    }

}
