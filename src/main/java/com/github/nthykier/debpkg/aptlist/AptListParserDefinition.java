package com.github.nthykier.debpkg.aptlist;

import com.github.nthykier.debpkg.aptlist.parser.AptListParser;
import com.github.nthykier.debpkg.aptlist.psi.AptListTypes;
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

public class AptListParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE_ELEMENT_TYPE = new IFileElementType(AptListLanguage.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new AptListLexerAdapter();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new AptListParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE_ELEMENT_TYPE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return TokenSet.create(AptListTypes.COMMENT);
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return AptListTypes.Factory.createElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new AptListFile(viewProvider);
    }
}
