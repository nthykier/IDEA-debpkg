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

public class DchVersionLineImpl extends ASTWrapperPsiElement implements DchVersionLine {

  public DchVersionLineImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DchVisitor visitor) {
    visitor.visitVersionLine(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DchVisitor) accept((DchVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DchDistribution> getDistributionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DchDistribution.class);
  }

  @Override
  @NotNull
  public DchSource getSource() {
    return findNotNullChildByClass(DchSource.class);
  }

  @Override
  @Nullable
  public DchVersion getVersion() {
    return findChildByClass(DchVersion.class);
  }

}
