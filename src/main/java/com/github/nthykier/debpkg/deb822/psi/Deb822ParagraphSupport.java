package com.github.nthykier.debpkg.deb822.psi;

import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.Deb822ParagraphClassifier;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Deb822ParagraphSupport extends PsiElement, PsiNameIdentifierOwner {

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
   * This method will also handle stripping of "X-" (and similar prefixes) of the field name
   * if this is a debian/control file.
   *
   * @param fieldName Name of the field to look up.  The method handles lower-casing as necessary.
   * @return The Deb822FieldValuePair with the field of this name.
   */
  @Nullable
  Deb822FieldValuePair getFieldValuePair(@NotNull String fieldName);

  /**
   * Convenient way of getting the value of a field by name.
   *
   * In case of errors, where the paragraph have duplicate fields (which is not permitted
   * in deb822 but the parser survives it), the first field is used.
   *
   * If the field is absent, null is returned.
   *
   * This method will also handle stripping of "X-" (and similar prefixes) of the field name
   * if this is a debian/control file.
   *
   * @param fieldName Name of the field to look up.  The method handles lower-casing as necessary.
   * @return The value of the field (or null if the field is absent).
   */
  @Nullable
  default String getFieldValue(@NotNull String fieldName) {
    Deb822FieldValuePair pair = getFieldValuePair(fieldName);
    if (pair == null) {
      return null;
    }
    return pair.getFieldValue();
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
   * @return A language specific classification of the paragraph or {@link Deb822ParagraphClassifier#UNCLASSIFIED)
   */
  @NotNull
  default String classifyParagraph() {
    return Deb822LanguageSupport.fromDeb822Language(getContainingFile().getLanguage()).getParagraphClassifier().classifyParagraph(this);
  }
}
