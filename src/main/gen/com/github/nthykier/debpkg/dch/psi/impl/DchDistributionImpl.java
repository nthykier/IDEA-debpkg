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

public class DchDistributionImpl extends ASTWrapperPsiElement implements DchDistribution {

  public DchDistributionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DchVisitor visitor) {
    visitor.visitDistribution(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DchVisitor) accept((DchVisitor)visitor);
    else super.accept(visitor);
  }

}
