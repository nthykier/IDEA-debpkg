// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.language;

import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;

%%

%public
%class Deb822Lexer
%implements FlexLexer
%unicode
%{
    private final Deb822LanguageSupport languageSupport;
    private final KnownFieldTable knownFieldTable;


    private IElementType parsedFieldName() {
        Deb822KnownField knownField = knownFieldTable.getField(yytext().toString());
        Deb822KnownFieldValueType valueType = Deb822KnownFieldValueType.FREE_TEXT_VALUE;
        if (knownField != null && knownField.getCanonicalFieldName().equals("Build-Profiles")) {
            valueType = knownField.getFieldValueType();
        }
        yybegin(valueType.getInitialValueParsingLexerState());
        return Deb822Types.FIELD_NAME;
    }

    private void nextValueParsingState() {
        int newState;
        switch(zzLexicalState) {
            case SEPARATOR_BEFORE_VALUE:
            case MAYBE_CONT_VALUE:
                newState = PARSING_VALUE;
                break;
            case SEPARATOR_BEFORE_BUILD_PROFILES:
            case MAYBE_CONT_BUILD_PROFILES:
                newState = PARSING_BUILD_PROFILES;
                break;
            default:
                throw new IllegalStateException("The nextValueParsingState was called in the wrong context");
        }
        yybegin(newState);
    }
%}
%ctorarg Deb822LanguageSupport languageSupport
%init{
    this.languageSupport = languageSupport;
    this.knownFieldTable = languageSupport.getKnownFieldTable();
%init}
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
BUILD_PROFILE_TOKEN=[a-zA-Z0-9][a-zA-Z0-9.+\-~_]*
GPG_DASHES=-----
GPG_WHITESPACE=[ \t]
/* All characters in range 0x21 to 0x39 (incl.) + 0x3b to 0x7e (incl.) are valid, except for the
 * first character, where 0x23 (#) and 0x2D (-) are not permitted.
 */
FIRST_FIELD_CHARACTER=[\u0021\u0022\u0024-\u002c\u002e-\u0039\u003b-\u007e]
FIELD_CHARACTER=[\u0021-\u0039\u003b-\u007e]

// Use exclusive states - we do not want a value to unexpectedly be mistaken for a field name.
%xstate GPG_PARSE_ARMORED_HEADERS, GPG_BEGIN_SIGNATURE_ARMORED_HEADERS, GPG_SIGNATURE_BLOB
%xstate SEPARATOR_BEFORE_VALUE, MAYBE_CONT_VALUE, PARSING_VALUE
%xstate SEPARATOR_BEFORE_BUILD_PROFILES, MAYBE_CONT_BUILD_PROFILES, PARSING_BUILD_PROFILES

%%

<YYINITIAL>{
{END_OF_LINE_COMMENT}                                                                                                { return Deb822Types.COMMENT; }
^{FIRST_FIELD_CHARACTER}{FIELD_CHARACTER}*                                                                           { return parsedFieldName(); }
^{GPG_DASHES}BEGIN{SINGLE_SPACE}PGP{SINGLE_SPACE}SIGNED{SINGLE_SPACE}MESSAGE{GPG_DASHES}{GPG_WHITESPACE}*{NEWLINE}   { yybegin(GPG_PARSE_ARMORED_HEADERS); return Deb822Types.GPG_BEGIN_SIGNED_MESSAGE; }
^{GPG_DASHES}BEGIN{SINGLE_SPACE}PGP{SINGLE_SPACE}SIGNATURE{GPG_DASHES}{GPG_WHITESPACE}*{NEWLINE}                     { yybegin(GPG_BEGIN_SIGNATURE_ARMORED_HEADERS); return Deb822Types.GPG_BEGIN_SIGNATURE; }
^{WHITE_SPACE}+{NEWLINE}+                                                                                            { return TokenType.WHITE_SPACE; }
{NEWLINE}+                                                                                                           { return TokenType.WHITE_SPACE; }
[^]                                                                                                                  { return TokenType.BAD_CHARACTER; }
}

