// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.deplang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.nthykier.debpkg.deb822.deplang.psi.impl.*;

public interface DependencyLanguageTypes {

  IElementType AND_DEPENDENCY_CLAUSE = new DepLangElementType("AND_DEPENDENCY_CLAUSE");
  IElementType DEPENDENCY = new DepLangElementType("DEPENDENCY");
  IElementType OR_DEPENDENCY_CLAUSE = new DepLangElementType("OR_DEPENDENCY_CLAUSE");
  IElementType PACKAGE_NAME = new DepLangElementType("PACKAGE_NAME");
  IElementType RESTRICTION_LIST = new DepLangElementType("RESTRICTION_LIST");
  IElementType SUBSTVAR = new DepLangElementType("SUBSTVAR");
  IElementType VERSION = new DepLangElementType("VERSION");
  IElementType VERSION_OPERATOR = new DepLangElementType("VERSION_OPERATOR");
  IElementType VERSION_PART = new DepLangElementType("VERSION_PART");

  IElementType BRACKETS_CLOSE = new DepLangTokenType("]");
  IElementType BRACKETS_OPEN = new DepLangTokenType("[");
  IElementType COMMENT = new DepLangTokenType("COMMENT");
  IElementType GREATER_THAN = new DepLangTokenType(">");
  IElementType LESS_THAN = new DepLangTokenType("<");
  IElementType MISPLACED_COMMENT = new DepLangTokenType("MISPLACED_COMMENT");
  IElementType NEGATION = new DepLangTokenType("!");
  IElementType OPERATOR_AND = new DepLangTokenType(",");
  IElementType OPERATOR_OR = new DepLangTokenType("|");
  IElementType PARANTHESES_CLOSE = new DepLangTokenType(")");
  IElementType PARANTHESES_OPEN = new DepLangTokenType("(");
  IElementType SUBSTVAR_TOKEN = new DepLangTokenType("SUBSTVAR_TOKEN");
  IElementType VERSION_OP = new DepLangTokenType("VERSION_OP");
  IElementType WORDISH = new DepLangTokenType("WORDISH");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == AND_DEPENDENCY_CLAUSE) {
        return new DepLangAndDependencyClauseImpl(node);
      }
      else if (type == DEPENDENCY) {
        return new DepLangDependencyImpl(node);
      }
      else if (type == OR_DEPENDENCY_CLAUSE) {
        return new DepLangOrDependencyClauseImpl(node);
      }
      else if (type == PACKAGE_NAME) {
        return new DepLangPackageNameImpl(node);
      }
      else if (type == RESTRICTION_LIST) {
        return new DepLangRestrictionListImpl(node);
      }
      else if (type == SUBSTVAR) {
        return new DepLangSubstvarImpl(node);
      }
      else if (type == VERSION) {
        return new DepLangVersionImpl(node);
      }
      else if (type == VERSION_OPERATOR) {
        return new DepLangVersionOperatorImpl(node);
      }
      else if (type == VERSION_PART) {
        return new DepLangVersionPartImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
