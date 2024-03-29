// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.dch.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.HintedReferenceHost;
import com.intellij.psi.NavigatablePsiElement;

public class DchVisitor extends PsiElementVisitor {

  public void visitChangeDescription(@NotNull DchChangeDescription o) {
    visitHintedReferenceHost(o);
  }

  public void visitChangelogEntry(@NotNull DchChangelogEntry o) {
    visitNavigatablePsiElement(o);
  }

  public void visitChangelogLine(@NotNull DchChangelogLine o) {
    visitPsiElement(o);
  }

  public void visitDistribution(@NotNull DchDistribution o) {
    visitPsiElement(o);
  }

  public void visitSignoff(@NotNull DchSignoff o) {
    visitPsiElement(o);
  }

  public void visitSignoffDate(@NotNull DchSignoffDate o) {
    visitSignoffDateSupport(o);
  }

  public void visitSource(@NotNull DchSource o) {
    visitPsiElement(o);
  }

  public void visitVersion(@NotNull DchVersion o) {
    visitPsiElement(o);
  }

  public void visitVersionLine(@NotNull DchVersionLine o) {
    visitPsiElement(o);
  }

  public void visitSignoffDateSupport(@NotNull DchSignoffDateSupport o) {
    visitPsiElement(o);
  }

  public void visitHintedReferenceHost(@NotNull HintedReferenceHost o) {
    visitElement(o);
  }

  public void visitNavigatablePsiElement(@NotNull NavigatablePsiElement o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
