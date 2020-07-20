// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Deb822ParagraphSupport extends PsiElement {

  @NotNull
  Map<String, Deb822FieldValuePair> getFieldMap();

  @Nullable
  default Deb822FieldValuePair getFieldValuePair(@NotNull String fieldName) {
     return getFieldMap().get(fieldName.toLowerCase());
  }

}
