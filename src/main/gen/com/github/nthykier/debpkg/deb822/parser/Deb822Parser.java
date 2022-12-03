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
  // GPG_ARMOR_HEADER* GPG_ARMOR_HEADERS_END {
  // }
  static boolean armor_headers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "armor_headers")) return false;
    if (!nextTokenIs(b, "", GPG_ARMOR_HEADER, GPG_ARMOR_HEADERS_END)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = armor_headers_0(b, l + 1);
    r = r && consumeToken(b, GPG_ARMOR_HEADERS_END);
    r = r && armor_headers_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // GPG_ARMOR_HEADER*
  private static boolean armor_headers_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "armor_headers_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, GPG_ARMOR_HEADER)) break;
      if (!empty_element_parsed_guard_(b, "armor_headers_0", c)) break;
    }
    return true;
  }

  // {
  // }
  private static boolean armor_headers_2(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // '!'? BUILD_PROFILE_TOKEN
  public static boolean build_profile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "build_profile")) return false;
    if (!nextTokenIs(b, "<build profile>", BUILD_PROFILE_TOKEN, NEGATION)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BUILD_PROFILE, "<build profile>");
    r = build_profile_0(b, l + 1);
    r = r && consumeToken(b, BUILD_PROFILE_TOKEN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '!'?
  private static boolean build_profile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "build_profile_0")) return false;
    consumeToken(b, NEGATION);
    return true;
  }

  /* ********************************************************** */
  // '<' ignorable_value_noise? build_profile (ignorable_value_noise|build_profile)* '>'
  public static boolean build_profile_group(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "build_profile_group")) return false;
    if (!nextTokenIs(b, ANGLE_BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ANGLE_BRACKET_OPEN);
    r = r && build_profile_group_1(b, l + 1);
    r = r && build_profile(b, l + 1);
    r = r && build_profile_group_3(b, l + 1);
    r = r && consumeToken(b, ANGLE_BRACKET_CLOSE);
    exit_section_(b, m, BUILD_PROFILE_GROUP, r);
    return r;
  }

  // ignorable_value_noise?
  private static boolean build_profile_group_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "build_profile_group_1")) return false;
    ignorable_value_noise(b, l + 1);
    return true;
  }

  // (ignorable_value_noise|build_profile)*
  private static boolean build_profile_group_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "build_profile_group_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!build_profile_group_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "build_profile_group_3", c)) break;
    }
    return true;
  }

  // ignorable_value_noise|build_profile
  private static boolean build_profile_group_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "build_profile_group_3_0")) return false;
    boolean r;
    r = ignorable_value_noise(b, l + 1);
    if (!r) r = build_profile(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // COMMENT* gpg_signed? all_paragraphs gpg_signature? COMMENT*
  static boolean deb822file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deb822file")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = deb822file_0(b, l + 1);
    r = r && deb822file_1(b, l + 1);
    r = r && all_paragraphs(b, l + 1);
    r = r && deb822file_3(b, l + 1);
    r = r && deb822file_4(b, l + 1);
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

  // gpg_signed?
  private static boolean deb822file_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deb822file_1")) return false;
    gpg_signed(b, l + 1);
    return true;
  }

  // gpg_signature?
  private static boolean deb822file_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deb822file_3")) return false;
    gpg_signature(b, l + 1);
    return true;
  }

  // COMMENT*
  private static boolean deb822file_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deb822file_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "deb822file_4", c)) break;
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
  // field SEPARATOR ignorable_value_noise? value_parts?
  public static boolean field_value_pair(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_value_pair")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FIELD_VALUE_PAIR, "<field value pair>");
    r = field(b, l + 1);
    r = r && consumeToken(b, SEPARATOR);
    p = r; // pin = 2
    r = r && report_error_(b, field_value_pair_2(b, l + 1));
    r = p && field_value_pair_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, Deb822Parser::recover_property);
    return r || p;
  }

  // ignorable_value_noise?
  private static boolean field_value_pair_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_value_pair_2")) return false;
    ignorable_value_noise(b, l + 1);
    return true;
  }

  // value_parts?
  private static boolean field_value_pair_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_value_pair_3")) return false;
    value_parts(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // GPG_BEGIN_SIGNATURE armor_headers gpg_signature_blob GPG_END_SIGNATURE
  public static boolean gpg_signature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "gpg_signature")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GPG_SIGNATURE, "<gpg signature>");
    r = consumeToken(b, GPG_BEGIN_SIGNATURE);
    p = r; // pin = 1
    r = r && report_error_(b, armor_headers(b, l + 1));
    r = p && report_error_(b, gpg_signature_blob(b, l + 1)) && r;
    r = p && consumeToken(b, GPG_END_SIGNATURE) && r;
    exit_section_(b, l, m, r, p, Deb822Parser::recover_signature);
    return r || p;
  }

  /* ********************************************************** */
  // GPG_SIGNATURE_BLOB_PART*
  static boolean gpg_signature_blob(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "gpg_signature_blob")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, GPG_SIGNATURE_BLOB_PART)) break;
      if (!empty_element_parsed_guard_(b, "gpg_signature_blob", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // GPG_BEGIN_SIGNED_MESSAGE armor_headers
  public static boolean gpg_signed(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "gpg_signed")) return false;
    if (!nextTokenIs(b, GPG_BEGIN_SIGNED_MESSAGE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GPG_SIGNED, null);
    r = consumeToken(b, GPG_BEGIN_SIGNED_MESSAGE);
    p = r; // pin = 1
    r = r && armor_headers(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // HANGING_CONT_VALUE_TOKEN
  public static boolean hanging_cont_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hanging_cont_value")) return false;
    if (!nextTokenIs(b, HANGING_CONT_VALUE_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HANGING_CONT_VALUE_TOKEN);
    exit_section_(b, m, HANGING_CONT_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // (COMMENT | hanging_cont_value)+
  static boolean ignorable_value_noise(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ignorable_value_noise")) return false;
    if (!nextTokenIs(b, "", COMMENT, HANGING_CONT_VALUE_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ignorable_value_noise_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!ignorable_value_noise_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ignorable_value_noise", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMENT | hanging_cont_value
  private static boolean ignorable_value_noise_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ignorable_value_noise_0")) return false;
    boolean r;
    r = consumeToken(b, COMMENT);
    if (!r) r = hanging_cont_value(b, l + 1);
    return r;
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
  // !(GPG_ARMOR_HEADERS_END|GPG_SIGNATURE_BLOB_PART|GPG_END_SIGNATURE)
  static boolean recover_signature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_signature")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_signature_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // GPG_ARMOR_HEADERS_END|GPG_SIGNATURE_BLOB_PART|GPG_END_SIGNATURE
  private static boolean recover_signature_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_signature_0")) return false;
    boolean r;
    r = consumeToken(b, GPG_ARMOR_HEADERS_END);
    if (!r) r = consumeToken(b, GPG_SIGNATURE_BLOB_PART);
    if (!r) r = consumeToken(b, GPG_END_SIGNATURE);
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
  // (value|substvar|COMMA) (ignorable_value_noise|value|substvar|COMMA)*
  //               | build_profile_group+ ignorable_value_noise?
  public static boolean value_parts(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_PARTS, "<Value or Substvars>");
    r = value_parts_0(b, l + 1);
    if (!r) r = value_parts_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (value|substvar|COMMA) (ignorable_value_noise|value|substvar|COMMA)*
  private static boolean value_parts_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value_parts_0_0(b, l + 1);
    r = r && value_parts_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // value|substvar|COMMA
  private static boolean value_parts_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_0_0")) return false;
    boolean r;
    r = value(b, l + 1);
    if (!r) r = substvar(b, l + 1);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  // (ignorable_value_noise|value|substvar|COMMA)*
  private static boolean value_parts_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!value_parts_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "value_parts_0_1", c)) break;
    }
    return true;
  }

  // ignorable_value_noise|value|substvar|COMMA
  private static boolean value_parts_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_0_1_0")) return false;
    boolean r;
    r = ignorable_value_noise(b, l + 1);
    if (!r) r = value(b, l + 1);
    if (!r) r = substvar(b, l + 1);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  // build_profile_group+ ignorable_value_noise?
  private static boolean value_parts_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value_parts_1_0(b, l + 1);
    r = r && value_parts_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // build_profile_group+
  private static boolean value_parts_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = build_profile_group(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!build_profile_group(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "value_parts_1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // ignorable_value_noise?
  private static boolean value_parts_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_parts_1_1")) return false;
    ignorable_value_noise(b, l + 1);
    return true;
  }

}
