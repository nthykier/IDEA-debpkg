package com.github.nthykier.debpkg.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.impl.source.tree.TreeElementVisitor;
import org.jetbrains.annotations.NotNull;

public class ASTNodeStringConverter {

    private StringBuilder stringBuilder;
    private final TreeElementVisitor treeElementVisitor = new StringConvertingTreeElementVisitor();

    public ASTNodeStringConverter() {
        this(new StringBuilder());
    }

    public ASTNodeStringConverter(StringBuilder builder) {
        this.stringBuilder = builder;
    }

    public @NotNull StringBuilder getStringBuilder() {
        return this.stringBuilder;
    }

    public void setStringBuilder(@NotNull StringBuilder builder) {
        this.stringBuilder = builder;
    }

    public ASTNodeStringConverter readTextFromNode(ASTNode node) {
        if (node instanceof TreeElement) {
            ((TreeElement)node).acceptTree(treeElementVisitor);
        } else {
            stringBuilder.append(node.getText());
        }
        return this;
    }

    private class StringConvertingTreeElementVisitor extends TreeElementVisitor {

        public void visitComposite(CompositeElement compositeElement){
            TreeElement e = compositeElement.getFirstChildNode();
            while (e != null) {
                /* DFS */
                e.acceptTree(this);
                e = e.getTreeNext();
            }
        }

        public void visitLeaf(LeafElement leaf) {
            stringBuilder.append(leaf.getChars());
        }
    }
}
