// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.deplang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.nthykier.debpkg.deb822.deplang.psi.DependencyLanguageTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class DependencyLanguageParser implements PsiParser, LightPsiParser {

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
    return start(b, l + 1);
  }

  /* ********************************************************** */
  // <<a>> (<<b>> <<a>>)*
  static boolean a_b_a_p(PsiBuilder b, int l, Parser _a, Parser _b) {
    if (!recursion_guard_(b, l, "a_b_a_p")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = _a.parse(b, l);
    p = r; // pin = 1
    r = r && a_b_a_p_1(b, l + 1, _b, _a);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (<<b>> <<a>>)*
  private static boolean a_b_a_p_1(PsiBuilder b, int l, Parser _b, Parser _a) {
    if (!recursion_guard_(b, l, "a_b_a_p_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!a_b_a_p_1_0(b, l + 1, _b, _a)) break;
      if (!empty_element_parsed_guard_(b, "a_b_a_p_1", c)) break;
    }
    return true;
  }

  // <<b>> <<a>>
  private static boolean a_b_a_p_1_0(PsiBuilder b, int l, Parser _b, Parser _a) {
    if (!recursion_guard_(b, l, "a_b_a_p_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = _b.parse(b, l);
    r = r && _a.parse(b, l);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<a_b_a_p or_dependency_clause ','>> ','?
  public static boolean and_dependency_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_dependency_clause")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, AND_DEPENDENCY_CLAUSE, "<and dependency clause>");
    r = a_b_a_p(b, l + 1, DependencyLanguageParser::or_dependency_clause, OPERATOR_AND_parser_);
    r = r && and_dependency_clause_1(b, l + 1);
    exit_section_(b, l, m, r, false, DependencyLanguageParser::recover_dependency_clauses);
    return r;
  }

  // ','?
  private static boolean and_dependency_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_dependency_clause_1")) return false;
    consumeToken(b, OPERATOR_AND);
    return true;
  }

  /* ********************************************************** */
  // '[' restriction_list ']'
  public static boolean arch_restriction_part(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arch_restriction_part")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARCH_RESTRICTION_PART, "<arch restriction part>");
    r = consumeToken(b, BRACKETS_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, restriction_list(b, l + 1));
    r = p && consumeToken(b, BRACKETS_CLOSE) && r;
    exit_section_(b, l, m, r, p, DependencyLanguageParser::recover_until_closing_token);
    return r || p;
  }

  /* ********************************************************** */
  // '<' restriction_list '>'
  public static boolean build_profile_restriction_part(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "build_profile_restriction_part")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BUILD_PROFILE_RESTRICTION_PART, "<build profile restriction part>");
    r = consumeToken(b, LESS_THAN);
    p = r; // pin = 1
    r = r && report_error_(b, restriction_list(b, l + 1));
    r = p && consumeToken(b, GREATER_THAN) && r;
    exit_section_(b, l, m, r, p, DependencyLanguageParser::recover_until_closing_token);
    return r || p;
  }

  /* ********************************************************** */
  // package_name (version_part)? (arch_restriction_part)? (build_profile_restriction_part+)?
  public static boolean dependency(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency")) return false;
    if (!nextTokenIs(b, "<dependency>", SUBSTVAR_TOKEN, WORDISH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEPENDENCY, "<dependency>");
    r = package_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, dependency_1(b, l + 1));
    r = p && report_error_(b, dependency_2(b, l + 1)) && r;
    r = p && dependency_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (version_part)?
  private static boolean dependency_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_1")) return false;
    dependency_1_0(b, l + 1);
    return true;
  }

  // (version_part)
  private static boolean dependency_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = version_part(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (arch_restriction_part)?
  private static boolean dependency_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_2")) return false;
    dependency_2_0(b, l + 1);
    return true;
  }

  // (arch_restriction_part)
  private static boolean dependency_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = arch_restriction_part(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (build_profile_restriction_part+)?
  private static boolean dependency_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_3")) return false;
    dependency_3_0(b, l + 1);
    return true;
  }

  // build_profile_restriction_part+
  private static boolean dependency_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = build_profile_restriction_part(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!build_profile_restriction_part(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "dependency_3_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (language_definition '--')? and_dependency_clause+
  public static boolean dependency_info(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_info")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEPENDENCY_INFO, "<dependency info>");
    r = dependency_info_0(b, l + 1);
    p = r; // pin = 1
    r = r && dependency_info_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (language_definition '--')?
  private static boolean dependency_info_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_info_0")) return false;
    dependency_info_0_0(b, l + 1);
    return true;
  }

  // language_definition '--'
  private static boolean dependency_info_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_info_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = language_definition(b, l + 1);
    r = r && consumeToken(b, DEPENDENCY_LANG_SEPARATOR);
    exit_section_(b, m, null, r);
    return r;
  }

  // and_dependency_clause+
  private static boolean dependency_info_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dependency_info_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = and_dependency_clause(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!and_dependency_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "dependency_info_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // WORDISH
  public static boolean language_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "language_definition")) return false;
    if (!nextTokenIs(b, WORDISH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WORDISH);
    exit_section_(b, m, LANGUAGE_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // WORDISH
  static boolean name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "name")) return false;
    if (!nextTokenIs(b, "<name>", WORDISH)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, null, "<name>");
    r = consumeToken(b, WORDISH);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // WORDISH|substvar
  static boolean name_or_substvar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "name_or_substvar")) return false;
    if (!nextTokenIs(b, "<name (or substitution variable)>", SUBSTVAR_TOKEN, WORDISH)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, null, "<name (or substitution variable)>");
    r = consumeToken(b, WORDISH);
    if (!r) r = substvar(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // name_or_substvar
  static boolean name_or_substvar_as_version(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "name_or_substvar_as_version")) return false;
    if (!nextTokenIs(b, "<version (or substitution variable)>", SUBSTVAR_TOKEN, WORDISH)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, null, "<version (or substitution variable)>");
    r = name_or_substvar(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // <<a_b_a_p dependency '|'>>
  public static boolean or_dependency_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_dependency_clause")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OR_DEPENDENCY_CLAUSE, "<or dependency clause>");
    r = a_b_a_p(b, l + 1, DependencyLanguageParser::dependency, OPERATOR_OR_parser_);
    exit_section_(b, l, m, r, false, DependencyLanguageParser::recover_dependency_clauses);
    return r;
  }

  /* ********************************************************** */
  // name_or_substvar
  public static boolean package_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_name")) return false;
    if (!nextTokenIs(b, "<name (or substitution variable)>", SUBSTVAR_TOKEN, WORDISH)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PACKAGE_NAME, "<name (or substitution variable)>");
    r = name_or_substvar(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(','|'|'|'--')
  static boolean recover_dependency_clauses(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_dependency_clauses")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_dependency_clauses_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ','|'|'|'--'
  private static boolean recover_dependency_clauses_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_dependency_clauses_0")) return false;
    boolean r;
    r = consumeToken(b, OPERATOR_AND);
    if (!r) r = consumeToken(b, OPERATOR_OR);
    if (!r) r = consumeToken(b, DEPENDENCY_LANG_SEPARATOR);
    return r;
  }

  /* ********************************************************** */
  // !(')'|'<'|'['|','|'|'|'--')
  static boolean recover_until_closing_token(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_until_closing_token")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_until_closing_token_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ')'|'<'|'['|','|'|'|'--'
  private static boolean recover_until_closing_token_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_until_closing_token_0")) return false;
    boolean r;
    r = consumeToken(b, PARANTHESES_CLOSE);
    if (!r) r = consumeToken(b, LESS_THAN);
    if (!r) r = consumeToken(b, BRACKETS_OPEN);
    if (!r) r = consumeToken(b, OPERATOR_AND);
    if (!r) r = consumeToken(b, OPERATOR_OR);
    if (!r) r = consumeToken(b, DEPENDENCY_LANG_SEPARATOR);
    return r;
  }

  /* ********************************************************** */
  // ('!'? name)+
  public static boolean restriction_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "restriction_list")) return false;
    if (!nextTokenIs(b, "<restriction list>", NEGATION, WORDISH)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RESTRICTION_LIST, "<restriction list>");
    r = restriction_list_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!restriction_list_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "restriction_list", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '!'? name
  private static boolean restriction_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "restriction_list_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = restriction_list_0_0(b, l + 1);
    r = r && name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '!'?
  private static boolean restriction_list_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "restriction_list_0_0")) return false;
    consumeToken(b, NEGATION);
    return true;
  }

  /* ********************************************************** */
  // dependency_info
  static boolean start(PsiBuilder b, int l) {
    return dependency_info(b, l + 1);
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
  // name_or_substvar_as_version
  public static boolean version(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "version")) return false;
    if (!nextTokenIs(b, "<version (or substitution variable)>", SUBSTVAR_TOKEN, WORDISH)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VERSION, "<version (or substitution variable)>");
    r = name_or_substvar_as_version(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '='|'>='|'<='|'<<'|'>>' | /* <-- valid / invalid --> */ '!='|'=='|'<''>'?|'>'
  public static boolean version_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "version_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VERSION_OPERATOR, "<version operator>");
    r = consumeToken(b, "=");
    if (!r) r = consumeToken(b, ">=");
    if (!r) r = consumeToken(b, "<=");
    if (!r) r = consumeToken(b, "<<");
    if (!r) r = consumeToken(b, ">>");
    if (!r) r = consumeToken(b, "!=");
    if (!r) r = consumeToken(b, "==");
    if (!r) r = version_operator_7(b, l + 1);
    if (!r) r = consumeToken(b, GREATER_THAN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '<''>'?
  private static boolean version_operator_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "version_operator_7")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LESS_THAN);
    p = r; // pin = 1
    r = r && version_operator_7_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // '>'?
  private static boolean version_operator_7_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "version_operator_7_1")) return false;
    consumeToken(b, GREATER_THAN);
    return true;
  }

  /* ********************************************************** */
  // '(' version_operator version ')'
  public static boolean version_part(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "version_part")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VERSION_PART, "<version part>");
    r = consumeToken(b, PARANTHESES_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, version_operator(b, l + 1));
    r = p && report_error_(b, version(b, l + 1)) && r;
    r = p && consumeToken(b, PARANTHESES_CLOSE) && r;
    exit_section_(b, l, m, r, p, DependencyLanguageParser::recover_until_closing_token);
    return r || p;
  }

  static final Parser OPERATOR_AND_parser_ = (b, l) -> consumeToken(b, OPERATOR_AND);
  static final Parser OPERATOR_OR_parser_ = (b, l) -> consumeToken(b, OPERATOR_OR);
}
