// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.language;

import com.github.nthykier.debpkg.deb822.deplang.psi.DependencyLanguageTypes;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.intellij.spellchecker.tokenizer.Tokenizer;

%%

%public
%class DependencyLanguageLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

WORDISH=([0-9]+:)?[a-zA-Z0-9][a-zA-Z0-9.+\-~:_]*

WHITE_SPACE=[\ \t\f\n]
END_OF_LINE_COMMENT=[#][^\r\n]*
SUBSTVAR_NAME=[a-zA-Z0-9][a-zA-Z0-9\-:]*

%state DEPENDENCY

%%

{WHITE_SPACE}+                                  { return TokenType.WHITE_SPACE; }

[>][>=]                                         { return DependencyLanguageTypes.VERSION_OP; }
[<][<=]                                         { return DependencyLanguageTypes.VERSION_OP; }
/* Technically, == and != are wrong, but it gives better usability to accept it and then propose a fix in the annotator */
[=!]?[=]                                        { return DependencyLanguageTypes.VERSION_OP; }
[!]                                             { return DependencyLanguageTypes.NEGATION; }
[|]                                             { return DependencyLanguageTypes.OPERATOR_OR; }
[,]                                             { return DependencyLanguageTypes.OPERATOR_AND; }

[(]                                             { yybegin(DEPENDENCY); return DependencyLanguageTypes.PARANTHESES_OPEN; }
[)]                                             { yybegin(YYINITIAL); return DependencyLanguageTypes.PARANTHESES_CLOSE; }

[\[]                                            { return DependencyLanguageTypes.BRACKETS_OPEN; }
[\]]                                            { return DependencyLanguageTypes.BRACKETS_CLOSE; }

/*
 * We use difference tokens for <> based on context for the sake of the BraceMatcher
 * The "<" in "(<" should not have a closing brace (version OP) while "a <" should have (profile).
 */
<DEPENDENCY>{
    [<]                                         { return DependencyLanguageTypes.VERSION_OP; }
    [>]                                         { return DependencyLanguageTypes.VERSION_OP; }
}

<YYINITIAL>{
    [<]                                         { return DependencyLanguageTypes.ANGLE_BRACKET_OPEN; }
    [>]                                         { return DependencyLanguageTypes.ANGLE_BRACKET_CLOSE; }
}

/* Fake token */
[-][-]                                          { return DependencyLanguageTypes.DEPENDENCY_LANG_SEPARATOR; }

{WORDISH}                                       { return DependencyLanguageTypes.WORDISH; }

// Accept slightly invalid variants of the token to ease code completition
[$][{]?                                         { return DependencyLanguageTypes.SUBSTVAR_TOKEN; }
[$][{][}]                                       { return DependencyLanguageTypes.SUBSTVAR_TOKEN; }
[$][{]?{SUBSTVAR_NAME}[}]?                      { return DependencyLanguageTypes.SUBSTVAR_TOKEN; }

^{END_OF_LINE_COMMENT}                          { return DependencyLanguageTypes.COMMENT; }
{END_OF_LINE_COMMENT}                           { return DependencyLanguageTypes.MISPLACED_COMMENT; }

[^]                                             { return TokenType.BAD_CHARACTER; }
