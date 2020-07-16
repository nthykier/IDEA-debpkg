package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.dch.psi.DchTypes;
import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import junit.framework.TestCase;
import org.intellij.sdk.language.DchLexer;
import org.intellij.sdk.language.Deb822Lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DchLexerTest extends TestCase {

    public void testSimpleDeb822File() throws IOException {
        String content = "debhelper (13.2) unstable; urgency=medium\n"
                + "  \n"
                + "  [ Niels Thykier ]\n"
                + "  * Some change description\n"
                + "    Spanning Multiple-lines (Closes: #123456)\n"
                + "\n"
                + " -- Niels Thykier <niels@thykier.net>  Sun, 05 Jul 2020 21:14:04 +0000\n"
                ;
        DchLexer lexer = new DchLexer(null);
        List<IElementType> tokens = new ArrayList<>();
        IElementType token;
        List<IElementType> expected = Arrays.asList(
                /* Initial line */
                DchTypes.SOURCE_NAME, DchTypes.PARANTHESES_OPEN, DchTypes.VERSION, DchTypes.PARANTHESES_CLOSE,
                /* Continued */ DchTypes.DISTRIBUTION_NAME, DchTypes.SEMI_COLON, DchTypes.KEYVALUE_PAIR,
                /* Changes */
                DchTypes.CHANGE_RESPONSIBLE,
                DchTypes.CHANGE_DETAILS,
                DchTypes.CHANGE_DETAILS,
                /* Sign-off line*/
                DchTypes.SIGNOFF_STARTER, DchTypes.MAINTAINER_NAME,
                /* Continued */ DchTypes.LESS_THAN, DchTypes.MAINTAINER_EMAIL, DchTypes.GREATER_THAN,
                /* Continued */ DchTypes.DOUBLE_SPACE, DchTypes.SIGNOFF_DATE
        );
        lexer.reset(content, 0, content.length(), Deb822Lexer.YYINITIAL);
        while ( (token = lexer.advance()) != null) {
            CharSequence value = lexer.yytext();
            if (token == TokenType.WHITE_SPACE) {
                continue;
            }
            tokens.add(token);

            System.out.println(token + " \"" + value.toString().replace("\n", "\\n") + "\" (state: " + lexer.yystate() + ")");
        }
        assertEquals(expected, tokens);
    }
}
