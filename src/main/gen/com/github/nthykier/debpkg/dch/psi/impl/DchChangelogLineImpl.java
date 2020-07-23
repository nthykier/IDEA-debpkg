// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.dch.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.github.nthykier.debpkg.dch.psi.*;

public class DchChangelogLineImpl extends AbstractDchChangelogEntry implements DchChangelogLine {

  public DchChangelogLineImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DchVisitor visitor) {
    visitor.visitChangelogLine(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DchVisitor) accept((DchVisitor)visitor);
    else super.accept(visitor);
  }

}
