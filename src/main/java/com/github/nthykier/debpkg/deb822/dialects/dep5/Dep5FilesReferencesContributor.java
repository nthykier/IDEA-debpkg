package com.github.nthykier.debpkg.deb822.dialects.dep5;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Value;
import com.github.nthykier.debpkg.deb822.psi.Deb822ValueParts;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.github.nthykier.debpkg.util.ASTNodeStringConverter;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Dep5FilesReferencesContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(Deb822Value.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        Deb822Value value = (Deb822Value)element;
                        Deb822FieldValuePair fieldValuePair = Deb822PsiImplUtil.getAncestorOfType(element, Deb822FieldValuePair.class);
                        if (fieldValuePair == null) {
                            return PsiReference.EMPTY_ARRAY;
                        }
                        Deb822KnownField knownField = fieldValuePair.getField().getDeb822KnownField();
                        if (knownField == null || fieldValuePair.getValueParts() == null) {
                            return PsiReference.EMPTY_ARRAY;
                        }
                        if (knownField.getCanonicalFieldName().equals("Files")) {
                            SmartList<PsiReference> references;
                            String word;
                            TextRange range;
                            PsiManager psiManager;
                            PathPart[] pathParts;
                            VirtualFile rootDir = Dep5Annotator.getRootDir(fieldValuePair.getContainingFile());

                            if (rootDir == null) {
                                return PsiReference.EMPTY_ARRAY;
                            }
                            word = value.getText();
                            references = new SmartList<>();
                            range = TextRange.create(0, word.length());
                            psiManager = PsiManager.getInstance(element.getProject());

                            pathParts = Dep5Annotator.parsePathIntoParts(word, range, null);
                            detectPathReferences(rootDir, pathParts, psiManager, element, references);

                            return references.toArray(PsiReference.EMPTY_ARRAY);
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }

    private void detectPathReferences(@NotNull VirtualFile rootDir, PathPart[] pathParts, PsiManager psiManager,
                                      PsiElement psiElement, List<PsiReference> references) {
        int length;
        int startOffset;
        List<TextRange> ranges = new ArrayList<>();
        Dep5FileReferenceSet referenceSet;
        StringBuilder finalPath = new StringBuilder();

        /* Special-case ".../*" and "*", which are very common patterns */
        if (Dep5Annotator.isStarWildcardOrEndsWithSlashStarWildCard(pathParts)) {
            length = pathParts.length - 1;
        } else {
            length = pathParts.length;
        }
        if (length < 1) {
            return;
        }
        startOffset = pathParts[0].getTextRange().getStartOffset();

        for (int i = 0 ; i < length ; i++) {
            PathPart pathPart = pathParts[i];
            finalPath.append(pathPart.getPath());
            switch (pathPart.getPathType()) {
                case NAME_PART:
                    ranges.add(pathPart.getTextRange());
                    break;
                case DIRECTORY_SEPARATOR:
                    break;
                case WILDCARD_QUESTION_MARK:
                case WILDCARD_STAR:
                    /* we cannot handle wildcards */
                    return;
            }
        }

        if (ranges.isEmpty()) {
            return;
        }
        referenceSet = new Dep5FileReferenceSet(
                finalPath.toString(),
                psiElement,
                startOffset,
                ranges,
                Collections.singletonList(psiManager.findDirectory(rootDir))
        );
        Collections.addAll(references, referenceSet.getAllReferences());
    }
}
