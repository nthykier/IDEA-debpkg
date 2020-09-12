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
WHITE_SPACE={SPACE_CHAR}
SOURCE_NAME=[a-z0-9][a-z0-9+-.]+
VERSION=([0-9]*[:])?[0-9][0-9a-zA-Z.+~-]*
SEMI_COLON=[;]
DASH=[\-]

%state ENTRY_STARTER_EXPECTING_VERSION ENTRY_STARTER_EXPECTING_DISTS ENTRY_STARTER_EXPECTING_KWPAIRS SIGNOFF_EXPECTING_MAINTAINER SIGNOFF_EXPECTING_EMAIL SIGNOFF_EXPECTING_DATE CHANGE_ENTRY START_OF_SOMETHING

%%

<YYINITIAL>{
{SOURCE_NAME}                                  { yybegin(ENTRY_STARTER_EXPECTING_VERSION); return DchTypes.SOURCE_NAME; }
{SPACE_CHAR}                                   { yybegin(START_OF_SOMETHING); return TokenType.WHITE_SPACE; }
}

<START_OF_SOMETHING>{
{SPACE_CHAR}                                    { yybegin(CHANGE_ENTRY); return TokenType.WHITE_SPACE; }
{DASH}{DASH}                                    { yybegin(SIGNOFF_EXPECTING_MAINTAINER); return DchTypes.SIGNOFF_STARTER; }
}

<ENTRY_STARTER_EXPECTING_VERSION>{
[(]                                              { return DchTypes.PARANTHESES_OPEN; }
{VERSION}                                        { return DchTypes.VERSION; }
[)]                                              { yybegin(ENTRY_STARTER_EXPECTING_DISTS); return DchTypes.PARANTHESES_CLOSE; }
{WHITE_SPACE}+                                   { return TokenType.WHITE_SPACE; }
}

<ENTRY_STARTER_EXPECTING_DISTS>{
[^ ;]+                                           { return DchTypes.DISTRIBUTION_NAME; }
{WHITE_SPACE}+                                   { return TokenType.WHITE_SPACE; }
{SEMI_COLON}                                     { yybegin(ENTRY_STARTER_EXPECTING_KWPAIRS); return DchTypes.SEMI_COLON; }
}

<ENTRY_STARTER_EXPECTING_KWPAIRS>{
{WHITE_SPACE}+                                                   { return TokenType.WHITE_SPACE; }
/* FIXME: Parse Key value pairs*/
\S([^\n,]*\S)?                                                   { return DchTypes.KEYVALUE_PAIR; }
}

<SIGNOFF_EXPECTING_MAINTAINER>{
{WHITE_SPACE}+                                   { return TokenType.WHITE_SPACE; }
[^< \n][^<\n]*                                   { yybegin(SIGNOFF_EXPECTING_EMAIL); return DchTypes.MAINTAINER_NAME; }
}

<CHANGE_ENTRY>{
\[[^\]\n]+\]{WHITE_SPACE}*                       { return DchTypes.CHANGE_RESPONSIBLE; }
[^\n]+                                           { return DchTypes.CHANGE_DETAILS; }
}

<SIGNOFF_EXPECTING_EMAIL>{
[<]                                              { return DchTypes.LESS_THAN; }
[^<> \n][^ \n<>]*                                { return DchTypes.MAINTAINER_EMAIL; }
[>]                                              { yybegin(SIGNOFF_EXPECTING_DATE); return DchTypes.GREATER_THAN; }
}

<SIGNOFF_EXPECTING_DATE>{
{SPACE_CHAR}{SPACE_CHAR}                                         { return DchTypes.DOUBLE_SPACE; }
\S+,[ ][ 0-9]\d{1}[ ]\S+[ ] \d{4}[ ]\d{2}:\d{2}:\d{2}[ ][+\-]\d{4}  { return DchTypes.SIGNOFF_DATE; }
}

{NEWLINE}                                        { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

[^]                                              { return TokenType.BAD_CHARACTER; }

