// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.nthykier.debpkg.deb822.psi.Deb822Types.*;
import com.github.nthykier.debpkg.deb822.psi.*;

public class Deb822FieldImpl extends AbstractDeb822Field implements Deb822Field {

  public Deb822FieldImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Deb822Visitor visitor) {
    visitor.visitField(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Deb822Visitor) accept((Deb822Visitor)visitor);
    else super.accept(visitor);
  }

}
