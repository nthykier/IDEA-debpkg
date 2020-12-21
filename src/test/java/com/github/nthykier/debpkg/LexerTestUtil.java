package com.github.nthykier.debpkg;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import junit.framework.TestCase;
import org.intellij.sdk.language.Deb822Lexer;
import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexerTestUtil {

    public static void runLexerText(FlexLexer lexer, int intialState, String content, List<IElementType> expected) throws IOException {
        List<IElementType> tokens = new ArrayList<>(expected.size());
        IElementType token;
        int tokenCount = 0;
        boolean firstDiff = true;
        lexer.reset(content, 0, content.length(), intialState);
        while ((token = lexer.advance()) != null) {
            final int tokenStart = lexer.getTokenStart();
            final int tokenEnd = lexer.getTokenEnd();
            CharSequence value = content.substring(tokenStart, tokenEnd);
            IElementType expectedToken = null;
            if (token == TokenType.WHITE_SPACE) {
                continue;
            }
            tokens.add(token);
            if (tokenCount < expected.size()) {
                expectedToken = expected.get(tokenCount);
            }

            if (token != expectedToken && firstDiff) {
                System.out.println("   --- DIFF ---");
            }

            System.out.println(token + " \"" + value.toString().replace("\n", "\\n") + "\" (state: " + lexer.yystate() + ")");


            tokenCount++;

            if (token == TokenType.BAD_CHARACTER) {
                // Abort early on unexpected BAD_CHARACTER tokens as they tend to flood output
                Assert.assertEquals(token, expectedToken);
            } else if (token != expectedToken && firstDiff) {
                System.out.println(" >> Expected " + expectedToken + " <<");
                System.out.println("   --- END DIFF ---");
                firstDiff = false;
            }
        }
        Assert.assertEquals(expected, tokens);
    }
}
