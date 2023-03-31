// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.aptlist.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.nthykier.debpkg.aptlist.psi.impl.*;

public interface AptListTypes {

  IElementType APT_SOURCES_LINE = new AptListElementType("APT_SOURCES_LINE");
  IElementType COMPONENT = new AptListElementType("COMPONENT");
  IElementType OPTION = new AptListElementType("OPTION");
  IElementType OPTION_KEY = new AptListElementType("OPTION_KEY");
  IElementType OPTION_VALUE = new AptListElementType("OPTION_VALUE");
  IElementType SUITE = new AptListElementType("SUITE");
  IElementType TYPE = new AptListElementType("TYPE");
  IElementType URI = new AptListElementType("URI");

  IElementType BRACKET_CLOSE = new AptListTokenType("]");
  IElementType BRACKET_OPEN = new AptListTokenType("[");
  IElementType COMMA_TOKEN = new AptListTokenType(",");
  IElementType COMMENT = new AptListTokenType("^[#][^\\n]+\\n");
  IElementType EQUALS_TOKEN = new AptListTokenType("=");
  IElementType NAME_TOKEN = new AptListTokenType("NAME_TOKEN");
  IElementType NEWLINE_TOKEN = new AptListTokenType("\\n");
  IElementType OPTION_KEY_TOKEN = new AptListTokenType("OPTION_KEY_TOKEN");
  IElementType OPTION_VALUE_TOKEN = new AptListTokenType("OPTION_VALUE_TOKEN");
  IElementType TYPE_TOKEN = new AptListTokenType("TYPE_TOKEN");
  IElementType URI_TOKEN = new AptListTokenType("URI_TOKEN");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == APT_SOURCES_LINE) {
        return new AptListAptSourcesLineImpl(node);
      }
      else if (type == COMPONENT) {
        return new AptListComponentImpl(node);
      }
      else if (type == OPTION) {
        return new AptListOptionImpl(node);
      }
      else if (type == OPTION_KEY) {
        return new AptListOptionKeyImpl(node);
      }
      else if (type == OPTION_VALUE) {
        return new AptListOptionValueImpl(node);
      }
      else if (type == SUITE) {
        return new AptListSuiteImpl(node);
      }
      else if (type == TYPE) {
        return new AptListTypeImpl(node);
      }
      else if (type == URI) {
        return new AptListUriImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
