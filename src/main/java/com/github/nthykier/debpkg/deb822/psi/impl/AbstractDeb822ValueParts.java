package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.psi.Deb822ElementFactory;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822ValueParts;
import com.github.nthykier.debpkg.util.ASTNodeStringConverter;
import com.github.nthykier.debpkg.util.CommonPsiUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.CheckUtil;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AbstractDeb822ValueParts extends ASTWrapperPsiElement implements PsiLanguageInjectionHost {

    private final LiteralTextEscaper<AbstractDeb822ValueParts> escaper = new PassThroughLiteralTextEscaper<>(this);
    private String cachedText;
    private PsiReference[] internalReferences;

    public AbstractDeb822ValueParts(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        this.cachedText = null;
        this.internalReferences = null;
    }

    @Override
    public String getText() {
        if (cachedText == null) {
            cachedText = ASTNodeStringConverter.extractString(this.getNode());
        }
        return cachedText;
    }

    @Override
    public PsiReference getReference() {
        PsiReference[] references = this.getReferences();
        return references.length > 0 ? references[0] : null;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        if (internalReferences == null) {
            List<PsiReference> references = new SmartList<>();

            CommonPsiUtil.addURLReferences(this, getText(), references);

            if (references.isEmpty()) {
                this.internalReferences = PsiReference.EMPTY_ARRAY;
            } else {
                this.internalReferences = references.toArray(PsiReference.EMPTY_ARRAY);
            }
        }
        return internalReferences;
    }

    @Override
    public boolean isValidHost() {
        Deb822FieldValuePair pair = Deb822PsiImplUtil.getAncestorOfType(this, Deb822FieldValuePair.class);
        return pair != null;
    }

    @Override
    public PsiLanguageInjectionHost updateText(@NotNull String text) {
        Deb822ValueParts valueParts = Deb822ElementFactory.createValuePartsFromText(this.getProject(), "Foo: " + text);
        CheckUtil.checkWritable(this);
        this.getNode().replaceAllChildrenToChildrenOf(valueParts.getNode());
        return this;
    }

    @Override
    public @NotNull LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
        return this.escaper;
    }

    private static class PassThroughLiteralTextEscaper<T extends PsiLanguageInjectionHost> extends LiteralTextEscaper<T> {
        public PassThroughLiteralTextEscaper(@NotNull T host) {
            super(host);
        }

        @Override
        public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
            outChars.append(rangeInsideHost.substring(myHost.getText()));
            return true;
        }

        @Override
        public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
            return rangeInsideHost.getStartOffset() + offsetInDecoded;
        }

        @Override
        public boolean isOneLine() {
            return false;
        }
    }
}
