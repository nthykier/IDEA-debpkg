package com.github.nthykier.debpkg.deb822.dialects.dep5;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822ValueParts;
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
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(Deb822FieldValuePair.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        Deb822FieldValuePair fieldValuePair = (Deb822FieldValuePair)element;
                        Deb822KnownField knownField = fieldValuePair.getField().getDeb822KnownField();
                        if (knownField == null || fieldValuePair.getValueParts() == null) {
                            return PsiReference.EMPTY_ARRAY;
                        }
                        if (knownField.getCanonicalFieldName().equals("Files")) {
                            SmartList<PsiReference> references = new SmartList<>();
                            ASTNodeStringConverter converter = new ASTNodeStringConverter();
                            Deb822ValueParts valueParts = fieldValuePair.getValueParts();
                            List<List<ASTNode>> splitFieldValue = knownField.getFieldValueType().splitValue(valueParts);
                            VirtualFile rootDir = Dep5Annotator.getRootDir(fieldValuePair.getContainingFile());
                            PsiManager psiManager = PsiManager.getInstance(element.getProject());

                            if (rootDir == null) {
                                return PsiReference.EMPTY_ARRAY;
                            }

                            for (List<ASTNode> wordParts : splitFieldValue) {
                                String word = Dep5Annotator.asString(converter, wordParts);
                                TextRange range = Dep5Annotator.rangeOfWordParts(wordParts, true,
                                        valueParts.getStartOffsetInParent());
                                PathPart[] pathParts = Dep5Annotator.parsePathIntoParts(word, range, null);
                                int length;

                                if (Dep5Annotator.isStarWildcardOrEndsWithSlashStarWildCard(pathParts)) {
                                    if (pathParts.length == 1) {
                                        continue;
                                    }
                                    length = pathParts.length - 1;
                                } else {
                                    length = pathParts.length;
                                }

                                if (Arrays.stream(pathParts, 0, length)
                                        .anyMatch(p -> p.getPathType().isWildcard())) {
                                    continue;
                                }
                                detectPathReferences(rootDir, pathParts, psiManager, fieldValuePair, references);
                            }

                            return references.toArray(PsiReference.EMPTY_ARRAY);
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }

    private void detectPathReferences(@NotNull VirtualFile rootDir, PathPart[] pathParts, PsiManager psiManager,
                                      PsiElement psiElement, List<PsiReference> references) {
        VirtualFile currentPath = rootDir;
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
                    currentPath = currentPath.findChild(pathPart.getPath());
                    if (currentPath == null) {
                        return;
                    }
                    ranges.add(pathPart.getTextRange());
                    break;
                case DIRECTORY_SEPARATOR:
                    if (!currentPath.isDirectory()) {
                        return;
                    }
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
