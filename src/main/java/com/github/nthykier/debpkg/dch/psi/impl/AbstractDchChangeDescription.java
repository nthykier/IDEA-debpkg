package com.github.nthykier.debpkg.dch.psi.impl;

import com.github.nthykier.debpkg.dch.psi.DchChangeDescription;
import com.github.nthykier.debpkg.util.ASTNodeStringConverter;
import com.github.nthykier.debpkg.util.CommonPsiUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.paths.WebReference;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceService;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDchChangeDescription extends ASTWrapperPsiElement implements DchChangeDescription {

    // Based heavily on the regex described in Debian Policy 4.4
    private static final Pattern CLOSES_PATTERN = Pattern.compile(
            "closes:\\s*+((?:bug|lp:\\s*+)?+[#]?+\\s?+\\d++(?:,\\s*+(?:bug|lp:)?+[#]?+\\s?+\\d++)*)",
            Pattern.CASE_INSENSITIVE
    );

    private static final int CLOSES_LEN = "Closes:".length();

    private String cachedText;
    private PsiReference[] internalReferences;

    public AbstractDchChangeDescription(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        this.cachedText = null;
        this.internalReferences = null;
    }

    @Override
    public String getText() {
        if (cachedText == null) {
            cachedText = ASTNodeStringConverter.extractString(this.getNode());
        }
        return cachedText;
    }

    @Override
    public PsiReference getReference() {
        PsiReference[] refs = getReferences();
        if (refs.length == 1 && refs[0] != null) {
            return refs[0];
        }
        return null;
    }

    private List<TextRange> splitFilenames(@NotNull String text, final int offset, final int endOffset) {
        boolean inFilename = false;
        int startPoint = offset;
        int nesting = 0;
        List<TextRange> ranges = new SmartList<>();

        for (int i = startPoint ; i < endOffset ; i++) {
            char c = text.charAt(i);
            switch (c) {
                case ' ':
                    if (!inFilename) {
                        startPoint++;
                    }
                    break;
                case ',':
                    if (inFilename && nesting == 0) {
                        ranges.add(new TextRange(startPoint, i));
                        inFilename = false;
                        startPoint = i + 1;
                    }
                    break;
                case '{':
                    inFilename = true;
                    nesting++;
                    break;
                case '}':
                    inFilename = true;
                    nesting--;
                    break;
                default:
                    inFilename = true;
                    break;
            }
        }
        if (inFilename) {
            ranges.add(new TextRange(startPoint, endOffset));
        }

        return ranges;
    }

    @Override
    @NotNull
    public PsiReference @NotNull [] getReferences() {
        return getReferences(PsiReferenceService.Hints.NO_HINTS);
    }

        @Override
    public boolean shouldAskParentForReferences(PsiReferenceService.@NotNull Hints hints) {
        return false;
    }


    @Override
    public PsiReference @NotNull [] getReferences(PsiReferenceService.@NotNull Hints hints) {
        if (this.internalReferences == null) {
            String text = this.getText();
            List<PsiReference> references = new SmartList<>();
            if (text.startsWith("* ")) {
                final int startOffset = 2;
                int spaceIndex = text.indexOf(' ', startOffset + 1);
                int indexOfColon = text.indexOf(':', startOffset + 1);
                if (indexOfColon > -1 && (spaceIndex == -1 || indexOfColon < spaceIndex)) {
                    int commaIndex = text.lastIndexOf(',', indexOfColon - 1);
                    if (commaIndex == -1) {
                        /* Simple case; single file name with no spaces or commas */
                        references.add(new DchFileReference(this, new TextRange(startOffset, indexOfColon),
                                text.substring(2, indexOfColon)));
                    } else {
                        for (TextRange range : splitFilenames(text, startOffset, indexOfColon)) {
                            String match = text.substring(range.getStartOffset(), range.getEndOffset());
                            references.add(new DchFileReference(this, range, match));
                        }
                    }
                }
            }
            CommonPsiUtil.addURLReferences(this, text, references);
            processClosesStatement(text, references);
            if (references.isEmpty()) {
                this.internalReferences = PsiReference.EMPTY_ARRAY;
            } else {
                this.internalReferences = references.toArray(PsiReference.EMPTY_ARRAY);
            }
        }
        return this.internalReferences;
    }

    private void processClosesStatement(String text, List<PsiReference> references) {
        // Patterns are not exactly fast, so use this prilimary scan to find the "Closes:" part.
        // Scan backwards; Closes are often (but not always) near the end.
        int i = text.lastIndexOf(':');
        while (i >= CLOSES_LEN) {
            // With the "lastIndexOf" finding "Closes:" then the cursor is between the "s" and the ":"
            // Therefore we add 1 (to move past the column) and subtract CLOSES_LEN to move to the start of
            // word.
            int startOffset = i - CLOSES_LEN + 1;
            if (text.regionMatches(true, startOffset, "Closes:", 0, CLOSES_LEN)) {
                linkifyCloses(text, startOffset, references);
                break;
            }
            i = text.lastIndexOf(':', i - 1);
        }
    }

    private void linkifyCloses(String text, int startOffset, List<PsiReference> references) {
        Matcher matcher = CLOSES_PATTERN.matcher(text);
        if (matcher.find(startOffset)) {
            int matchOffset = matcher.start(1);
            int matchEnd = matcher.end();
            do {
                WebReference reference = linkifyBug(text, matchOffset, matchEnd);
                TextRange range = reference.getRangeInElement();
                references.add(reference);
                matchOffset = range.getEndOffset();
                // +2 is because it needs at least 2 characters to find a bug ("#NNNNNNN,M" with M being the second)
                if (matchOffset+2 < matchEnd && text.charAt(matchOffset) == ',') {
                    matchOffset++;
                    // Move past spaces after the comma - it looks stupid if a leading space is part of the link
                    while (matchOffset < matchEnd && Character.isSpaceChar(text.charAt(matchOffset))) {
                        matchOffset++;
                    }
                }
            } while (matchOffset < matchEnd);
        }
    }

    private WebReference linkifyBug(String text, int startOffset, int limitOffset) {
        int numberStart = -1;
        boolean isLPBug = text.regionMatches(true, startOffset, "LP:", 0, 3);
        int currentOffset;
        String url;
        for (currentOffset = startOffset; currentOffset < limitOffset; currentOffset++) {
            char c = text.charAt(currentOffset);
            if (Character.isDigit(c)) {
                if (numberStart == -1) {
                    numberStart = currentOffset;
                }
            } else if (numberStart != -1) {
                break;
            }
        }
        assert numberStart != -1;
        if (isLPBug) {
            url = "https://bugs.launchpad.net/ubuntu/+bug/" + text.substring(numberStart, currentOffset);
        } else {
            url = "https://bugs.debian.org/" + text.substring(numberStart, currentOffset);
        }
        return new WebReference(this, new TextRange(startOffset, currentOffset), url);
    }
}
