// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.language;

import com.github.nthykier.debpkg.dch.psi.DchFile;import com.github.nthykier.debpkg.dch.psi.DchTypes;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;import com.intellij.psi.tree.TokenSet;import com.intellij.spellchecker.tokenizer.Tokenizer;

%%

%public
%class DchLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

NEWLINE=\n
SPACE_CHAR=[ ]
SOURCE_NAME=[a-z0-9][a-z0-9+-.]+
VERSION=([0-9]*[:])?[0-9][0-9a-zA-Z.+~-]*
SEMI_COLON=[;]
DASH=[\-]

%state ENTRY_STARTER_EXPECTING_VERSION ENTRY_STARTER_EXPECTING_DISTS ENTRY_STARTER_EXPECTING_KWPAIRS SIGNOFF_EXPECTING_MAINTAINER SIGNOFF_EXPECTING_EMAIL SIGNOFF_EXPECTING_DATE CHANGE_ENTRY START_OF_SOMETHING CHANGE_ENTRY_AFTER_BULLET

%%

<YYINITIAL>{
({SPACE_CHAR}*{NEWLINE})*{SOURCE_NAME}                   { yybegin(ENTRY_STARTER_EXPECTING_VERSION); return DchTypes.SOURCE_NAME; }
({SPACE_CHAR}*{NEWLINE})*{SPACE_CHAR}                    { yybegin(START_OF_SOMETHING); return TokenType.WHITE_SPACE; }
({SPACE_CHAR}*{NEWLINE})*{SPACE_CHAR}{SPACE_CHAR}        { yybegin(CHANGE_ENTRY); return TokenType.WHITE_SPACE; }
^({SPACE_CHAR}*{NEWLINE})+                               { return TokenType.WHITE_SPACE; }
}

<START_OF_SOMETHING>{
{SPACE_CHAR}                                    { yybegin(CHANGE_ENTRY); return TokenType.WHITE_SPACE; }
{DASH}{DASH}                                    { yybegin(SIGNOFF_EXPECTING_MAINTAINER); return DchTypes.SIGNOFF_STARTER; }
}

<ENTRY_STARTER_EXPECTING_VERSION>{
[(]                                              { return DchTypes.PARANTHESES_OPEN; }
{VERSION}                                        { return DchTypes.VERSION_TOKEN; }
[)]                                              { yybegin(ENTRY_STARTER_EXPECTING_DISTS); return DchTypes.PARANTHESES_CLOSE; }
{SPACE_CHAR}+                                    { return TokenType.WHITE_SPACE; }
}

<ENTRY_STARTER_EXPECTING_DISTS>{
[^ ;]+                                           { return DchTypes.DISTRIBUTION_NAME; }
{SPACE_CHAR}+                                    { return TokenType.WHITE_SPACE; }
{SEMI_COLON}                                     { yybegin(ENTRY_STARTER_EXPECTING_KWPAIRS); return DchTypes.SEMI_COLON; }
}

<ENTRY_STARTER_EXPECTING_KWPAIRS>{
{SPACE_CHAR}+                                                    { return TokenType.WHITE_SPACE; }
/* FIXME: Parse Key value pairs*/
\S([^\n,]*\S)?                                                   { return DchTypes.KEYVALUE_PAIR; }
}

<SIGNOFF_EXPECTING_MAINTAINER>{
{SPACE_CHAR}+                                    { return TokenType.WHITE_SPACE; }
[^< \n][^<\n]*                                   { yybegin(SIGNOFF_EXPECTING_EMAIL); return DchTypes.MAINTAINER_NAME; }
}

<CHANGE_ENTRY>{
\[[^\]\n]+\]:?{SPACE_CHAR}*                      { return DchTypes.CHANGE_RESPONSIBLE; }
[*]                                              { yybegin(CHANGE_ENTRY_AFTER_BULLET); return DchTypes.CHANGE_BULLET_POINT; }
[^*\n][^\n]*                                     { return DchTypes.CHANGE_DETAILS; }
}

<CHANGE_ENTRY_AFTER_BULLET>{
[^\n]+                                           { return DchTypes.CHANGE_DETAILS; }
}

<SIGNOFF_EXPECTING_EMAIL>{
[<]                                              { return DchTypes.LESS_THAN; }
[^<> \n][^ \n<>]*                                { return DchTypes.MAINTAINER_EMAIL; }
[>]                                              { yybegin(SIGNOFF_EXPECTING_DATE); return DchTypes.GREATER_THAN; }
}

<SIGNOFF_EXPECTING_DATE>{
{SPACE_CHAR}{SPACE_CHAR}                                    { return DchTypes.DOUBLE_SPACE; }
\S(.*\S)?                                                   { return DchTypes.SIGNOFF_DATE_TOKEN; }
{SPACE_CHAR}+                                               { return TokenType.WHITE_SPACE; }
}

{NEWLINE}({SPACE_CHAR}*{NEWLINE})*                          { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
// Special-cases to reduce the number of whitespace tokens,
// which in turn reduces memory usage.
{NEWLINE}({SPACE_CHAR}*{NEWLINE})*{SPACE_CHAR}{SPACE_CHAR}  { yybegin(CHANGE_ENTRY); return TokenType.WHITE_SPACE; }
{NEWLINE}({SPACE_CHAR}*{NEWLINE})*{SPACE_CHAR}              { yybegin(START_OF_SOMETHING); return TokenType.WHITE_SPACE; }

[^]                                                         { return TokenType.BAD_CHARACTER; }
