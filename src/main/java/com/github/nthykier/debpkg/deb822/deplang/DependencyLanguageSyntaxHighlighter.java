package com.github.nthykier.debpkg.deb822.deplang;

import com.github.nthykier.debpkg.deb822.deplang.psi.DependencyLanguageTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DependencyLanguageSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("DEB822.DEP_LANG.BAD_CHARACTER", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);

    public static final TextAttributesKey SUBSTVARS =
            TextAttributesKey.createTextAttributesKey("DEB822.DEP_LANG.SUBSTVAR", DefaultLanguageHighlighterColors.LABEL);

    public static final TextAttributesKey COMMENT =
            TextAttributesKey.createTextAttributesKey("DEB822.DEP_LANG.COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);


    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    private static final Map<IElementType, TextAttributesKey[]> TOKEN_TYPE2TEXT_ATTR_KEYS = new HashMap<>();

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new DependencyLanguageLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return TOKEN_TYPE2TEXT_ATTR_KEYS.getOrDefault(tokenType, EMPTY_KEYS);
    }

    private static void addTokenMapEntry(IElementType tokenType, String externalName, TextAttributesKey fallbackAttributeKey) {
        TextAttributesKey attrKey = TextAttributesKey.createTextAttributesKey(externalName, fallbackAttributeKey);
        addTokenMapEntry(tokenType, attrKey);
    }

    private static void addTokenMapEntry(IElementType tokenType, TextAttributesKey attributesKey) {
        TOKEN_TYPE2TEXT_ATTR_KEYS.put(tokenType, new TextAttributesKey[]{attributesKey});
    }


    static {
        addTokenMapEntry(DependencyLanguageTypes.WORDISH, "DEB822.DEP_LANG.WORDISH", DefaultLanguageHighlighterColors.IDENTIFIER);
        addTokenMapEntry(DependencyLanguageTypes.COMMENT, COMMENT);
        addTokenMapEntry(DependencyLanguageTypes.SUBSTVAR_TOKEN, SUBSTVARS);

        addTokenMapEntry(TokenType.BAD_CHARACTER, BAD_CHARACTER);
    }
}
