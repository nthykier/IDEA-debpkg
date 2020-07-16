package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import junit.framework.TestCase;
import org.intellij.sdk.language.Deb822Lexer;
import org.junit.Assert;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deb822LexerTest extends TestCase {

    public void testSimpleDeb822File() throws IOException {
        String content = "Source: foo\n"
                + "Section: bar\n"
                + "# Random comment\n"
                + "\n"
                + "Package: foo\n"
                + "Architecture: any\n"
                + "Depends: ${misc:Depends}, ${shlib:Depends}\n"
                + "# Random comment\n"
                + "Description:foo\n"
                + " Extended description\n"
                + " .\n"
                + "# inline comment in description\n"
                + " Following line\n"
                ;
        Deb822Lexer lexer = new Deb822Lexer(null);
        List<IElementType> tokens = new ArrayList<>();
        IElementType token;
        List<IElementType> expected = Arrays.asList(
                /* Field Source */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE,
                /* Field Section */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE,
                Deb822Types.COMMENT,
                Deb822Types.PARAGRAPH_FINISH,
                /* Field Package */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE,
                /* Field Architecture */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE,
                /* Field Depends */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.SUBSTVAR, Deb822Types.VALUE, Deb822Types.SUBSTVAR,
                Deb822Types.COMMENT,
                /* Field Description */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE, Deb822Types.VALUE,
                /* cont */ Deb822Types.VALUE, Deb822Types.COMMENT, Deb822Types.VALUE

                /*No Deb822Types.PARAGRAPH_FINISH; there is an EOF instead */
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
