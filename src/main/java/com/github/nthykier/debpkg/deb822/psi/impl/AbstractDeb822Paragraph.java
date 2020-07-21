// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDeb822Paragraph extends ASTWrapperPsiElement implements Deb822Paragraph {

  private Map<String, Deb822FieldValuePair> fieldMap;

  public AbstractDeb822Paragraph(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void subtreeChanged() {
    super.subtreeChanged();
    this.fieldMap = null;
  }

  private Map<String, Deb822FieldValuePair> getOrLoadFieldMap() {
    if (this.fieldMap == null) {
      Map<String, Deb822FieldValuePair> map = new HashMap<>();
      for (Deb822FieldValuePair valuePair : this.getFieldValuePairList()) {
        String fieldName = valuePair.getField().getText().trim().toLowerCase();
        /* if there are duplicate fields (which is not permitted but can happen), we pick the first */
        map.putIfAbsent(fieldName, valuePair);
      }
      this.fieldMap = Collections.unmodifiableMap(map);
    }
    return this.fieldMap;
  }

  @NotNull
  public Map<String, Deb822FieldValuePair> getFieldMap() {
    return getOrLoadFieldMap();
  }

}
