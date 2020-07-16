// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.dch.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.nthykier.debpkg.dch.psi.DchTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class DchParser implements PsiParser, LightPsiParser {

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
    return dchFile(b, l + 1);
  }

  /* ********************************************************** */
  // version_line changelog_line+ signoff {
  // //  recoverWhile=recover_property
  // }
  public static boolean changelog_entry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "changelog_entry")) return false;
    if (!nextTokenIs(b, SOURCE_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = version_line(b, l + 1);
    r = r && changelog_entry_1(b, l + 1);
    r = r && signoff(b, l + 1);
    r = r && changelog_entry_3(b, l + 1);
    exit_section_(b, m, CHANGELOG_ENTRY, r);
    return r;
  }

  // changelog_line+
  private static boolean changelog_entry_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "changelog_entry_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = changelog_line(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!changelog_line(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "changelog_entry_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // {
  // //  recoverWhile=recover_property
  // }
  private static boolean changelog_entry_3(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // CHANGE_DETAILS|CHANGE_RESPONSIBLE
  public static boolean changelog_line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "changelog_line")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CHANGELOG_LINE, "<changelog line>");
    r = consumeToken(b, CHANGE_DETAILS);
    if (!r) r = consumeToken(b, CHANGE_RESPONSIBLE);
    exit_section_(b, l, m, r, false, recover_top_level_parser_);
    return r;
  }

  /* ********************************************************** */
  // changelog_entry+
  static boolean dchFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dchFile")) return false;
    if (!nextTokenIs(b, SOURCE_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = changelog_entry(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!changelog_entry(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "dchFile", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LESS_THAN MAINTAINER_EMAIL GREATER_THAN
  static boolean maintainer_email(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "maintainer_email")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, LESS_THAN, MAINTAINER_EMAIL, GREATER_THAN);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, recover_maintainer_email_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // !(SOURCE_NAME|SIGNOFF_STARTER|GREATER_THAN|DOUBLE_SPACE|SIGNOFF_DATE)
  static boolean recover_maintainer_email(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_maintainer_email")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_maintainer_email_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SOURCE_NAME|SIGNOFF_STARTER|GREATER_THAN|DOUBLE_SPACE|SIGNOFF_DATE
  private static boolean recover_maintainer_email_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_maintainer_email_0")) return false;
    boolean r;
    r = consumeToken(b, SOURCE_NAME);
    if (!r) r = consumeToken(b, SIGNOFF_STARTER);
    if (!r) r = consumeToken(b, GREATER_THAN);
    if (!r) r = consumeToken(b, DOUBLE_SPACE);
    if (!r) r = consumeToken(b, SIGNOFF_DATE);
    return r;
  }

  /* ********************************************************** */
  // !(SOURCE_NAME|SIGNOFF_STARTER|MAINTAINER_NAME|MAINTAINER_EMAIL|GREATER_THAN|LESS_THAN|CHANGE_DETAILS|CHANGE_RESPONSIBLE)
  static boolean recover_signoff(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_signoff")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_signoff_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SOURCE_NAME|SIGNOFF_STARTER|MAINTAINER_NAME|MAINTAINER_EMAIL|GREATER_THAN|LESS_THAN|CHANGE_DETAILS|CHANGE_RESPONSIBLE
  private static boolean recover_signoff_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_signoff_0")) return false;
    boolean r;
    r = consumeToken(b, SOURCE_NAME);
    if (!r) r = consumeToken(b, SIGNOFF_STARTER);
    if (!r) r = consumeToken(b, MAINTAINER_NAME);
    if (!r) r = consumeToken(b, MAINTAINER_EMAIL);
    if (!r) r = consumeToken(b, GREATER_THAN);
    if (!r) r = consumeToken(b, LESS_THAN);
    if (!r) r = consumeToken(b, CHANGE_DETAILS);
    if (!r) r = consumeToken(b, CHANGE_RESPONSIBLE);
    return r;
  }

  /* ********************************************************** */
  // !(SOURCE_NAME|SIGNOFF_STARTER|CHANGE_DETAILS|CHANGE_RESPONSIBLE|DISTRIBUTION_NAME|SEMI_COLON|KEYVALUE_PAIR)
  static boolean recover_top_level(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_top_level")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_top_level_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SOURCE_NAME|SIGNOFF_STARTER|CHANGE_DETAILS|CHANGE_RESPONSIBLE|DISTRIBUTION_NAME|SEMI_COLON|KEYVALUE_PAIR
  private static boolean recover_top_level_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_top_level_0")) return false;
    boolean r;
    r = consumeToken(b, SOURCE_NAME);
    if (!r) r = consumeToken(b, SIGNOFF_STARTER);
    if (!r) r = consumeToken(b, CHANGE_DETAILS);
    if (!r) r = consumeToken(b, CHANGE_RESPONSIBLE);
    if (!r) r = consumeToken(b, DISTRIBUTION_NAME);
    if (!r) r = consumeToken(b, SEMI_COLON);
    if (!r) r = consumeToken(b, KEYVALUE_PAIR);
    return r;
  }

  /* ********************************************************** */
  // SIGNOFF_STARTER MAINTAINER_NAME maintainer_email DOUBLE_SPACE SIGNOFF_DATE
  public static boolean signoff(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signoff")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SIGNOFF, "<signoff>");
    r = consumeTokens(b, 2, SIGNOFF_STARTER, MAINTAINER_NAME);
    p = r; // pin = 2
    r = r && report_error_(b, maintainer_email(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, DOUBLE_SPACE, SIGNOFF_DATE)) && r;
    exit_section_(b, l, m, r, p, recover_signoff_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // PARANTHESES_OPEN VERSION PARANTHESES_CLOSE
  static boolean version(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "version")) return false;
    if (!nextTokenIs(b, PARANTHESES_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, PARANTHESES_OPEN, VERSION, PARANTHESES_CLOSE);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // SOURCE_NAME version DISTRIBUTION_NAME+ SEMI_COLON KEYVALUE_PAIR+
  public static boolean version_line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "version_line")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VERSION_LINE, "<version line>");
    r = consumeToken(b, SOURCE_NAME);
    r = r && version(b, l + 1);
    r = r && version_line_2(b, l + 1);
    r = r && consumeToken(b, SEMI_COLON);
    r = r && version_line_4(b, l + 1);
    exit_section_(b, l, m, r, false, recover_top_level_parser_);
    return r;
  }

  // DISTRIBUTION_NAME+
  private static boolean version_line_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "version_line_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DISTRIBUTION_NAME);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, DISTRIBUTION_NAME)) break;
      if (!empty_element_parsed_guard_(b, "version_line_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // KEYVALUE_PAIR+
  private static boolean version_line_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "version_line_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEYVALUE_PAIR);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, KEYVALUE_PAIR)) break;
      if (!empty_element_parsed_guard_(b, "version_line_4", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  static final Parser recover_maintainer_email_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_maintainer_email(b, l + 1);
    }
  };
  static final Parser recover_signoff_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_signoff(b, l + 1);
    }
  };
  static final Parser recover_top_level_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_top_level(b, l + 1);
    }
  };
}
