package com.github.nthykier.debpkg.deb822.dialects.dep5;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileTargetContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Dep5FileReferenceSet extends FileReferenceSet {

    private final List<PsiFileSystemItem> packageRootDirAsList;

    public Dep5FileReferenceSet(@NotNull String str,
                                @NotNull PsiElement element,
                                int startInElement,
                                List<TextRange> referenceTextRanges,
                                List<PsiFileSystemItem> packageRootDirAsList
    ) {
        super(str, element, startInElement, null, true, false, null, false);
        this.packageRootDirAsList = packageRootDirAsList;
        this.myReferences = computeReferences(str, referenceTextRanges);
    }

    private FileReference[] computeReferences(String text, List<TextRange> referenceTextRanges) {
        FileReference[] references = new FileReference[referenceTextRanges.size()];
        int index = 0;
        for (TextRange range : referenceTextRanges) {
            TextRange shiftedRange = range.shiftLeft(this.getStartInElement());
            references[index] = createFileReference(range, index, shiftedRange.substring(text));
            index++;
        }
        return references;
    }

    @Override
    public boolean isSoft() {
        return true;
    }

    @Override
    public boolean couldBeConvertedTo(final boolean relative) {
        return false;
    }

    @Override
    public boolean absoluteUrlNeedsStartSlash() {
        return false;
    }

    @Override
    public boolean isAbsolutePathReference() {
        return true;
    }

    @Override
    public @NotNull Collection<PsiFileSystemItem> getDefaultContexts() {
        return packageRootDirAsList;
    }

    @Override
    public @NotNull Collection<PsiFileSystemItem> computeDefaultContexts() {
        return packageRootDirAsList;
    }

    @Override
    public Collection<FileTargetContext> getTargetContexts() {
        return packageRootDirAsList.stream().map(FileTargetContext::new).collect(Collectors.toList());
    }
}
