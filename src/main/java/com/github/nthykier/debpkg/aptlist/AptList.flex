// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.language;

import com.github.nthykier.debpkg.aptlist.psi.AptListTypes;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.spellchecker.tokenizer.Tokenizer;

%%

%public
%class AptListLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

NEWLINE=\n
SPACE_CHAR=[ \t\r]
NAME_TOKEN=[a-zA-Z0-9+-.]+
BRACKET_OPEN=[\[]
BRACKET_CLOSE=[\]]
URI=\S+[:][/][/]\S+

%state OPTION_START, OPTION_VALUE

%%

<YYINITIAL>{
    deb(-src)?                 { return AptListTypes.TYPE_TOKEN; }
    {URI}                      { return AptListTypes.URI_TOKEN; }
    {NAME_TOKEN}+              { return AptListTypes.NAME_TOKEN; }
    ^[#][^\n]*\n?              { return AptListTypes.COMMENT; }
    {SPACE_CHAR}+              { return TokenType.WHITE_SPACE; }
}


<OPTION_START>{
    [=]                { yybegin(OPTION_VALUE); return AptListTypes.EQUALS_TOKEN; }
    {NAME_TOKEN}       { return AptListTypes.OPTION_KEY_TOKEN; }
    {SPACE_CHAR}+      { return TokenType.WHITE_SPACE; }
}

<OPTION_VALUE>{
    [,]                        { return AptListTypes.COMMA_TOKEN; }
    {NAME_TOKEN}               { return AptListTypes.OPTION_VALUE_TOKEN; }
    {SPACE_CHAR}+              { yybegin(OPTION_VALUE); return TokenType.WHITE_SPACE; }
}


{BRACKET_OPEN}             { yybegin(OPTION_START); return AptListTypes.BRACKET_OPEN; }
{BRACKET_CLOSE}            { yybegin(YYINITIAL); return AptListTypes.BRACKET_CLOSE; }
^{SPACE_CHAR}*{NEWLINE}    { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
{NEWLINE}                  { yybegin(YYINITIAL); return AptListTypes.NEWLINE_TOKEN; }
[^]                        { return TokenType.BAD_CHARACTER; }
