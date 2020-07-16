package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.dch.parser.DchParser;
import com.github.nthykier.debpkg.dch.psi.DchFile;
import com.github.nthykier.debpkg.dch.psi.DchTypes;
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

public class DchParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE_ELEMENT_TYPE = new IFileElementType(DchLanguage.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new DchLexerAdapter();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new DchParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE_ELEMENT_TYPE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return DchTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new DchFile(viewProvider);
    }
}
