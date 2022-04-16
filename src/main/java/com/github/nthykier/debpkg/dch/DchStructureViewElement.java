package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.dch.psi.*;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DchStructureViewElement implements StructureViewTreeElement {

    private final NavigatablePsiElement myElement;

    @Override
    public Object getValue() {
        return myElement;
    }

    @Override
    public @NotNull ItemPresentation getPresentation() {
        ItemPresentation presentation;
        if (myElement instanceof DchChangelogEntry) {
            PresentationData presentationData = new PresentationData();
            presentation = presentationData;
            DchChangelogEntry changelogEntry = (DchChangelogEntry)myElement;
            DchChangelogEntry firstChangelogEntry = changelogEntry.getFirstEntry();
            DchVersionLine versionLine = changelogEntry.getVersionLine();
            DchSignoffDate signoff = changelogEntry.getSignoff().getSignoffDate();
            DchVersion dchVersion = versionLine.getVersion();
            String sourceNameOfFirstEntry = firstChangelogEntry.getVersionLine().getSource().getText();

            String sourceName = versionLine.getSource().getText();
            String sourcePart = sourceName.equals(sourceNameOfFirstEntry) ? "" : " (" + sourceName + ") ";
            String version = dchVersion != null ? dchVersion.getText() : "NO-VERSION";
            String date = signoff != null ? signoff.getSignoffDate().toString():  "N/A";
            presentationData.setPresentableText(version + sourcePart);
            if (!changelogEntry.isUnreleasedEntry()) {
                presentationData.setLocationString(Deb822Bundle.message("dch.files.structureView.versionReleasedOn", date));
            } else {
                presentationData.setLocationString(Deb822Bundle.message("dch.files.structureView.unreleasedVersion"));
            }
        } else if (myElement instanceof DchFile) {
            DchFile dchFile = (DchFile)myElement;
            VirtualFile virtualFile = dchFile.getVirtualFile();
            PresentationData presentationData = new PresentationData();
            DchChangelogEntry entry = Deb822PsiImplUtil.getNextSiblingOfType(dchFile.getFirstChild(), DchChangelogEntry.class);
            presentation = presentationData;
            String sourceName = entry != null ? entry.getVersionLine().getSource().getText() : "Unknown";
            presentationData.setPresentableText(Deb822Bundle.message("dch.files.structureView.changelogFor", sourceName));
            presentationData.setIcon(dchFile.getIcon(0));
            if (virtualFile != null && virtualFile.isValid()) {
                presentationData.setLocationString(virtualFile.getPresentableUrl());
            }
        } else {
            presentation = myElement.getPresentation();
            if (presentation == null) {
                presentation = new PresentationData();
            }
        }
        return presentation;
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        if (myElement instanceof DchFile) {
            return Arrays.stream(myElement.getChildren())
                    .filter(e -> e instanceof DchChangelogEntry)
                    .map(e -> new DchStructureViewElement((DchChangelogEntry)e))
                    .toArray(DchStructureViewElement[]::new);
        }
        return EMPTY_ARRAY;
    }

    @Override
    public void navigate(boolean requestFocus) {
        myElement.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return myElement.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return myElement.canNavigateToSource();
    }
}
