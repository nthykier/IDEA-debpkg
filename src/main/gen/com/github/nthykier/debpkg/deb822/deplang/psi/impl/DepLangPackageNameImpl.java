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
import com.intellij.psi.PsiReference;

public class DepLangPackageNameImpl extends ASTWrapperPsiElement implements DepLangPackageName {

  public DepLangPackageNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DepLangVisitor visitor) {
    visitor.visitPackageName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DepLangVisitor) accept((DepLangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DepLangSubstvar getSubstvar() {
    return findChildByClass(DepLangSubstvar.class);
  }

  @Override
  public @Nullable PsiReference getReference() {
    return Deb822PsiImplUtil.getReference(this);
  }

}
