// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.dch.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.nthykier.debpkg.dch.psi.impl.*;

public interface DchTypes {

  IElementType CHANGELOG_ENTRY = new DchElementType("CHANGELOG_ENTRY");
  IElementType CHANGELOG_LINE = new DchElementType("CHANGELOG_LINE");
  IElementType SIGNOFF = new DchElementType("SIGNOFF");
  IElementType VERSION_LINE = new DchElementType("VERSION_LINE");

  IElementType CHANGE_DETAILS = new DchTokenType("CHANGE_DETAILS");
  IElementType CHANGE_RESPONSIBLE = new DchTokenType("CHANGE_RESPONSIBLE");
  IElementType DISTRIBUTION_NAME = new DchTokenType("DISTRIBUTION_NAME");
  IElementType DOUBLE_SPACE = new DchTokenType("DOUBLE_SPACE");
  IElementType GREATER_THAN = new DchTokenType("GREATER_THAN");
  IElementType KEYVALUE_PAIR = new DchTokenType("KEYVALUE_PAIR");
  IElementType LESS_THAN = new DchTokenType("LESS_THAN");
  IElementType MAINTAINER_EMAIL = new DchTokenType("MAINTAINER_EMAIL");
  IElementType MAINTAINER_NAME = new DchTokenType("MAINTAINER_NAME");
  IElementType PARANTHESES_CLOSE = new DchTokenType("PARANTHESES_CLOSE");
  IElementType PARANTHESES_OPEN = new DchTokenType("PARANTHESES_OPEN");
  IElementType SEMI_COLON = new DchTokenType("SEMI_COLON");
  IElementType SIGNOFF_DATE = new DchTokenType("SIGNOFF_DATE");
  IElementType SIGNOFF_STARTER = new DchTokenType("SIGNOFF_STARTER");
  IElementType SOURCE_NAME = new DchTokenType("SOURCE_NAME");
  IElementType VERSION = new DchTokenType("VERSION");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == CHANGELOG_ENTRY) {
        return new DchChangelogEntryImpl(node);
      }
      else if (type == CHANGELOG_LINE) {
        return new DchChangelogLineImpl(node);
      }
      else if (type == SIGNOFF) {
        return new DchSignoffImpl(node);
      }
      else if (type == VERSION_LINE) {
        return new DchVersionLineImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
