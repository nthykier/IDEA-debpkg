package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import junit.framework.TestCase;
import org.intellij.sdk.language.Deb822Lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deb822LexerTest extends TestCase {

    private void runLexerText(Deb822Lexer lexer, String content, List<IElementType> expected) throws IOException  {
        List<IElementType> tokens = new ArrayList<>();
        IElementType token;
        int tokenCount = 0;
        boolean firstDiff = true;
        lexer.reset(content, 0, content.length(), Deb822Lexer.YYINITIAL);
        while ( (token = lexer.advance()) != null) {
            CharSequence value = lexer.yytext();
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
                assertEquals(token, expectedToken);
            } else if (token != expectedToken && firstDiff) {
                System.out.println(" >> Expected " + expectedToken + " <<");
                System.out.println("   --- END DIFF ---");
                firstDiff = false;
            }
        }
        assertEquals(expected, tokens);
    }

    public void testSimpleDeb822File() throws IOException {
        String content = "Source: foo\n"
                + "Section: bar\n"
                + "# Random comment\n"
                + "\n"
                + "Package: foo\n"
                + "Architecture: any\n"
                + "Depends: ${misc:Depends}, ${shlib:Depends}\n"
                + "# Random comment\n"
                + "Recommends:\n"
                + " foo,\n"
                + " bar,\n"
                + "Description:foo\n"
                + " Extended description\n"
                + " .\n"
                + "# inline comment in description\n"
                + " Following line\n"
                ;
        Deb822Lexer lexer = new Deb822Lexer(null);
        List<IElementType> expected = Arrays.asList(
                /* Field Source */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                /* Field Section */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                Deb822Types.COMMENT,
                Deb822Types.PARAGRAPH_FINISH,
                /* Field Package */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                /* Field Architecture */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                /* Field Depends */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.SUBSTVAR_TOKEN, Deb822Types.COMMA, Deb822Types.SUBSTVAR_TOKEN,
                Deb822Types.COMMENT,
                /* Field Recommends */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN, Deb822Types.COMMA, Deb822Types.VALUE_TOKEN, Deb822Types.COMMA,
                /* Field Description */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.COMMENT, Deb822Types.VALUE_TOKEN,
                /* cont */ Deb822Types.VALUE_TOKEN

                /*No Deb822Types.PARAGRAPH_FINISH; there is an EOF instead */
        );
        runLexerText(lexer, content, expected);
    }

    public void testSimpleDscFile() throws IOException {
        String content = "-----BEGIN PGP SIGNED MESSAGE-----\n" +
                "Hash: SHA512\n" +
                "\n" +
                "Format: 3.0 (native)\n" +
                "Source: debhelper\n" +
                "Binary: debhelper, libdebhelper-perl, dh-systemd\n" +
                "Architecture: all\n" +
                "Version: 13.2.1\n" +
                /*  --- OMITTED FOR SIMPLICITY ---
                "Maintainer: Debhelper Maintainers <debhelper@packages.debian.org>\n" +
                "Uploaders: Niels Thykier <niels@thykier.net>,\n" +
                "Standards-Version: 4.5.0\n" +
                "Vcs-Browser: https://salsa.debian.org/debian/debhelper\n" +
                "Vcs-Git: https://salsa.debian.org/debian/debhelper.git\n" +
                "Testsuite: autopkgtest-pkg-perl\n" +
                "Build-Depends: dpkg-dev (>= 1.18.0~), perl:any, po4a, man-db <pkg.debhelper.ci>, libtest-pod-perl <pkg.debhelper.ci>\n" +
                */
                "Package-List:\n" +
                " debhelper deb devel optional arch=all\n" +
                " dh-systemd deb oldlibs optional arch=all\n" +
                " libdebhelper-perl deb perl optional arch=all\n" +
                "Checksums-Sha1:\n" +
                " 3cc7d0220cc05657f138880a3a500c398f983d39 541428 debhelper_13.2.1.tar.xz\n" +
                "Checksums-Sha256:\n" +
                " 3f2f4e085ab8f3389f6e8bf1610bc196c363cd7d08d15c791c04451490868eba 541428 debhelper_13.2.1.tar.xz\n" +
                "Files:\n" +
                " 3bded8140c6c290c555e43722cd03f3b 541428 debhelper_13.2.1.tar.xz\n" +
                "\n" +
                "-----BEGIN PGP SIGNATURE-----\n" +
                "\n" +
                "iQJGBAEBCgAwFiEE8f9dDX4ALfD+VfsMplt42+Z8eqwFAl9dMWgSHG5pZWxzQHRo\n" +
                "eWtpZXIubmV0AAoJEKZbeNvmfHqsxX4QAJkzp8B7ZddNzGUFnnM3IFgQrXFtPM8j\n" +
                "tQvbjTu+ohdtpDcGsaqROdjttWondxK7gfPQNoXB617qns1RQGw20aaA8/KyhDRw\n" +
                "Ies1XUVhql1f8H+rm3IWiozaoP0C+CILFJt279Z610djC5P7PNigJRvtRCQRx0R6\n" +
                "qb4XwC6o8SOKix7wXT/RAsiHNkM6u8UekcluohWe5+4Rt6yvey0gBI6FR5qxHU/R\n" +
                "qhSZdk70i/YyMDSLRF5LlJGyPGKhfE/3YMIeIcqmxD7epVPuY7bCkhmdZD21FSID\n" +
                "bSJ+0nadVaZ1qiG4/MRbvXpb3c5twxlbDWKdlbwPnPgHCvlAI1Pguo76YY0aq9wx\n" +
                "8WfEPGctGPVkg7KOrGOZhKzBXN4feXkxQ4cVHq+2O+SvOpo1XtgxtJDroQ1hReRN\n" +
                "nBiCxGf0jzCPXIoOI8eKSlZEMxjIz/ssFM2obwzMRvw4/owMJhBbDzRfReJVN7ry\n" +
                "/DwrvjC47gqg1PmgFFcdl6kynY5+7FuRyu7rdvT3UX7/j/xOLg2FUO4MyU+OHlA6\n" +
                "Ak1webjy8Po4pQ9ApGYPEMzBVh1fCbI8L2oOra++C4bF18AAeHeKsw/5D1O2wPt1\n" +
                "JrW5VY6cXZFb9AEmk1eRSMYbTaDo/9iC+y/R1/SVZ2WZqmWCCQb2O+6lfGwRU8f3\n" +
                "Yqxk/bSK4m8g\n" +
                "=3gec\n" +
                "-----END PGP SIGNATURE-----\n"
                ;
        Deb822Lexer lexer = new Deb822Lexer(null);
        List<IElementType> expected = Arrays.asList(
                Deb822Types.GPG_BEGIN_SIGNED_MESSAGE,
                Deb822Types.GPG_ARMOR_HEADER,
                Deb822Types.GPG_ARMOR_HEADERS_END,
                /* Field Format */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,
                /* Field Source */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                /* Field Binary */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN, Deb822Types.COMMA,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.COMMA, Deb822Types.VALUE_TOKEN,
                /* Field Architecture */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                /* Field Version */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,

                /* Skipped some fields */

                /* Field Package-List */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,

                /* Field Checksums-Sha1 */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,
                /* Field Checksums-Sha256 */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,
                /* Field Files */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,
                Deb822Types.PARAGRAPH_FINISH,


                Deb822Types.GPG_BEGIN_SIGNATURE,
                Deb822Types.GPG_ARMOR_HEADERS_END,
                Deb822Types.GPG_SIGNATURE_BLOB_PART, Deb822Types.GPG_SIGNATURE_BLOB_PART, Deb822Types.GPG_SIGNATURE_BLOB_PART,
                Deb822Types.GPG_SIGNATURE_BLOB_PART, Deb822Types.GPG_SIGNATURE_BLOB_PART, Deb822Types.GPG_SIGNATURE_BLOB_PART,
                Deb822Types.GPG_SIGNATURE_BLOB_PART, Deb822Types.GPG_SIGNATURE_BLOB_PART, Deb822Types.GPG_SIGNATURE_BLOB_PART,
                Deb822Types.GPG_SIGNATURE_BLOB_PART, Deb822Types.GPG_SIGNATURE_BLOB_PART, Deb822Types.GPG_SIGNATURE_BLOB_PART,
                Deb822Types.GPG_SIGNATURE_BLOB_PART, Deb822Types.GPG_SIGNATURE_BLOB_PART,
                Deb822Types.GPG_END_SIGNATURE

        );
        runLexerText(lexer, content, expected);
    }

    public void testHangingContLine() throws IOException {
        String content = "Package: dh-systemd\n" +
                "Section: oldlibs\n" +
                "Architecture: all\n" +
                "Multi-Arch: foreign\n" +
                "Depends: debhelper (>= 9.20160709),\n" +
                "  \n" + /* <-- invalid and should be parsed as a hanging continuation value */
                "         ${misc:Depends},\n" +
                "Description: debhelper add-on ...\n" +
                " This package ...\n";
        Deb822Lexer lexer = new Deb822Lexer(null);
        List<IElementType> expected = Arrays.asList(
                /* Field Package */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                /* Field Section */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                /* Field Architecture */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                /* Field Multi-Arch */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN,
                /* Field Depends */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.COMMA,
                /* cont */ Deb822Types.HANGING_CONT_VALUE_TOKEN, Deb822Types.SUBSTVAR_TOKEN, Deb822Types.COMMA,
                /* Field Description */
                Deb822Types.FIELD_NAME, Deb822Types.SEPARATOR, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN,
                /* cont */ Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN, Deb822Types.VALUE_TOKEN
        );
        runLexerText(lexer, content, expected);
    }
}
