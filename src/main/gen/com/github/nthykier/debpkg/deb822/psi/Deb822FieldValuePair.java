// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ContributedReferenceHost;
import com.intellij.psi.PsiReference;

public interface Deb822FieldValuePair extends ContributedReferenceHost {

  @NotNull
  Deb822Field getField();

  @Nullable
  Deb822ValueParts getValueParts();

  @Nullable PsiReference @NotNull [] getReferences();

}
