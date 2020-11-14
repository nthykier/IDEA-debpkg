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
  IElementType GPG_SIGNATURE = new Deb822ElementType("GPG_SIGNATURE");
  IElementType GPG_SIGNED = new Deb822ElementType("GPG_SIGNED");
  IElementType PARAGRAPH = new Deb822ElementType("PARAGRAPH");
  IElementType SUBSTVAR = new Deb822ElementType("SUBSTVAR");
  IElementType VALUE = new Deb822ElementType("VALUE");
  IElementType VALUE_PARTS = new Deb822ElementType("VALUE_PARTS");

  IElementType COMMA = new Deb822TokenType("COMMA");
  IElementType COMMENT = new Deb822TokenType("COMMENT");
  IElementType FIELD_NAME = new Deb822TokenType("FIELD_NAME");
  IElementType GPG_ARMOR_HEADER = new Deb822TokenType("GPG_ARMOR_HEADER");
  IElementType GPG_ARMOR_HEADERS_END = new Deb822TokenType("GPG_ARMOR_HEADERS_END");
  IElementType GPG_BEGIN_SIGNATURE = new Deb822TokenType("GPG_BEGIN_SIGNATURE");
  IElementType GPG_BEGIN_SIGNED_MESSAGE = new Deb822TokenType("GPG_BEGIN_SIGNED_MESSAGE");
  IElementType GPG_END_SIGNATURE = new Deb822TokenType("GPG_END_SIGNATURE");
  IElementType GPG_SIGNATURE_BLOB_PART = new Deb822TokenType("GPG_SIGNATURE_BLOB_PART");
  IElementType PARAGRAPH_FINISH = new Deb822TokenType("PARAGRAPH_FINISH");
  IElementType SEPARATOR = new Deb822TokenType("SEPARATOR");
  IElementType SUBSTVAR_TOKEN = new Deb822TokenType("SUBSTVAR_TOKEN");
  IElementType VALUE_TOKEN = new Deb822TokenType("VALUE_TOKEN");

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
      else if (type == GPG_SIGNATURE) {
        return new Deb822GpgSignatureImpl(node);
      }
      else if (type == GPG_SIGNED) {
        return new Deb822GpgSignedImpl(node);
      }
      else if (type == PARAGRAPH) {
        return new Deb822ParagraphImpl(node);
      }
      else if (type == SUBSTVAR) {
        return new Deb822SubstvarImpl(node);
      }
      else if (type == VALUE) {
        return new Deb822ValueImpl(node);
      }
      else if (type == VALUE_PARTS) {
        return new Deb822ValuePartsImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
