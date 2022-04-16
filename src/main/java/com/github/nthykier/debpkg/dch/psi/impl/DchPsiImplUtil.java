package com.github.nthykier.debpkg.dch.psi.impl;

import com.github.nthykier.debpkg.dch.psi.*;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class DchPsiImplUtil {

    public static @Nullable PsiReference getReference(@NotNull DchSource dchSource) {
        DchChangelogEntry change = Deb822PsiImplUtil.getAncestorOfType(dchSource, DchChangelogEntry.class);
        if (change == null) {
            return null;
        }
        if (!isFirstChangelogEntry(change)) {
            // Only the latest entry can be renamed (refactor).
            return null;
        }
        if (!isUnreleasedEntry(change)) {
            // Only UNRELEASED ones can be renamed (refactor).
            return null;
        }
        return new DchDCtrlReference(dchSource);
    }

    public static boolean isFirstChangelogEntry(@NotNull DchChangelogEntry dchChangelogEntry) {
        DchChangelogEntry previous = Deb822PsiImplUtil.getPreviousSiblingOfType(dchChangelogEntry.getPrevSibling(), DchChangelogEntry.class);
        return previous == null;
    }

    public static boolean isUnreleasedEntry(@NotNull DchChangelogEntry dchChangelogEntry) {
        List<DchDistribution> dists = dchChangelogEntry.getVersionLine().getDistributionList();
        return dists.size() == 1 && dists.get(0).getText().equals("UNRELEASED");
    }

    public static DchChangelogEntry getFirstEntry(@NotNull DchChangelogEntry dchChangelogEntry) {
        return Deb822PsiImplUtil.getNextSiblingOfType(dchChangelogEntry.getParent().getFirstChild(), DchChangelogEntry.class);
    }

    public static ItemPresentation getPresentation(@NotNull DchChangelogEntry dchChangelogEntry) {
        DchVersionLine versionLine = dchChangelogEntry.getVersionLine();
        DchSignoffDate signoff = dchChangelogEntry.getSignoff().getSignoffDate();
        DchVersion dchVersion = versionLine.getVersion();
        String sourceName = versionLine.getSource().getText();
        String version = dchVersion != null ? dchVersion.getText() : "NO-VERSION";
        String dist = versionLine.getDistributionList().stream().map(PsiElement::getText).collect(Collectors.joining(", ", " (", ")"));
        String date = signoff != null ? signoff.getSignoffDate().toString():  "N/A";
        String presentation = sourceName + " " + version + " [" + date + "]" + dist;
        String locationString = null;
        PsiFile dchFile = dchChangelogEntry.getContainingFile();
        VirtualFile virtualFile = dchFile.getVirtualFile();
        if (virtualFile != null && virtualFile.isValid()) {
            locationString = virtualFile.getPresentableUrl();
        }
        return new PresentationData(presentation,
                locationString,
                dchFile.getIcon(0),
                null
                );
    }
}
