// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.nthykier.debpkg.deb822.psi.Deb822Types.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.intellij.psi.PsiReference;

public class Deb822FieldValuePairImpl extends ASTWrapperPsiElement implements Deb822FieldValuePair {

  public Deb822FieldValuePairImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Deb822Visitor visitor) {
    visitor.visitFieldValuePair(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Deb822Visitor) accept((Deb822Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public Deb822Field getField() {
    return findNotNullChildByClass(Deb822Field.class);
  }

  @Override
  @Nullable
  public Deb822ValueParts getValueParts() {
    return findChildByClass(Deb822ValueParts.class);
  }

  @Override
  public @NotNull PsiReference @NotNull [] getReferences() {
    return Deb822PsiImplUtil.getReferences(this);
  }

}
