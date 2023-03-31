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

public class AptListOptionValueImpl extends ASTWrapperPsiElement implements AptListOptionValue {

  public AptListOptionValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AptListVisitor visitor) {
    visitor.visitOptionValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AptListVisitor) accept((AptListVisitor)visitor);
    else super.accept(visitor);
  }

}
