package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.dch.psi.DchTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DchSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("DCH.BAD_CHARACTER", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);


    public static final TextAttributesKey FILE_NAME =
            TextAttributesKey.createTextAttributesKey("DCH.FILE_NAME", DefaultLanguageHighlighterColors.INSTANCE_FIELD);


    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    private static final Map<IElementType, TextAttributesKey[]> TOKEN_TYPE2TEXT_ATTR_KEYS = new HashMap<>();

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new DchLexerAdapter();
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
        addTokenMapEntry(DchTypes.SOURCE_NAME, "DCH.SOURCE_NAME", DefaultLanguageHighlighterColors.KEYWORD);
        addTokenMapEntry(DchTypes.VERSION, "DCH.VERSION", DefaultLanguageHighlighterColors.KEYWORD);
        addTokenMapEntry(DchTypes.PARANTHESES_OPEN, "DCH.PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
        addTokenMapEntry(DchTypes.PARANTHESES_CLOSE, "DCH.PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
        addTokenMapEntry(DchTypes.PARANTHESES_OPEN, "DCH.PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
        addTokenMapEntry(DchTypes.GREATER_THAN, "DCH.EMAIL_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
        addTokenMapEntry(DchTypes.LESS_THAN, "DCH.EMAIL_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
        addTokenMapEntry(DchTypes.DISTRIBUTION_NAME, "DCH.DISTRIBUTION", DefaultLanguageHighlighterColors.LABEL);
        addTokenMapEntry(DchTypes.CHANGE_RESPONSIBLE, "DCH.CHANGE_RESPONSIBLE", DefaultLanguageHighlighterColors.STRING);
        addTokenMapEntry(DchTypes.MAINTAINER_NAME, "DCH.MAINTAINER_NAME", DefaultLanguageHighlighterColors.IDENTIFIER);
        addTokenMapEntry(DchTypes.MAINTAINER_EMAIL, "DCH.MAINTAINER_EMAIL", DefaultLanguageHighlighterColors.IDENTIFIER);

        addTokenMapEntry(TokenType.BAD_CHARACTER, BAD_CHARACTER);
    }
}
