package com.github.nthykier.debpkg.deb822.deplang;

import com.github.nthykier.debpkg.deb822.deplang.parser.DependencyLanguageParser;
import com.github.nthykier.debpkg.deb822.deplang.psi.DependencyLanguageTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class DependencyLanguageParserDefinition implements ParserDefinition {

    public static final TokenSet COMMENTS = TokenSet.create(DependencyLanguageTypes.COMMENT);
    private final static IFileElementType FILE_ELEMENT_TYPE = new IFileElementType(DependencyLanguage.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new DependencyLanguageLexerAdapter();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new DependencyLanguageParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE_ELEMENT_TYPE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return DependencyLanguageTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new DepLangFile(viewProvider, DependencyLanguage.INSTANCE, DependencyLanguageFileType.INSTANCE);
    }
}
