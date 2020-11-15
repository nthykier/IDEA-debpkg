package com.github.nthykier.debpkg.deb822.psi;

import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.Deb822ParagraphClassifier;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Deb822ParagraphSupport extends PsiElement {

  /**
   * Convenient and cache-enabled way of getting the key-value pairs in a paragraph.
   *
   * In case of errors, where the paragraph have duplicate fields (which is not permitted
   * in deb822 but the parser survives it), the first field is used.
   *
   * @return A Map of the fields and their values.  All keys use lower-case.
   */
  @NotNull
  Map<String, Deb822FieldValuePair> getFieldMap();

  /**
   * Convenient way of getting a key-value pair by field name.
   *
   * In case of errors, where the paragraph have duplicate fields (which is not permitted
   * in deb822 but the parser survives it), the first field is used.
   *
   * @param fieldName Name of the field to look up.  The method handles lower-casing as necessary.
   * @return The Deb822FieldValuePair with the field of this name.
   */
  @Nullable
  default Deb822FieldValuePair getFieldValuePair(@NotNull String fieldName) {
     return getFieldMap().get(fieldName.toLowerCase());
  }

  /**
   * Check if the paragraph is the first in the file
   *
   * @return true if this paragraph is the first paragraph in the file.
   */
  boolean isFirstParagraph();

  /**
   * Classify the paragraph according to the Deb822 language variant
   *
   * @return A language specific classification of the paragraph or Deb822ParagraphClassifier.UNCLASSIFIED
   */
  @NotNull
  default String classifyParagraph() {
    Language language = getContainingFile().getLanguage();
    if (language instanceof Deb822LanguageSupport) {
      return ((Deb822LanguageSupport) language).getParagraphClassifier().classifyParagraph(this);
    }
    return Deb822ParagraphClassifier.UNCLASSIFIED;
  }
}
