// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.deplang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DepLangDependency extends PsiElement {

  @NotNull
  DepLangPackageName getPackageName();

  @NotNull
  List<DepLangRestrictionList> getRestrictionListList();

  @Nullable
  DepLangVersionPart getVersionPart();

}
