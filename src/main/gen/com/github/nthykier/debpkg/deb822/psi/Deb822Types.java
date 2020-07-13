// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.nthykier.debpkg.deb822.psi.impl.*;

public interface Deb822Types {

  IElementType ALL_PARAGRAPHS = new Deb822ElementType("ALL_PARAGRAPHS");
  IElementType FIELD = new Deb822ElementType("FIELD");
  IElementType FIELD_VALUE_PAIR = new Deb822ElementType("FIELD_VALUE_PAIR");
  IElementType PARAGRAPH = new Deb822ElementType("PARAGRAPH");
  IElementType VALUE_PARTS = new Deb822ElementType("VALUE_PARTS");

  IElementType COMMENT = new Deb822TokenType("COMMENT");
  IElementType FIELD_NAME = new Deb822TokenType("FIELD_NAME");
  IElementType PARAGRAPH_FINISH = new Deb822TokenType("PARAGRAPH_FINISH");
  IElementType SEPARATOR = new Deb822TokenType("SEPARATOR");
  IElementType SUBSTVAR = new Deb822TokenType("SUBSTVAR");
  IElementType VALUE = new Deb822TokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ALL_PARAGRAPHS) {
        return new Deb822AllParagraphsImpl(node);
      }
      else if (type == FIELD) {
        return new Deb822FieldImpl(node);
      }
      else if (type == FIELD_VALUE_PAIR) {
        return new Deb822FieldValuePairImpl(node);
      }
      else if (type == PARAGRAPH) {
        return new Deb822ParagraphImpl(node);
      }
      else if (type == VALUE_PARTS) {
        return new Deb822ValuePartsImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
