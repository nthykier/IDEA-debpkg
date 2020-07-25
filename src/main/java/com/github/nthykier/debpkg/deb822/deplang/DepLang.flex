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

WORDISH=[a-zA-Z0-9][a-zA-Z0-9.+\-~:_]*

WHITE_SPACE=[\ \t\f\n]
END_OF_LINE_COMMENT=[#][^\r\n]*
SUBSTVAR_NAME=[a-zA-Z0-9][a-zA-Z0-9\-:]*

%%

{WHITE_SPACE}+                                  { return TokenType.WHITE_SPACE; }

[>][>=]                                         { return DependencyLanguageTypes.VERSION_OP; }
[<][<=]                                         { return DependencyLanguageTypes.VERSION_OP; }
/* Technically, == and != are wrong, but it gives better usability to accept it and then propose a fix in the annotator */
[=!]?[=]                                        { return DependencyLanguageTypes.VERSION_OP; }
[!]                                             { return DependencyLanguageTypes.NEGATION; }
[|]                                             { return DependencyLanguageTypes.OPERATOR_OR; }
[,]                                             { return DependencyLanguageTypes.OPERATOR_AND; }

[(]                                             { return DependencyLanguageTypes.PARANTHESES_OPEN; }
[)]                                             { return DependencyLanguageTypes.PARANTHESES_CLOSE; }

[\[]                                            { return DependencyLanguageTypes.BRACKETS_OPEN; }
[\]]                                            { return DependencyLanguageTypes.BRACKETS_CLOSE; }

[<]                                             { return DependencyLanguageTypes.LESS_THAN; }
[>]                                             { return DependencyLanguageTypes.GREATER_THAN; }

{WORDISH}                                       { return DependencyLanguageTypes.WORDISH; }
// Same as the above, except covers epoch
[0-9]+:{WORDISH}                                { return DependencyLanguageTypes.WORDISH; }

// Accept slightly invalid variants of the token to ease code completition
[$][{]?                                         { return DependencyLanguageTypes.SUBSTVAR_TOKEN; }
[$][{][}]?                                      { return DependencyLanguageTypes.SUBSTVAR_TOKEN; }
[$][{]?{SUBSTVAR_NAME}[}]?                      { return DependencyLanguageTypes.SUBSTVAR_TOKEN; }

^{END_OF_LINE_COMMENT}                          { return DependencyLanguageTypes.COMMENT; }
{END_OF_LINE_COMMENT}                           { return DependencyLanguageTypes.MISPLACED_COMMENT; }

[^]                                             { return TokenType.BAD_CHARACTER; }
