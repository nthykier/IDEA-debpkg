package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.dch.psi.DchTypes;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.inspections.BaseSplitter;
import com.intellij.spellchecker.inspections.PlainTextSplitter;
import com.intellij.spellchecker.inspections.Splitter;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import com.intellij.spellchecker.tokenizer.TokenizerBase;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class DchSpellcheckingStrategy extends SpellcheckingStrategy {

    private static final Tokenizer<?> CHANGELOG_TOKENIZER = new TokenizerBase<>(new ChangelogSplitter());
    private static final Pattern IGNORE_PATTERN = Pattern.compile(
            "(?: (?:override_|execute_(?:after|before)_)?dh_ | dh-sequence- | dpkg-) \\S++" /* commands */
                   + " | \\S+ [.] (?:[ch](?:pp)?|in|p[ylm]|sh|txt) (?:\\s|[.,:;\")]|$)" /* common file extensions*/
                   + " | \\s -- \\S+"  /* long command line options */
                      /* e.g. -DCMAKE_INSTALL_LIBDIR or ${DEB_HOST_MULTIARCH} */
                   + " | (?:--?|[$][{]?)? ([A-Z]{3,}+([_] [A-Z]++)*+ | [A-Z]++([_] [A-Z]++)++) (?:=\\S++)? [}]?"
                     /* Common man page references - e.g. foo.1 or foo(1)*/
                   + " | \\s \\S+ (?: [.]\\d++ | [(]\\d++[)]) (?:\\s|[.,:;\")]|$)"
                      /* absolute paths */
                   + " | (\\s|[\\[(])? [/](?: s?bin|boot|dev|etc|home|lib(?:32|64|x32)|mnt|opt|proc|root|run|srv|sys|tmp|usr|var) (?:/\\S++)?"
                      /* paths relative to home or the debian directory */
                   + " | \\s (?:[$]HOME|~|d(?:ebian)?) / (?:\\S++)?"
            , Pattern.COMMENTS
    );

    @NotNull
    public Tokenizer<?> getTokenizer(PsiElement element) {
        if (DchTypes.CHANGE_DETAILS.equals(element.getNode().getElementType())) {
            return CHANGELOG_TOKENIZER;
        }
        return EMPTY_TOKENIZER;
    }

    private static class ChangelogSplitter extends BaseSplitter {
        @Override
        public void split(@Nullable String text, @NotNull TextRange range, Consumer<TextRange> consumer) {
            if (StringUtil.isEmpty(text)) {
                return;
            }
            final Splitter ws = PlainTextSplitter.getInstance();
            for (TextRange r : excludeByPattern(text, range, IGNORE_PATTERN, 0)) {
                ws.split(text, r, consumer);
            }
        }
    }
}
