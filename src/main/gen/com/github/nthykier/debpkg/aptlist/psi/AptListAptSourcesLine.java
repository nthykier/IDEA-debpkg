// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.aptlist.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AptListAptSourcesLine extends PsiElement {

  @NotNull
  List<AptListComponent> getComponentList();

  @NotNull
  List<AptListOption> getOptionList();

  @NotNull
  AptListSuite getSuite();

  @NotNull
  AptListType getType();

  @NotNull
  AptListUri getUri();

}
