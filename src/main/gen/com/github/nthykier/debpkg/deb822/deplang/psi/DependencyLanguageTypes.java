// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.deplang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.nthykier.debpkg.deb822.deplang.psi.impl.*;

public interface DependencyLanguageTypes {

  IElementType AND_DEPENDENCY_CLAUSE = new DepLangElementType("AND_DEPENDENCY_CLAUSE");
  IElementType ARCH_RESTRICTION_PART = new DepLangElementType("ARCH_RESTRICTION_PART");
  IElementType BUILD_PROFILE_RESTRICTION_PART = new DepLangElementType("BUILD_PROFILE_RESTRICTION_PART");
  IElementType DEPENDENCY = new DepLangElementType("DEPENDENCY");
  IElementType DEPENDENCY_INFO = new DepLangElementType("DEPENDENCY_INFO");
  IElementType LANGUAGE_DEFINITION = new DepLangElementType("LANGUAGE_DEFINITION");
  IElementType OR_DEPENDENCY_CLAUSE = new DepLangElementType("OR_DEPENDENCY_CLAUSE");
  IElementType PACKAGE_NAME = new DepLangElementType("PACKAGE_NAME");
  IElementType RESTRICTION_LIST = new DepLangElementType("RESTRICTION_LIST");
  IElementType SUBSTVAR = new DepLangElementType("SUBSTVAR");
  IElementType VERSION = new DepLangElementType("VERSION");
  IElementType VERSION_OPERATOR = new DepLangElementType("VERSION_OPERATOR");
  IElementType VERSION_PART = new DepLangElementType("VERSION_PART");

  IElementType ANGLE_BRACKET_CLOSE = new DepLangTokenType(">");
  IElementType ANGLE_BRACKET_OPEN = new DepLangTokenType("<");
  IElementType BRACKETS_CLOSE = new DepLangTokenType("]");
  IElementType BRACKETS_OPEN = new DepLangTokenType("[");
  IElementType COMMENT = new DepLangTokenType("COMMENT");
  IElementType DEPENDENCY_LANG_SEPARATOR = new DepLangTokenType("--");
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
      else if (type == ARCH_RESTRICTION_PART) {
        return new DepLangArchRestrictionPartImpl(node);
      }
      else if (type == BUILD_PROFILE_RESTRICTION_PART) {
        return new DepLangBuildProfileRestrictionPartImpl(node);
      }
      else if (type == DEPENDENCY) {
        return new DepLangDependencyImpl(node);
      }
      else if (type == DEPENDENCY_INFO) {
        return new DepLangDependencyInfoImpl(node);
      }
      else if (type == LANGUAGE_DEFINITION) {
        return new DepLangLanguageDefinitionImpl(node);
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
