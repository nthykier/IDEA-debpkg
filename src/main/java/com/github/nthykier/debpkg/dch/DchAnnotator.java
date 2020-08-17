package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.dch.psi.DchChangelogLine;
import com.github.nthykier.debpkg.dch.psi.impl.DchFileReference;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

public class DchAnnotator implements Annotator {

    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof DchChangelogLine) {
            checkDchChangelogLine(holder, (DchChangelogLine)element);
        }
    }

    private void checkDchChangelogLine(AnnotationHolder holder, DchChangelogLine element) {
        PsiReference[] references = element.getReferences();
        if (references != null && references.length > 0) {
            for (PsiReference ref : references) {
                if (ref instanceof DchFileReference) {
                    DchFileReference dchRef = (DchFileReference)ref;
                    TextRange rangeInElement = ref.getRangeInElement();
                    int startOffset = element.getTextOffset() + rangeInElement.getStartOffset();
                    TextRange textRange = new TextRange(
                            element.getTextOffset() + rangeInElement.getStartOffset(),
                            startOffset + rangeInElement.getLength());
                    if (dchRef.multiResolve(false).length > 0) {
                        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                .range(textRange)
                                .textAttributes(DchSyntaxHighlighter.FILE_NAME)
                                .create();
                    }
                }
            }
        }
    }
}
