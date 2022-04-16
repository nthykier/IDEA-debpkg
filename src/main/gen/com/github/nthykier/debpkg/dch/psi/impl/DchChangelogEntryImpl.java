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
import com.intellij.navigation.ItemPresentation;

public class DchChangelogEntryImpl extends ASTWrapperPsiElement implements DchChangelogEntry {

  public DchChangelogEntryImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DchVisitor visitor) {
    visitor.visitChangelogEntry(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DchVisitor) accept((DchVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DchChangelogLine> getChangelogLineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DchChangelogLine.class);
  }

  @Override
  @NotNull
  public DchSignoff getSignoff() {
    return findNotNullChildByClass(DchSignoff.class);
  }

  @Override
  @NotNull
  public DchVersionLine getVersionLine() {
    return findNotNullChildByClass(DchVersionLine.class);
  }

  @Override
  public boolean isFirstChangelogEntry() {
    return DchPsiImplUtil.isFirstChangelogEntry(this);
  }

  @Override
  public boolean isUnreleasedEntry() {
    return DchPsiImplUtil.isUnreleasedEntry(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    return DchPsiImplUtil.getPresentation(this);
  }

  @Override
  public DchChangelogEntry getFirstEntry() {
    return DchPsiImplUtil.getFirstEntry(this);
  }

}
