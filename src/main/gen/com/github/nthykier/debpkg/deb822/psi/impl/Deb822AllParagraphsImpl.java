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

public class Deb822AllParagraphsImpl extends ASTWrapperPsiElement implements Deb822AllParagraphs {

  public Deb822AllParagraphsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Deb822Visitor visitor) {
    visitor.visitAllParagraphs(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Deb822Visitor) accept((Deb822Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<Deb822Paragraph> getParagraphList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Deb822Paragraph.class);
  }

}
