package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.LexerTestUtil;
import com.github.nthykier.debpkg.dch.psi.DchTypes;
import com.intellij.psi.tree.IElementType;
import junit.framework.TestCase;
import org.intellij.sdk.language.DchLexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DchLexerTest extends TestCase {

    public void testSimpleDchFile() throws IOException {
        String content = "debhelper (13.2) UNRELEASED; urgency=medium\n"
                + "  \n"
                + "  [ Niels Thykier ]\n"
                + "  * Some change description\n"
                + "    Spanning Multiple-lines (Closes: #123456)\n"
                + "  * A change that only took a single line.\n"
                + "  *Missing space, but should still work\n"
                + "  And this should be associated with the line above\n"
                + "  * Another single line change.\n"
                + "\n"
                + " -- Niels Thykier <niels@thykier.net>  Sun, 05 Jul 2020 21:14:04 +0000\n"
                + "\n" /* Check non-native versions */
                + "debhelper (13.1-1) unstable; urgency=medium\n"
                + "  \n"
                + "  * Some change description\n"
                + "    Spanning Multiple-lines (Closes: #123455)\n"
                + "\n"
                + " -- Niels Thykier <niels@thykier.net>  Sat, 04 Jul 2020 21:14:04 +0000\n"
                ;
        DchLexer lexer = new DchLexer(null);
        List<IElementType> expected = Arrays.asList(
                /* Initial line */
                DchTypes.SOURCE_NAME, DchTypes.PARANTHESES_OPEN, DchTypes.VERSION_TOKEN, DchTypes.PARANTHESES_CLOSE,
                /* Continued */ DchTypes.DISTRIBUTION_NAME, DchTypes.SEMI_COLON, DchTypes.KEYVALUE_PAIR,
                /* Changes */
                DchTypes.CHANGE_RESPONSIBLE,
                DchTypes.CHANGE_BULLET_POINT, DchTypes.CHANGE_DETAILS, DchTypes.CHANGE_DETAILS,
                DchTypes.CHANGE_BULLET_POINT, DchTypes.CHANGE_DETAILS,
                DchTypes.CHANGE_BULLET_POINT, DchTypes.CHANGE_DETAILS, DchTypes.CHANGE_DETAILS,
                DchTypes.CHANGE_BULLET_POINT, DchTypes.CHANGE_DETAILS,
                /* Sign-off line*/
                DchTypes.SIGNOFF_STARTER, DchTypes.MAINTAINER_NAME,
                /* Continued */ DchTypes.LESS_THAN, DchTypes.MAINTAINER_EMAIL, DchTypes.GREATER_THAN,
                /* Continued */ DchTypes.DOUBLE_SPACE, DchTypes.SIGNOFF_DATE_TOKEN,
                /* Initial line of second entry */
                DchTypes.SOURCE_NAME, DchTypes.PARANTHESES_OPEN, DchTypes.VERSION_TOKEN, DchTypes.PARANTHESES_CLOSE,
                /* Continued */ DchTypes.DISTRIBUTION_NAME, DchTypes.SEMI_COLON, DchTypes.KEYVALUE_PAIR,
                /* Changes */
                DchTypes.CHANGE_BULLET_POINT, DchTypes.CHANGE_DETAILS, DchTypes.CHANGE_DETAILS,
                /* Sign-off line*/
                DchTypes.SIGNOFF_STARTER, DchTypes.MAINTAINER_NAME,
                /* Continued */ DchTypes.LESS_THAN, DchTypes.MAINTAINER_EMAIL, DchTypes.GREATER_THAN,
                /* Continued */ DchTypes.DOUBLE_SPACE, DchTypes.SIGNOFF_DATE_TOKEN
        );
        LexerTestUtil.runLexerText(lexer, DchLexer.YYINITIAL, content, expected);
    }
}
