// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.aptlist.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.nthykier.debpkg.aptlist.psi.AptListTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;

import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class AptListParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return aptListFile(b, l + 1);
  }

  /* ********************************************************** */
  // aptSourcesLine*
  static boolean aptListFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "aptListFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!aptSourcesLine(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "aptListFile", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // type uri option_list? suite component+ ('\n'|<<eof>>)
  public static boolean aptSourcesLine(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "aptSourcesLine")) return false;
    if (!nextTokenIs(b, TYPE_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type(b, l + 1);
    r = r && uri(b, l + 1);
    r = r && aptSourcesLine_2(b, l + 1);
    r = r && suite(b, l + 1);
    r = r && aptSourcesLine_4(b, l + 1);
    r = r && aptSourcesLine_5(b, l + 1);
    exit_section_(b, m, APT_SOURCES_LINE, r);
    return r;
  }

  // option_list?
  private static boolean aptSourcesLine_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "aptSourcesLine_2")) return false;
    option_list(b, l + 1);
    return true;
  }

  // component+
  private static boolean aptSourcesLine_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "aptSourcesLine_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = component(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!component(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "aptSourcesLine_4", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // '\n'|<<eof>>
  private static boolean aptSourcesLine_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "aptSourcesLine_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NEWLINE_TOKEN);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // NAME_TOKEN
  public static boolean component(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component")) return false;
    if (!nextTokenIs(b, NAME_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NAME_TOKEN);
    exit_section_(b, m, COMPONENT, r);
    return r;
  }

  /* ********************************************************** */
  // option_key '=' option_values
  public static boolean option(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option")) return false;
    if (!nextTokenIs(b, OPTION_KEY_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = option_key(b, l + 1);
    r = r && consumeToken(b, EQUALS_TOKEN);
    r = r && option_values(b, l + 1);
    exit_section_(b, m, OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // OPTION_KEY_TOKEN
  public static boolean option_key(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option_key")) return false;
    if (!nextTokenIs(b, OPTION_KEY_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPTION_KEY_TOKEN);
    exit_section_(b, m, OPTION_KEY, r);
    return r;
  }

  /* ********************************************************** */
  // '[' option+ ']'
  static boolean option_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option_list")) return false;
    if (!nextTokenIs(b, BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACKET_OPEN);
    r = r && option_list_1(b, l + 1);
    r = r && consumeToken(b, BRACKET_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  // option+
  private static boolean option_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option_list_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = option(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!option(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "option_list_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OPTION_VALUE_TOKEN
  public static boolean option_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option_value")) return false;
    if (!nextTokenIs(b, OPTION_VALUE_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPTION_VALUE_TOKEN);
    exit_section_(b, m, OPTION_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // option_value (',' option_value)* ','?
  static boolean option_values(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option_values")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = option_value(b, l + 1);
    r = r && option_values_1(b, l + 1);
    r = r && option_values_2(b, l + 1);
    exit_section_(b, l, m, r, false, AptListParser::recoverLine);
    return r;
  }

  // (',' option_value)*
  private static boolean option_values_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option_values_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!option_values_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "option_values_1", c)) break;
    }
    return true;
  }

  // ',' option_value
  private static boolean option_values_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option_values_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA_TOKEN);
    r = r && option_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean option_values_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option_values_2")) return false;
    consumeToken(b, COMMA_TOKEN);
    return true;
  }

  /* ********************************************************** */
  // !('\n'|<<eof>>)
  static boolean recoverLine(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverLine")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverLine_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '\n'|<<eof>>
  private static boolean recoverLine_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverLine_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NEWLINE_TOKEN);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // NAME_TOKEN
  public static boolean suite(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "suite")) return false;
    if (!nextTokenIs(b, NAME_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NAME_TOKEN);
    exit_section_(b, m, SUITE, r);
    return r;
  }

  /* ********************************************************** */
  // TYPE_TOKEN
  public static boolean type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type")) return false;
    if (!nextTokenIs(b, TYPE_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TYPE_TOKEN);
    exit_section_(b, m, TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // URI_TOKEN
  public static boolean uri(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uri")) return false;
    if (!nextTokenIs(b, URI_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, URI_TOKEN);
    exit_section_(b, m, URI, r);
    return r;
  }

}
