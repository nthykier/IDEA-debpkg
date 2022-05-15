package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Deb822SyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("DEB822.BAD_CHARACTER", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);

    public static final TextAttributesKey VALUE_KEYWORD =
            TextAttributesKey.createTextAttributesKey("DEB822.VALUE_KEYWORD", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);

    public static final TextAttributesKey WILDCARD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("DEB822.WILDCARD_CHARACTER", DefaultLanguageHighlighterColors.LABEL);

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    private static final Map<IElementType, TextAttributesKey[]> TOKEN_TYPE2TEXT_ATTR_KEYS = new HashMap<>();

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new Deb822LexerAdapter(Deb822Language.INSTANCE);
    }

    @NotNull
    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
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
        addTokenMapEntry(Deb822Types.SEPARATOR, "DEB822.SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
        addTokenMapEntry(Deb822Types.SUBSTVAR_TOKEN, "DEB822.SUBSTVAR", DefaultLanguageHighlighterColors.LABEL);
        addTokenMapEntry(Deb822Types.COMMENT, "DEB822.COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
        addTokenMapEntry(Deb822Types.FIELD_NAME, "DEB822.FIELD", DefaultLanguageHighlighterColors.KEYWORD);
        addTokenMapEntry(Deb822Types.GPG_BEGIN_SIGNED_MESSAGE, "DEB822.GPG_MARKERS", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        addTokenMapEntry(Deb822Types.GPG_BEGIN_SIGNATURE, "DEB822.GPG_MARKERS", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        addTokenMapEntry(Deb822Types.GPG_END_SIGNATURE, "DEB822.GPG_MARKERS", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        addTokenMapEntry(Deb822Types.GPG_ARMOR_HEADER, "DEB822.GPG_SIGNATURE_BLOB", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        addTokenMapEntry(Deb822Types.GPG_SIGNATURE_BLOB_PART, "DEB822.GPG_SIGNATURE_BLOB", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

        addTokenMapEntry(TokenType.BAD_CHARACTER, BAD_CHARACTER);
    }
}
