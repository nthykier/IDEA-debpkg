package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.parser.Deb822Parser;
import com.github.nthykier.debpkg.deb822.psi.Deb822File;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDeb822ParserDefinition implements ParserDefinition {

    public static final TokenSet COMMENTS = TokenSet.create(Deb822Types.COMMENT);

    private final Language language;
    private final FileType fileType;
    private final IFileElementType fileElementType;

    protected AbstractDeb822ParserDefinition(Language language, FileType fileType) {
        this.language = language;
        this.fileType = fileType;
        this.fileElementType = new IFileElementType(language);
    }

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
        return this.fileElementType;
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
        return new Deb822File(viewProvider, this.language, this.fileType);
    }
}
