package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.parser.Deb822Parser;
import com.github.nthykier.debpkg.deb822.psi.Deb822File;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
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
import org.intellij.sdk.language.Deb822LexerAdapter;
import org.jetbrains.annotations.NotNull;

public class Deb822ParserDefinition implements ParserDefinition {

    public static final TokenSet COMMENTS = TokenSet.create(Deb822Types.COMMENT);

    public static final IFileElementType FILE_ELEMENT_TYPE = new IFileElementType(Deb822Language.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new Deb822LexerAdapter();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new Deb822Parser();
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
        return Deb822Types.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new Deb822File(viewProvider);
    }
}
