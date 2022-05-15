// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface Deb822BuildProfileGroup extends PsiElement {

  @NotNull
  List<Deb822BuildProfile> getBuildProfileList();

  @NotNull
  List<Deb822HangingContValue> getHangingContValueList();

}
