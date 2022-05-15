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
END_OF_LINE_COMMENT=[#][^\r\n]*
SUBSTVAR=[$][{][a-zA-Z0-9][a-zA-Z0-9\-:]*[}]
SEPARATOR=[:]
GPG_DASHES=-----
GPG_WHITESPACE=[ \t]
/* All characters in range 0x21 to 0x39 (incl.) + 0x3b to 0x7e (incl.) are valid, except for the
 * first character, where 0x23 (#) and 0x2D (-) are not permitted.
 */
FIRST_FIELD_CHARACTER=[\u0021\u0022\u0024-\u002c\u002e-\u0039\u003b-\u007e]
FIELD_CHARACTER=[\u0021-\u0039\u003b-\u007e]

%state WAITING_FOR_SEPARATOR, MAYBE_CONT_VALUE, PARSING_VALUE, GPG_PARSE_ARMORED_HEADERS, GPG_BEGIN_SIGNATURE_ARMORED_HEADERS, GPG_SIGNATURE_BLOB

%%

<YYINITIAL>{
{END_OF_LINE_COMMENT}                                                                                                { return Deb822Types.COMMENT; }
^{FIRST_FIELD_CHARACTER}{FIELD_CHARACTER}*                                                                           { yybegin(WAITING_FOR_SEPARATOR); return Deb822Types.FIELD_NAME; }
^{GPG_DASHES}BEGIN{SINGLE_SPACE}PGP{SINGLE_SPACE}SIGNED{SINGLE_SPACE}MESSAGE{GPG_DASHES}{GPG_WHITESPACE}*{NEWLINE}   { yybegin(GPG_PARSE_ARMORED_HEADERS); return Deb822Types.GPG_BEGIN_SIGNED_MESSAGE; }
^{GPG_DASHES}BEGIN{SINGLE_SPACE}PGP{SINGLE_SPACE}SIGNATURE{GPG_DASHES}{GPG_WHITESPACE}*{NEWLINE}                     { yybegin(GPG_BEGIN_SIGNATURE_ARMORED_HEADERS); return Deb822Types.GPG_BEGIN_SIGNATURE; }
^{WHITE_SPACE}+{NEWLINE}+                                                                                            { return TokenType.WHITE_SPACE; }
{NEWLINE}+                                                                                                           { return TokenType.WHITE_SPACE; }
}

<GPG_PARSE_ARMORED_HEADERS>{
\S+: [^\n]*\n         { return Deb822Types.GPG_ARMOR_HEADER; }
[*\n]+\n              { return TokenType.BAD_CHARACTER; }
^{NEWLINE}            { yybegin(YYINITIAL); return Deb822Types.GPG_ARMOR_HEADERS_END; }
}

<GPG_BEGIN_SIGNATURE_ARMORED_HEADERS>{
\S+: [^\n]*\n         { return Deb822Types.GPG_ARMOR_HEADER; }
[*\n]+\n              { return TokenType.BAD_CHARACTER; }
^{NEWLINE}            { yybegin(GPG_SIGNATURE_BLOB); return Deb822Types.GPG_ARMOR_HEADERS_END; }
}

<GPG_SIGNATURE_BLOB>{
[a-zA-Z0-9=/+_\-]+                                                                                    { return Deb822Types.GPG_SIGNATURE_BLOB_PART; }
{WHITE_SPACE}*\n                                                                                      { return TokenType.WHITE_SPACE; }
^{GPG_DASHES}END{SINGLE_SPACE}PGP{SINGLE_SPACE}SIGNATURE{GPG_DASHES}{GPG_WHITESPACE}*{NEWLINE}        { yybegin(GPG_PARSE_ARMORED_HEADERS); return Deb822Types.GPG_END_SIGNATURE; }
}

<WAITING_FOR_SEPARATOR>{
{WHITE_SPACE}+                                   { return TokenType.WHITE_SPACE; }
{SEPARATOR}                                      { yybegin(PARSING_VALUE); return Deb822Types.SEPARATOR; }
}


<PARSING_VALUE>{
{NEWLINE}                                                        { yybegin(MAYBE_CONT_VALUE); return TokenType.WHITE_SPACE; }
{SUBSTVAR}                                                       { return Deb822Types.SUBSTVAR_TOKEN; }
[$][{][}]                                                        { return Deb822Types.SUBSTVAR_TOKEN; }
[$]                                                              { return Deb822Types.VALUE_TOKEN; }
[,]                                                              { return Deb822Types.COMMA; }
[^$ ,\r\n]+                                                      { return Deb822Types.VALUE_TOKEN; }
{WHITE_SPACE}+                                                   { return TokenType.WHITE_SPACE; }
}

<MAYBE_CONT_VALUE>{
{END_OF_LINE_COMMENT}{NEWLINE}                                                     { return Deb822Types.COMMENT; }
/* Cope with some common missing " ." patterns */
^{NEWLINE}?({WHITE_SPACE}*{NEWLINE})*{WHITE_SPACE}*{NEWLINE}{SINGLE_SPACE}         { yybegin(PARSING_VALUE); return Deb822Types.HANGING_CONT_VALUE_TOKEN; }

^{SINGLE_SPACE}                                                                    { yybegin(PARSING_VALUE); return TokenType.WHITE_SPACE; }
^{FIRST_FIELD_CHARACTER}{FIELD_CHARACTER}*                                         { yybegin(WAITING_FOR_SEPARATOR); return Deb822Types.FIELD_NAME; }
{NEWLINE}                                                                          { yybegin(YYINITIAL); return Deb822Types.PARAGRAPH_FINISH; }
}

[^]                                              { return TokenType.BAD_CHARACTER; }
