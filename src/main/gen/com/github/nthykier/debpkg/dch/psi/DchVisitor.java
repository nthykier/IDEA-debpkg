// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.dch.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class DchVisitor extends PsiElementVisitor {

  public void visitChangeDescription(@NotNull DchChangeDescription o) {
    visitPsiElement(o);
  }

  public void visitChangelogEntry(@NotNull DchChangelogEntry o) {
    visitPsiElement(o);
  }

  public void visitChangelogLine(@NotNull DchChangelogLine o) {
    visitPsiElement(o);
  }

  public void visitSignoff(@NotNull DchSignoff o) {
    visitPsiElement(o);
  }

  public void visitVersionLine(@NotNull DchVersionLine o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
