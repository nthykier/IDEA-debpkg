// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.nthykier.debpkg.deb822.psi.Deb822Types.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class Deb822Parser implements PsiParser, LightPsiParser {

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
    return deb822file(b, l + 1);
  }

  /* ********************************************************** */
  // paragraph (COMMENT* paragraph)*
  public static boolean all_paragraphs(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "all_paragraphs")) return false;
    if (!nextTokenIs(b, FIELD_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = paragraph(b, l + 1);
    r = r && all_paragraphs_1(b, l + 1);
    exit_section_(b, m, ALL_PARAGRAPHS, r);
    return r;
  }

  // (COMMENT* paragraph)*
  private static boolean all_paragraphs_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "all_paragraphs_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!all_paragraphs_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "all_paragraphs_1", c)) break;
    }
    return true;
  }

  // COMMENT* paragraph
  private static boolean all_paragraphs_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "all_paragraphs_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = all_paragraphs_1_0_0(b, l + 1);
    r = r && paragraph(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMENT*
  private static boolean all_paragraphs_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "all_paragraphs_1_0_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "all_paragraphs_1_0_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // COMMENT* all_paragraphs COMMENT*
  static boolean deb822file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deb822file")) return false;
    if (!nextTokenIs(b, "", COMMENT, FIELD_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = deb822file_0(b, l + 1);
    r = r && all_paragraphs(b, l + 1);
    r = r && deb822file_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMENT*
  private static boolean deb822file_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deb822file_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "deb822file_0", c)) break;
    }
    return true;
  }

  // COMMENT*
  private static boolean deb822file_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deb822file_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "deb822file_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // FIELD_NAME
  public static boolean field(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field")) return false;
    if (!nextTokenIs(b, FIELD_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FIELD_NAME);
    exit_section_(b, m, FIELD, r);
    return r;
  }

  /* ********************************************************** */
  // field SEPARATOR value_parts
  public static boolean field_value_pair(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_value_pair")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FIELD_VALUE_PAIR, "<field value pair>");
    r = field(b, l + 1);
    r = r && consumeToken(b, SEPARATOR);
    p = r; // pin = 2
    r = r && value_parts(b, l + 1);
    exit_section_(b, l, m, r, p, recover_property_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // field_value_pair (COMMENT* field_value_pair)* (PARAGRAPH_FINISH|<<eof>>)
  public static boolean paragraph(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph")) return false;
    if (!nextTokenIs(b, FIELD_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = field_value_pair(b, l + 1);
    r = r && paragraph_1(b, l + 1);
    r = r && paragraph_2(b, l + 1);
    exit_section_(b, m, PARAGRAPH, r);
    return r;
  }

  // (COMMENT* field_value_pair)*
  private static boolean paragraph_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!paragraph_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "paragraph_1", c)) break;
    }
    return true;
  }

  // COMMENT* field_value_pair
  private static boolean paragraph_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = paragraph_1_0_0(b, l + 1);
    r = r && field_value_pair(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMENT*
  private static boolean paragraph_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph_1_0_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "paragraph_1_0_0", c)) break;
    }
    return true;
  }

  // PARAGRAPH_FINISH|<<eof>>
  private static boolean paragraph_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARAGRAPH_FINISH);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(FIELD_NAME|SEPARATOR|SUBSTVAR_TOKEN|VALUE_TOKEN|COMMA|COMMENT|PARAGRAPH_FINISH)
  static boolean recover_property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_property")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_property_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // FIELD_NAME|SEPARATOR|SUBSTVAR_TOKEN|VALUE_TOKEN|COMMA|COMMENT|PARAGRAPH_FINISH
  private static boolean recover_property_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_property_0")) return false;
    boolean r;
    r = consumeToken(b, FIELD_NAME);
    if (!r) r = consumeToken(b, SEPARATOR);
    if (!r) r = consumeToken(b, SUBSTVAR_TOKEN);
    if (!r) r = consumeToken(b, VALUE_TOKEN);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, PARAGRAPH_FINISH);
    return r;
  }

  /* ********************************************************** */
  // SUBSTVAR_TOKEN
  public static boolean substvar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "substvar")) return false;
    if (!nextTokenIs(b, SUBSTVAR_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SUBSTVAR_TOKEN);
    exit_section_(b, m, SUBSTVAR, r);
    return r;
  }

  /* ********************************************************** */
  // VALUE_TOKEN
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    if (!nextTokenIs(b, VALUE_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VALUE_TOKEN);
    exit_section_(b, m, VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // COMMENT* (value|COMMA|substvar) (COMMENT|value|COMMA|substvar)*
  public static boolean value_parts(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_PARTS, "<Value or Substvars>");
    r = value_parts_0(b, l + 1);
    r = r && value_parts_1(b, l + 1);
    r = r && value_parts_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COMMENT*
  private static boolean value_parts_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "value_parts_0", c)) break;
    }
    return true;
  }

  // value|COMMA|substvar
  private static boolean value_parts_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_1")) return false;
    boolean r;
    r = value(b, l + 1);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = substvar(b, l + 1);
    return r;
  }

  // (COMMENT|value|COMMA|substvar)*
  private static boolean value_parts_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!value_parts_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "value_parts_2", c)) break;
    }
    return true;
  }

  // COMMENT|value|COMMA|substvar
  private static boolean value_parts_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_2_0")) return false;
    boolean r;
    r = consumeToken(b, COMMENT);
    if (!r) r = value(b, l + 1);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = substvar(b, l + 1);
    return r;
  }

  static final Parser recover_property_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_property(b, l + 1);
    }
  };
}
