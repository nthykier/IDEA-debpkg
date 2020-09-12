package com.github.nthykier.debpkg.dch.psi.impl;

import com.github.nthykier.debpkg.dch.psi.DchChangelogLine;
import com.github.nthykier.debpkg.dch.psi.DchTypes;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractDchChangelogEntry extends ASTWrapperPsiElement implements DchChangelogLine {

    private static final TokenSet DCH_LINE_TOKEN_SET = TokenSet.create(DchTypes.CHANGE_DETAILS, DchTypes.CHANGE_RESPONSIBLE);

    private String cachedText;

    public AbstractDchChangelogEntry(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        this.cachedText = null;
    }

    @Override
    public String getText() {
        /* Guide lookup and cache the field name; we know there is exactly one leaf beneath this node containing the
         * text we want.  The default class structure does not and have to copy the text around, which is expensive.
         */
        if (cachedText == null) {
            cachedText = Deb822PsiImplUtil.getTextFromCompositeWrappingAToken(this, DCH_LINE_TOKEN_SET);
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
    public PsiReference[] getReferences() {
        List<PsiReference> references = new SmartList<>();
        String text = this.getText();
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

        if (references.isEmpty()) {
            return PsiReference.EMPTY_ARRAY;
        }
        return references.toArray(PsiReference.EMPTY_ARRAY);
    }
}
