// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.aptlist.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.nthykier.debpkg.aptlist.psi.AptListTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.nthykier.debpkg.aptlist.psi.*;

public class AptListAptSourcesLineImpl extends ASTWrapperPsiElement implements AptListAptSourcesLine {

  public AptListAptSourcesLineImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AptListVisitor visitor) {
    visitor.visitAptSourcesLine(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AptListVisitor) accept((AptListVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AptListComponent> getComponentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AptListComponent.class);
  }

  @Override
  @NotNull
  public List<AptListOption> getOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AptListOption.class);
  }

  @Override
  @NotNull
  public AptListSuite getSuite() {
    return findNotNullChildByClass(AptListSuite.class);
  }

  @Override
  @NotNull
  public AptListType getType() {
    return findNotNullChildByClass(AptListType.class);
  }

  @Override
  @NotNull
  public AptListUri getUri() {
    return findNotNullChildByClass(AptListUri.class);
  }

}
