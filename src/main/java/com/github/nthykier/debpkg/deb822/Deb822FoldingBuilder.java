package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.github.nthykier.debpkg.util.ASTNodeStringConverter;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Deb822FoldingBuilder extends FoldingBuilderEx implements DumbAware {

    private static final FoldingDescriptor[] EMPTY_ARRAY = new FoldingDescriptor[0];

    @NotNull
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        Collection<Deb822FieldValuePair> fieldValuePairs =
                PsiTreeUtil.findChildrenOfType(root, Deb822FieldValuePair.class);

        Collection<Deb822GpgSignature> signatures = PsiTreeUtil.findChildrenOfType(root, Deb822GpgSignature.class);
        Collection<Deb822GpgSigned> gpgSignedStatements = PsiTreeUtil.findChildrenOfType(root, Deb822GpgSigned.class);

        processComments(root, descriptors);

        for (Deb822GpgSignature signature : signatures) {
            descriptors.add(new FoldingDescriptor(
                    signature.getNode(),
                    signature.getTextRange(),
                    null,
                    Collections.emptySet(),
                    false,
                    "-----BEGIN PGP SIGNATURE-----",
                    true
            ));
        }

        for (Deb822GpgSigned gpgSignedStatement : gpgSignedStatements) {
            ASTNode node = gpgSignedStatement.getNode();
            TextRange range = node.getTextRange();
            /* The ASTNode always ends with two newline characters; keep them unfolded for readability */
            TextRange reducedRange = new TextRange(range.getStartOffset(), range.getEndOffset() - 2);

            descriptors.add(new FoldingDescriptor(
                    node,
                    reducedRange,
                    null,
                    Collections.emptySet(),
                    false,
                    "-----BEGIN PGP SIGNED MESSAGE-----",
                    true
            ));
        }

        for (final Deb822FieldValuePair fieldValuePair : fieldValuePairs) {
            Deb822KnownField knownField = fieldValuePair.getField().getDeb822KnownField();
            Deb822ValueParts valueParts = fieldValuePair.getValueParts();
            String placeholderText;
            ASTNode node;
            boolean foldedByDefault;
            ASTNode separatorNode;
            PsiElement firstElementAfter;
            int end;

            /* We only need a FoldingDescriptor if it is a multi-line field */
            if (valueParts == null || !fieldValuePair.textContains('\n')) {
                continue;
            }
            foldedByDefault = knownField != null && knownField.isFoldedByDefault();
            node = fieldValuePair.getNode();
            separatorNode = node.findChildByType(Deb822Types.SEPARATOR);
            assert separatorNode != null;
            placeholderText = getPlaceholderText(node);
            firstElementAfter = Deb822PsiImplUtil.getNextSiblingMatchingCondition(
                    fieldValuePair.getNextSibling(),
                    e -> !(e instanceof PsiWhiteSpace)
            );

            /* Always fold single line fields that with an empty bit */
            if (!foldedByDefault && placeholderText != null && !placeholderText.endsWith(" {...}")) {
                foldedByDefault = true;
            }

            if (firstElementAfter != null) {
                /* -1 for the newline at the end of the previous */
                end = firstElementAfter.getNode().getStartOffset() - 1;
            } else {
                end = fieldValuePair.getContainingFile().getTextLength() - 1;
            }
            descriptors.add(new FoldingDescriptor(
                    node,
                    new TextRange(separatorNode.getStartOffset() + 1, end),
                    null,
                    Collections.emptySet(),
                    false,
                    placeholderText,
                    foldedByDefault
            ));
        }
        return descriptors.toArray(EMPTY_ARRAY);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        String retTxt = "...";
        if (node.getTextLength() == 0) {
            /* should not happen, but it avoids logical corner cases of returning "..." when there is nothing */
            return "";
        }
        if (node.getElementType() == Deb822Types.FIELD_VALUE_PAIR) {
            final StringBuilder placeholderBuilder = new StringBuilder(" ");
            ASTNodeStringConverter converter = new ASTNodeStringConverter(placeholderBuilder);
            ASTNode partsNode = node.findChildByType(Deb822Types.VALUE_PARTS);
            ASTNode currentChild;
            ASTNode nextChild = partsNode != null ? partsNode.getFirstChildNode() : null;
            final int initialSize = placeholderBuilder.length();

            while (nextChild != null) {
                currentChild = nextChild;
                nextChild = nextChild.getTreeNext();
                if (placeholderBuilder.length() == initialSize) {
                    if (currentChild.getElementType() != TokenType.WHITE_SPACE) {
                        converter.readTextFromNode(currentChild);
                    }
                } else if (currentChild.textContains('\n')) {
                    /* We add an ellipsis when there is (or might be) some hidden text */
                    placeholderBuilder.append(" {...}");
                    break;
                } else {
                    converter.readTextFromNode(currentChild);
                }
            }

            retTxt = placeholderBuilder.toString();
        }
        return retTxt;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        /* We set up "Collapsed by default" in the FoldingDescriptors */
        return false;
    }

    private static void processComments(PsiElement root, List<FoldingDescriptor> descriptors) {
        List<PsiComment> comments = new ArrayList<>();
        // Use (root, PsiComment.class, ...) when 201 compat can be dropped.
        PsiTreeUtil.processElements(root, e -> {
            if (e instanceof PsiComment) {
                comments.add((PsiComment)e);
            }
            return true;
        });
        PsiComment currentStart = null;
        PsiComment currentComment = null;
        for (PsiComment nextComment : comments) {
            if (nextComment.getPrevSibling() == currentComment) {
                currentComment = nextComment;
            } else {
                if (currentStart != currentComment) {
                    descriptors.add(fromMultipleAdjacentComments(currentStart, currentComment));
                }
                currentComment = currentStart = nextComment;
            }
        }
        if (currentStart != currentComment) {
            descriptors.add(fromMultipleAdjacentComments(currentStart, currentComment));
        }
    }

    private static FoldingDescriptor fromMultipleAdjacentComments(PsiComment startComment, PsiComment endComment) {
        TextRange range = new TextRange(
                startComment.getTextOffset(),
                // Comments include EOL chars, but we want to keep the last one, so that folding
                // will look "correct"
                endComment.getTextRange().getEndOffset() - 1
        );
        return new FoldingDescriptor(
                startComment.getParent().getNode(),
                range,
                null,
                Collections.emptySet(),
                false,
                startComment.getText().trim() + " {...}",
                true
        );
    }
}
