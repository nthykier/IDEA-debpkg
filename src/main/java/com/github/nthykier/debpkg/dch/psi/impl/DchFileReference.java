package com.github.nthykier.debpkg.dch.psi.impl;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.FakePsiElement;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;

public class DchFileReference extends PsiPolyVariantReferenceBase<PsiElement> {

    private static final Pattern DIR_SEPARATOR_SPLITTER = Pattern.compile("/++");

    private final String basename;
    private final String[] dirParts;
    private final String altBase;

    public DchFileReference(@NotNull PsiElement element, TextRange range, String fileName) {
        super(element, range, false);
        this.basename = extractBasename(fileName);
        if (fileName.contains("/")) {
            dirParts = DIR_SEPARATOR_SPLITTER.split(fileName);
            assert this.basename.equals(dirParts[dirParts.length - 1]);
            /* It is common practise in Debian to shorten the "debian/" directory to "d/".  We might as well match
             * their expectations.
             */
            altBase = dirParts[0].equals("d") ? "debian" : null;
        } else {
            dirParts = null;
            altBase = null;
        }
    }

    @Override
    public @NotNull ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = this.myElement.getProject();
        PsiFile[] results = FilenameIndex.getFilesByName(project, basename, GlobalSearchScope.allScope(project));
        Function<PsiFileSystemItem, PsiElement> wrapper;
        if (results.length == 0) {
            return ResolveResult.EMPTY_ARRAY;
        }
        /* Wrap the PsiFileSystemItem to neuter rename/refactor support.  We do not want to update every link in the
         * changelog as it would carelessly end up rewriting the history.
         */
        wrapper = (PsiFileSystemItem f) -> new WrappingPsiElement(this.myElement, this.getRangeInElement(), f);
        if (dirParts != null) {
            return Arrays.stream(results).filter(this::checkMatch).map(wrapper).map(PsiElementResolveResult::new).toArray(ResolveResult[]::new);
        }
        return Arrays.stream(results).map(wrapper).map(PsiElementResolveResult::new).toArray(ResolveResult[]::new);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    private boolean checkMatch(PsiFileSystemItem psiFileSystemItem) {
        PsiFileSystemItem element = psiFileSystemItem;
        int i;
        boolean match = true;
        for (i = dirParts.length - 1; i >= 0 ; i--) {
            String expected = dirParts[i];
            String actual = element.getName();
            match = expected.equals(actual);
            if (!match && i == 0 && altBase != null) {
                match = altBase.equals(actual);
            }
            if (!match) {
                /* Not a match */
                break;
            }
            element = element.getParent();
            if (element == null) {
                match = i == 0;
                break;
            }
        }
        return match;
    }

    private static @NotNull String extractBasename(@NotNull String fullname) {
        int slash = fullname.lastIndexOf('/');
        if (slash == -1) {
            return fullname;
        }
        assert slash + 1 != fullname.length();
        return fullname.substring(slash + 1);
    }

    private static class WrappingPsiElement extends FakePsiElement {

        private final PsiElement myElement;
        private final PsiFileSystemItem target;
        private final TextRange range;

        WrappingPsiElement(PsiElement element, TextRange range, PsiFileSystemItem fileSystemItem) {
            this.myElement = element;
            this.target = fileSystemItem;
            this.range = range;
        }

        @Override
        public PsiElement getParent() {
            return myElement;
        }

        @Override
        public @NotNull TextRange getTextRangeInParent() {
            return range;
        }

        @Override
        public ItemPresentation getPresentation() {
            return target.getPresentation();
        }

        @Override
        public void navigate(boolean requestFocus) {
            target.navigate(requestFocus);
        }

        @Override
        public boolean canNavigate() {
            return target.canNavigate();
        }

        @Override
        public boolean canNavigateToSource() {
            return target.canNavigateToSource();
        }

        @Override
        public @NotNull Project getProject() {
            return target.getProject();
        }
    }
}
