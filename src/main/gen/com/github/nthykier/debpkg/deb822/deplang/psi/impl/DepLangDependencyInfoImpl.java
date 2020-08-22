// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.deplang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.nthykier.debpkg.deb822.deplang.psi.DependencyLanguageTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.nthykier.debpkg.deb822.deplang.psi.*;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;

public class DepLangDependencyInfoImpl extends ASTWrapperPsiElement implements DepLangDependencyInfo {

  public DepLangDependencyInfoImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DepLangVisitor visitor) {
    visitor.visitDependencyInfo(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DepLangVisitor) accept((DepLangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DepLangAndDependencyClause> getAndDependencyClauseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DepLangAndDependencyClause.class);
  }

  @Override
  @Nullable
  public DepLangLanguageDefinition getLanguageDefinition() {
    return findChildByClass(DepLangLanguageDefinition.class);
  }

}
