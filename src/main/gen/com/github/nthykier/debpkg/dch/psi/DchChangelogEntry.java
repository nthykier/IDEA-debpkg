// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.dch.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.navigation.ItemPresentation;

public interface DchChangelogEntry extends NavigatablePsiElement {

  @NotNull
  List<DchChangelogLine> getChangelogLineList();

  @NotNull
  DchSignoff getSignoff();

  @NotNull
  DchVersionLine getVersionLine();

  boolean isFirstChangelogEntry();

  boolean isUnreleasedEntry();

  ItemPresentation getPresentation();

  DchChangelogEntry getFirstEntry();

}
