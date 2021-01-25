package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDeb822Paragraph extends ASTWrapperPsiElement implements Deb822Paragraph {

  private Map<String, Deb822FieldValuePair> fieldMap;
  private Map<String, Deb822FieldValuePair> xPrefixedFieldMap;

  public AbstractDeb822Paragraph(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void subtreeChanged() {
    super.subtreeChanged();
    this.fieldMap = null;
    this.xPrefixedFieldMap = null;
  }

  private Map<String, Deb822FieldValuePair> getOrLoadFieldMap() {
    if (this.fieldMap == null) {
      Map<String, Deb822FieldValuePair> map = new HashMap<>();
      Map<String, Deb822FieldValuePair> prefixStrippedMap = new HashMap<>();
      for (Deb822FieldValuePair valuePair : this.getFieldValuePairList()) {
        String fieldName = valuePair.getField().getFieldName().toLowerCase();
        String xStrippedName = KnownFieldTable.withXPrefixStripped(fieldName);
        /* if there are duplicate fields (which is not permitted but can happen), we pick the first */
        map.putIfAbsent(fieldName, valuePair);
        if (xStrippedName != null) {
          prefixStrippedMap.putIfAbsent(xStrippedName, valuePair);
        }
      }
      this.fieldMap = Collections.unmodifiableMap(map);
      this.xPrefixedFieldMap = prefixStrippedMap.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(prefixStrippedMap);
    }
    return this.fieldMap;
  }

  @NotNull
  public Map<String, Deb822FieldValuePair> getFieldMap() {
    return getOrLoadFieldMap();
  }

  @Nullable
  public Deb822FieldValuePair getFieldValuePair(@NotNull String fieldName) {
    String fieldNameLC = fieldName.toLowerCase();
    Deb822FieldValuePair fieldValuePair = getFieldMap().get(fieldNameLC);
    if (fieldValuePair == null) {
      KnownFieldTable fieldTable = Deb822LanguageSupport.fromPsiElement(this).getKnownFieldTable();
      if (fieldTable.getAutoStripXPrefix()) {
        // Support looking up "X-Foo" and finding "XB-Foo" (etc.)
        String stripped = KnownFieldTable.withXPrefixStripped(fieldName);
        if (stripped == null) {
           stripped = fieldNameLC;
        }
        fieldValuePair = xPrefixedFieldMap.get(stripped);
      }
    }
    return fieldValuePair;
  }


  public boolean isFirstParagraph() {
    Deb822Paragraph prevSibling = Deb822PsiImplUtil.getPreviousSiblingOfType(this.getPrevSibling(), Deb822Paragraph.class);
    return prevSibling == null;
  }
}
