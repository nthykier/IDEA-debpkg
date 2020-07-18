// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.language;

import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;import com.intellij.spellchecker.tokenizer.Tokenizer;

%%

%public
%class Deb822Lexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

NEWLINE=\n
SINGLE_SPACE=[ ]
WHITE_SPACE=[\ \t\f]
END_OF_LINE_COMMENT="#"[^\r\n]*
SUBSTVAR=[$][{][a-zA-Z0-9][a-zA-Z0-9\-:]*[}]
SEPARATOR=[:]
/* All characters in range 0x21 to 0x39 (incl.) + 0x3b to 0x7e (incl.) are valid, except for the
 * first character, where 0x23 (#) and 0x2D (-) are not permitted.
 */
FIRST_FIELD_CHARACTER=[\u0021\u0022\u0024-\u002c\u002e-\u0039\u003b-\u007e]
FIELD_CHARACTER=[\u0021-\u0039\u003b-\u007e]

%state WAITING_FOR_SEPATOR, PARSING_INITIAL_VALUE_AFTER_SEPARATOR PARSING_INITIAL_VALUE, MAYBE_CONT_VALUE, SEEN_INITIAL_VALUE, PARSING_CONT_VALUE

%%

<YYINITIAL>{
{END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return Deb822Types.COMMENT; }
^{FIRST_FIELD_CHARACTER}{FIELD_CHARACTER}*      { yybegin(WAITING_FOR_SEPATOR); return Deb822Types.FIELD_NAME; }
{WHITE_SPACE}+                                  { return TokenType.WHITE_SPACE; }
{NEWLINE}+                                      { return TokenType.WHITE_SPACE; }
}

<WAITING_FOR_SEPATOR>{
{WHITE_SPACE}+                                   { return TokenType.WHITE_SPACE; }

{SEPARATOR}                                      { yybegin(PARSING_INITIAL_VALUE_AFTER_SEPARATOR); return Deb822Types.SEPARATOR; }
}

/* Same as PARSING_INITIAL_VALUE except we allow leading whitespace, which forces us into PARSING_INITIAL_VALUE
 * Ideally, we would like to just consume 0 or more whitespace and then move to PARSING_INITIAL_VALUE.  However,
 * that does not work in JFlex.
 *
 * To see where this is relevant, try:
 * """
 * Field1: value
 * Field2:value-but-no-leading-space
 * Field3: foo
 *     Continuation line where leadning and trailing space is significant... >.>
 * """
 *
 * Field2 is the variant that requires us to duplicate most of PARSING_INITIAL_VALUE in
 * PARSING_INITIAL_VALUE_AFTER_SEPARATOR
 */
<PARSING_INITIAL_VALUE_AFTER_SEPARATOR>{
{WHITE_SPACE}+                                                   { yybegin(PARSING_INITIAL_VALUE); return TokenType.WHITE_SPACE; }
{SUBSTVAR}                                                       { yybegin(SEEN_INITIAL_VALUE); return Deb822Types.SUBSTVAR_TOKEN; }
[$][{][}]                                                        { yybegin(SEEN_INITIAL_VALUE); return Deb822Types.SUBSTVAR_TOKEN; }
[$]                                                              { yybegin(SEEN_INITIAL_VALUE); return Deb822Types.VALUE; }
[^$ \t\r\n]+                                                     { yybegin(SEEN_INITIAL_VALUE); return Deb822Types.VALUE; }
}

<PARSING_INITIAL_VALUE>{
{NEWLINE}                                                        { yybegin(MAYBE_CONT_VALUE); return TokenType.WHITE_SPACE; }
{WHITE_SPACE}+$                                                  { return TokenType.WHITE_SPACE; }
{SUBSTVAR}                                                       { yybegin(SEEN_INITIAL_VALUE); return Deb822Types.SUBSTVAR_TOKEN; }
[$][{][}]                                                        { yybegin(SEEN_INITIAL_VALUE); return Deb822Types.SUBSTVAR_TOKEN; }
[$]                                                              { yybegin(SEEN_INITIAL_VALUE); return Deb822Types.VALUE; }
[^$ \t\r\n]+                                                     { yybegin(SEEN_INITIAL_VALUE); return Deb822Types.VALUE; }
}

<SEEN_INITIAL_VALUE>{
{SUBSTVAR}                                                       { return Deb822Types.SUBSTVAR_TOKEN; }
[$][{][}]                                                        { return Deb822Types.SUBSTVAR_TOKEN; }
[$]                                                              { return Deb822Types.VALUE; }
[^$\r\n]+                                                        { return Deb822Types.VALUE; }
{WHITE_SPACE}*{NEWLINE}                                          { yybegin(MAYBE_CONT_VALUE); return TokenType.WHITE_SPACE; }
}

<PARSING_CONT_VALUE>{
{SUBSTVAR}                                                       { return Deb822Types.SUBSTVAR_TOKEN; }
[$][{][}]                                                        { return Deb822Types.SUBSTVAR_TOKEN; }
[$]                                                              { return Deb822Types.VALUE; }
[^$\r\n]+                                                        { return Deb822Types.VALUE; }
{NEWLINE}                                                        { yybegin(MAYBE_CONT_VALUE); return TokenType.WHITE_SPACE; }
}

<MAYBE_CONT_VALUE>{
{END_OF_LINE_COMMENT}{NEWLINE}                           { return Deb822Types.COMMENT; }
^{SINGLE_SPACE}                                          { yybegin(PARSING_CONT_VALUE); return TokenType.WHITE_SPACE;}
^{FIRST_FIELD_CHARACTER}{FIELD_CHARACTER}*               { yybegin(WAITING_FOR_SEPATOR); return Deb822Types.FIELD_NAME; }
{NEWLINE}                                                { yybegin(YYINITIAL); return Deb822Types.PARAGRAPH_FINISH; }
}

[^]                                              { return TokenType.BAD_CHARACTER; }
