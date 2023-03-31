package com.github.nthykier.debpkg.dch.psi.impl;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.FakePsiElement;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class DchFileReference extends PsiPolyVariantReferenceBase<PsiElement> {

    private static final Pattern DIR_SEPARATOR_SPLITTER = Pattern.compile("/++");

    private final String basename;
    private final String[] dirParts;
    private final String altBase;

    public DchFileReference(@NotNull PsiElement element, TextRange range, String fileName) {
        super(element, range, false);
        if (fileName.endsWith("/")) {
            throw new IllegalArgumentException("DchFileReference does not work with directories (so paths ending with \"/\" like \"" + fileName + "\" will not work)");
        }
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
    public @NotNull ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        Project project = this.myElement.getProject();
        Collection<VirtualFile> results = FilenameIndex.getVirtualFilesByName(basename, GlobalSearchScope.allScope(project));
        if (results.isEmpty()) {
            return ResolveResult.EMPTY_ARRAY;
        }
        PsiManager psiManager = PsiManager.getInstance(project);
        Function<VirtualFile, PsiFileSystemItem> resolvePsi = (f) -> f.isDirectory() ? psiManager.findDirectory(f) : psiManager.findFile(f);
        /* Wrap the PsiFileSystemItem to neuter rename/refactor support.  We do not want to update every link in the
         * changelog as it would carelessly end up rewriting the history.
         */
        Function<PsiFileSystemItem, PsiElementResolveResult> wrapper = (psi) -> new PsiElementResolveResult(new PathReference(this.myElement, this.getRangeInElement(), psi));
        Predicate<PsiFileSystemItem> matching = dirParts != null
                ? (psi -> psi != null && this.checkMatch(psi))
                : Objects::nonNull;
        return results.stream()
                .filter(VirtualFile::isValid)
                .map(resolvePsi)
                .filter(matching)
                .map(wrapper)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    private boolean checkMatch(PsiFileSystemItem psiFileSystemItem) {
        PsiFileSystemItem element = psiFileSystemItem;
        int i;
        boolean match = false;
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

    private static class PathReference extends FakePsiElement {

        private final PsiElement myElement;
        private final PsiFileSystemItem target;
        private final TextRange range;

        PathReference(PsiElement element, TextRange range, PsiFileSystemItem fileSystemItem) {
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
        public String getName() {
            return target.getName();
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
