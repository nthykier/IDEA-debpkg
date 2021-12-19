// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.dch.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.nthykier.debpkg.dch.psi.DchTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.nthykier.debpkg.dch.psi.*;
import com.intellij.psi.PsiReference;

public class DchSourceImpl extends ASTWrapperPsiElement implements DchSource {

  public DchSourceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DchVisitor visitor) {
    visitor.visitSource(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DchVisitor) accept((DchVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public PsiReference getReference() {
    return DchPsiImplUtil.getReference(this);
  }

}
