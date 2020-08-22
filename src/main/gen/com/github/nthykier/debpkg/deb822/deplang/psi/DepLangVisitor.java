// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.deplang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.github.nthykier.debpkg.deb822.psi.Deb822SubstvarBase;

public class DepLangVisitor extends PsiElementVisitor {

  public void visitAndDependencyClause(@NotNull DepLangAndDependencyClause o) {
    visitPsiElement(o);
  }

  public void visitArchRestrictionPart(@NotNull DepLangArchRestrictionPart o) {
    visitPsiElement(o);
  }

  public void visitBuildProfileRestrictionPart(@NotNull DepLangBuildProfileRestrictionPart o) {
    visitPsiElement(o);
  }

  public void visitDependency(@NotNull DepLangDependency o) {
    visitPsiElement(o);
  }

  public void visitDependencyInfo(@NotNull DepLangDependencyInfo o) {
    visitPsiElement(o);
  }

  public void visitLanguageDefinition(@NotNull DepLangLanguageDefinition o) {
    visitPsiElement(o);
  }

  public void visitOrDependencyClause(@NotNull DepLangOrDependencyClause o) {
    visitPsiElement(o);
  }

  public void visitPackageName(@NotNull DepLangPackageName o) {
    visitPsiElement(o);
  }

  public void visitRestrictionList(@NotNull DepLangRestrictionList o) {
    visitPsiElement(o);
  }

  public void visitSubstvar(@NotNull DepLangSubstvar o) {
    visitDeb822SubstvarBase(o);
  }

  public void visitVersion(@NotNull DepLangVersion o) {
    visitPsiElement(o);
  }

  public void visitVersionOperator(@NotNull DepLangVersionOperator o) {
    visitPsiElement(o);
  }

  public void visitVersionPart(@NotNull DepLangVersionPart o) {
    visitPsiElement(o);
  }

  public void visitDeb822SubstvarBase(@NotNull Deb822SubstvarBase o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
