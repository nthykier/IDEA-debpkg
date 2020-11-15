package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.impl.source.tree.TreeElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Deb822FoldingBuilder extends FoldingBuilderEx implements DumbAware {

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        Collection<Deb822FieldValuePair> fieldValuePairs =
                PsiTreeUtil.findChildrenOfType(root, Deb822FieldValuePair.class);

        Collection<Deb822GpgSignature> signatures = PsiTreeUtil.findChildrenOfType(root, Deb822GpgSignature.class);
        Collection<Deb822GpgSigned> gpgSignedStatements = PsiTreeUtil.findChildrenOfType(root, Deb822GpgSigned.class);

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
            String placeholderText = null;
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
            if (foldedByDefault || !quick) {
                placeholderText = getPlaceholderText(node);
            }
            firstElementAfter = Deb822PsiImplUtil.getNextSiblingMatchingCondition(
                    fieldValuePair.getNextSibling(),
                    e -> !(e instanceof PsiWhiteSpace)
            );

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
        return descriptors.toArray(FoldingDescriptor.EMPTY);
    }

    /**
     * @param node  Node corresponding to PsiLiteralExpression containing a string in the format
     *              SIMPLE_PREFIX_STR + SIMPLE_SEPARATOR_STR + Key, where Key is
     *              defined by the Simple language file.
     */
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
            TreeElementVisitor visitor = buildVisitor(placeholderBuilder);
            ASTNode partsNode = node.findChildByType(Deb822Types.VALUE_PARTS);
            ASTNode currentChild;
            ASTNode nextChild = partsNode != null ? partsNode.getFirstChildNode() : null;
            final int initialSize = placeholderBuilder.length();

            while (nextChild != null) {
                currentChild = nextChild;
                nextChild = nextChild.getTreeNext();
                if (placeholderBuilder.length() == initialSize) {
                    if (currentChild.getElementType() != TokenType.WHITE_SPACE) {
                        addText(placeholderBuilder, visitor, currentChild);
                    }
                } else if (currentChild.textContains('\n')) {
                    /* We add an ellipsis when there is (or might be) some hidden text */
                    placeholderBuilder.append(" {...}");
                    break;
                } else {
                    addText(placeholderBuilder, visitor, currentChild);
                }
            }

            retTxt = placeholderBuilder.toString();
        }
        return retTxt;
    }

    private void addText(StringBuilder b, TreeElementVisitor visitor, ASTNode node) {
        if (node instanceof TreeElement) {
            ((TreeElement)node).acceptTree(visitor);
        } else {
            b.append(node.getText());
        }
    }

    private static TreeElementVisitor buildVisitor(final StringBuilder b) {
        return new TreeElementVisitor(){
            public void visitComposite(CompositeElement compositeElement){
                TreeElement e = compositeElement.getFirstChildNode();
                while (e != null) {
                    /* DFS */
                    e.acceptTree(this);
                    e = e.getTreeNext();
                }
            }
            public void visitLeaf(LeafElement leaf) {
                b.append(leaf.getChars());
            }
        };
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        /* We setup "Collapsed by default" in the FoldingDescriptors */
        return false;
    }
}
