package com.github.nthykier.debpkg.aptlist;

import com.github.nthykier.debpkg.aptlist.psi.AptListTypes;
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

public class AptListSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("APT_LIST.BAD_CHARACTER", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);


    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    private static final Map<IElementType, TextAttributesKey[]> TOKEN_TYPE2TEXT_ATTR_KEYS = new HashMap<>();

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new AptListLexerAdapter();
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
        addTokenMapEntry(AptListTypes.TYPE_TOKEN, "APT_LIST.TYPE", DefaultLanguageHighlighterColors.KEYWORD);
        addTokenMapEntry(AptListTypes.NAME_TOKEN, "APT_LIST.NAME_TOKEN", DefaultLanguageHighlighterColors.IDENTIFIER);
        addTokenMapEntry(AptListTypes.OPTION_KEY_TOKEN, "APT_LIST.OPTION_KEY_TOKEN", DefaultLanguageHighlighterColors.METADATA);
        addTokenMapEntry(AptListTypes.OPTION_VALUE_TOKEN, "APT_LIST.OPTION_VALUE_TOKEN", DefaultLanguageHighlighterColors.METADATA);
        addTokenMapEntry(AptListTypes.BRACKET_OPEN, "APT_LIST.BRACKET_OPEN", DefaultLanguageHighlighterColors.PARENTHESES);
        addTokenMapEntry(AptListTypes.BRACKET_CLOSE, "APT_LIST.BRACKET_CLOSE", DefaultLanguageHighlighterColors.PARENTHESES);
        addTokenMapEntry(AptListTypes.URI_TOKEN, "APT_LIST.URI", DefaultLanguageHighlighterColors.STRING);
        addTokenMapEntry(AptListTypes.EQUALS_TOKEN, "APT_LIST.EQUALS_TOKEN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
        addTokenMapEntry(AptListTypes.COMMA_TOKEN, "APT_LIST.COMMA_TOKEN", DefaultLanguageHighlighterColors.COMMA);
        addTokenMapEntry(AptListTypes.COMMENT, "APT_LIST.COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

        addTokenMapEntry(TokenType.BAD_CHARACTER, BAD_CHARACTER);
    }
}
