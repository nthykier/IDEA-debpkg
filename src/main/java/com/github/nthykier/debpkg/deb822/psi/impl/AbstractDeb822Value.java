package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.github.nthykier.debpkg.deb822.psi.Deb822Value;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class AbstractDeb822Value extends ASTWrapperPsiElement implements Deb822Value {

    private static final TokenSet VALUE_TOKEN_SET = TokenSet.create(Deb822Types.VALUE_TOKEN);

    private String cachedText;
    private PsiReference[] internalReferences;

    public AbstractDeb822Value(@NotNull ASTNode node) {
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
            cachedText = Deb822PsiImplUtil.getTextFromCompositeWrappingAToken(this, VALUE_TOKEN_SET);
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
            PsiReference reference = Deb822PsiImplUtil.getReference(this);
            if (reference != null) {
                references.add(reference);
            }

            //Deb822LanguageSupport.fromPsiElement(this).addReferences(this, references);
            //CommonPsiUtil.addURLReferences(this, getText(), references);
            references.addAll(Arrays.asList(ReferenceProvidersRegistry.getReferencesFromProviders(this)));
            if (references.isEmpty()) {
                this.internalReferences = PsiReference.EMPTY_ARRAY;
            } else {
                this.internalReferences = references.toArray(PsiReference.EMPTY_ARRAY);
            }
        }
        return internalReferences;
    }


}