<GPG_PARSE_ARMORED_HEADERS>{
\S+: [^\n]*\n         { return Deb822Types.GPG_ARMOR_HEADER; }
[*\n]+\n              { return TokenType.BAD_CHARACTER; }
^{NEWLINE}            { yybegin(YYINITIAL); return Deb822Types.GPG_ARMOR_HEADERS_END; }
[^]                   { return TokenType.BAD_CHARACTER; }
}

<GPG_BEGIN_SIGNATURE_ARMORED_HEADERS>{
\S+: [^\n]*\n         { return Deb822Types.GPG_ARMOR_HEADER; }
[*\n]+\n              { return TokenType.BAD_CHARACTER; }
^{NEWLINE}            { yybegin(GPG_SIGNATURE_BLOB); return Deb822Types.GPG_ARMOR_HEADERS_END; }
[^]                   { return TokenType.BAD_CHARACTER; }
}

<GPG_SIGNATURE_BLOB>{
[a-zA-Z0-9=/+_\-]+                                                                                    { return Deb822Types.GPG_SIGNATURE_BLOB_PART; }
{WHITE_SPACE}*\n                                                                                      { return TokenType.WHITE_SPACE; }
^{GPG_DASHES}END{SINGLE_SPACE}PGP{SINGLE_SPACE}SIGNATURE{GPG_DASHES}{GPG_WHITESPACE}*{NEWLINE}        { yybegin(GPG_PARSE_ARMORED_HEADERS); return Deb822Types.GPG_END_SIGNATURE; }
[^]                                                                                                   { return TokenType.BAD_CHARACTER; }
}

<SEPARATOR_BEFORE_VALUE, SEPARATOR_BEFORE_BUILD_PROFILES>{
{WHITE_SPACE}+                                   { return TokenType.WHITE_SPACE; }
{SEPARATOR}                                      { nextValueParsingState(); return Deb822Types.SEPARATOR; }
[^]                                              { return TokenType.BAD_CHARACTER; }
}


<PARSING_VALUE>{
{NEWLINE}                                                        { yybegin(MAYBE_CONT_VALUE); return TokenType.WHITE_SPACE; }
{SUBSTVAR}                                                       { return Deb822Types.SUBSTVAR_TOKEN; }
[$][{][}]                                                        { return Deb822Types.SUBSTVAR_TOKEN; }
[$]                                                              { return Deb822Types.VALUE_TOKEN; }
[,]                                                              { return Deb822Types.COMMA; }
[^$ ,\r\n]+                                                      { return Deb822Types.VALUE_TOKEN; }
{WHITE_SPACE}+                                                   { return TokenType.WHITE_SPACE; }
[^]                                                              { return TokenType.BAD_CHARACTER; }
}

<PARSING_BUILD_PROFILES>{
{NEWLINE}                                                        { yybegin(MAYBE_CONT_BUILD_PROFILES); return TokenType.WHITE_SPACE; }
[<]                                                              { return Deb822Types.ANGLE_BRACKET_OPEN; }
[!]                                                              { return Deb822Types.NEGATION; }
{BUILD_PROFILE_TOKEN}                                            { return Deb822Types.BUILD_PROFILE_TOKEN; }
[>]                                                              { return Deb822Types.ANGLE_BRACKET_CLOSE; }
{WHITE_SPACE}+                                                   { return TokenType.WHITE_SPACE; }
[^]                                                              { return TokenType.BAD_CHARACTER; }
}

<MAYBE_CONT_VALUE, MAYBE_CONT_BUILD_PROFILES>{
{END_OF_LINE_COMMENT}{NEWLINE}                                                     { return Deb822Types.COMMENT; }
/* Cope with some common missing " ." patterns */
^{SINGLE_SPACE}+{NEWLINE}                                                          { return Deb822Types.HANGING_CONT_VALUE_TOKEN; }

^{SINGLE_SPACE}                                                                    { nextValueParsingState(); return TokenType.WHITE_SPACE; }
^{FIRST_FIELD_CHARACTER}{FIELD_CHARACTER}*                                         { return parsedFieldName(); }
{NEWLINE}                                                                          { yybegin(YYINITIAL); return Deb822Types.PARAGRAPH_FINISH; }
[^]                                                                                { return TokenType.BAD_CHARACTER; }
}

// Should not be used - but it is better to have it than to have the parser get stuck.
[^]                                              { return TokenType.BAD_CHARACTER; }
