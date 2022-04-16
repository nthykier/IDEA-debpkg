// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.dch.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DchVersionLine extends PsiElement {

  @NotNull
  List<DchDistribution> getDistributionList();

  @NotNull
  DchSource getSource();

  @Nullable
  DchVersion getVersion();

}
