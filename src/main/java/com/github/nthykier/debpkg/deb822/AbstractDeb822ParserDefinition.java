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
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDeb822ParserDefinition implements ParserDefinition {

    public static final TokenSet COMMENTS = TokenSet.create(Deb822Types.COMMENT);

    private final LanguageFileType fileType;
    private final IFileElementType fileElementType;

    protected AbstractDeb822ParserDefinition(LanguageFileType fileType) {
        this.fileType = fileType;
        this.fileElementType = new IFileElementType(fileType.getLanguage());
    }

    @Override
    public @NotNull Lexer createLexer(Project project) {
        assert this.fileType.getLanguage() instanceof Deb822LanguageSupport;
        return new Deb822LexerAdapter((Deb822LanguageSupport) this.fileType.getLanguage());
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new Deb822Parser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
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
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new Deb822File(viewProvider, this.fileType);
    }
}
