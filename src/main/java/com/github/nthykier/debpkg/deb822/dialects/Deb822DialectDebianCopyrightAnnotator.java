package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822SyntaxHighlighter;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.github.nthykier.debpkg.util.ASTNodeStringConverter;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Deb822DialectDebianCopyrightAnnotator implements Annotator {

    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof Deb822File) {
            Deb822AllParagraphs paragraphs = Deb822PsiImplUtil.getNextSiblingOfType(
                    element.getFirstChild(),
                    Deb822AllParagraphs.class
            );
            VirtualFile rootDir = getRootDir(element.getContainingFile());
            ASTNodeStringConverter converter = new ASTNodeStringConverter();
            if (paragraphs == null) {
                return;
            }
            for (Deb822Paragraph paragraph : paragraphs.getParagraphList()) {
                checkParagraph(holder, paragraph, rootDir, converter);
            }
        }
    }

    private void checkParagraph(@NotNull AnnotationHolder holder, @NotNull Deb822Paragraph paragraph,
                                VirtualFile rootDir, ASTNodeStringConverter converter) {
        KnownFieldTable knownFieldTable = Deb822DialectDebianCopyrightLanguage.INSTANCE.getKnownFieldTable();
        String paragraphType = paragraph.classifyParagraph();
        Deb822FieldValuePair filesField;
        List<List<ASTNode>> splitFieldValue;
        Deb822KnownField filesKnownField;
        Deb822ValueParts valueParts;
        if (!Deb822DialectDebianCopyrightLanguage.PARAGRAPH_TYPE_FILES.equals(paragraphType)) {
            return;
        }
        filesField = paragraph.getFieldValuePair("Files");
        assert filesField != null;
        valueParts = filesField.getValueParts();
        if (valueParts == null) {
            /* Parser leaves an error for this already */
            return;
        }
        filesKnownField = knownFieldTable.getField("Files");
        assert filesKnownField != null : "Debian Copyright format must have \"Files\" as a known field";
        splitFieldValue = filesKnownField.getFieldValueType().splitValue(valueParts);
        for (List<ASTNode> wordParts : splitFieldValue) {
            String word = asString(converter, wordParts);
            TextRange range = rangeOfWordParts(wordParts);
            Boolean hadWildcards;
            if (word.equals("*")) {
                /* Catch all wildcard */
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(range)
                        .textAttributes(Deb822SyntaxHighlighter.VALUE_KEYWORD)
                        .create();
                continue;
            }

            if (word.startsWith("/") || word.startsWith("./") || word.startsWith("../")) {
                int badLen = word.indexOf('/');
                while (word.charAt(badLen) == '/') {
                    badLen++;
                }
                holder.newSilentAnnotation(HighlightSeverity.ERROR)
                        .range(TextRange.from(range.getStartOffset(), badLen))
                        .create();
                continue;
            }

            hadWildcards = scanPath(word, range, holder);
            if (hadWildcards == null || rootDir == null) {
                continue;
            }


            if (!hadWildcards) {
                String[] pathParts = word.split("/++");
                VirtualFile foundPath = rootDir;
                int i = 0;

                for (; i < pathParts.length ; i++) {
                    foundPath = foundPath.findChild(pathParts[i]);
                    if (foundPath == null) {
                        break;
                    }
                }
                if (foundPath == null) {
                    String message;
                    if (i == 0) {
                        message = Deb822Bundle.message("deb822.files.annotator.fields.path-in-files-field-does-not-match-anything", word);
                    } else {
                        String partThatMatches = String.join("/", Arrays.asList(pathParts).subList(0, i));
                        message = Deb822Bundle.message("deb822.files.annotator.fields.path-in-files-field-does-not-match-anything-after-x", word, partThatMatches);
                    }
                    holder.newAnnotation(
                            HighlightSeverity.WARNING,
                            message
                    )
                            .range(range)
                            .create();
                } else if (foundPath.isDirectory()) {
                    holder.newAnnotation(
                            HighlightSeverity.WARNING,
                            Deb822Bundle.message("deb822.files.annotator.fields.paths-in-files-field-must-match-files", word)
                    )
                            .range(range)
                            .create();
                }
            }
        }
    }

    private static Boolean scanPath(String path, TextRange pathRange, @NotNull AnnotationHolder holder) {
        final int length = path.length();
        char lastChar = 0;
        boolean allOk = true;
        Boolean hadWildcards = Boolean.FALSE;
        for (int i = 0 ; i < length ; i++) {
            char c = path.charAt(i);
            if (lastChar == '\\') {
                boolean valid = true;
                switch (c) {
                    case '*':
                    case '?':
                    case '\\':
                        /* Ok */
                        break;
                    default:
                        allOk = false;
                        valid = false;
                        break;
                }
                highlightWildcardOrEscapeSequence(pathRange, i - 1, 2, holder, valid);
                lastChar = c;
                continue;
            }
            if (c == '*' || c == '?') {
                hadWildcards = Boolean.TRUE;
                if (lastChar == '*' && c == '*') {
                    String message = Deb822Bundle.message("deb822.files.annotator.fields.files-pattern-double-star-not-supported");
                    highlightSequence(holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message),
                            null,
                            pathRange, i - 1, 2
                    );
                } else {
                    highlightSequence(holder.newSilentAnnotation(HighlightSeverity.INFORMATION),
                            Deb822SyntaxHighlighter.WILDCARD_CHARACTER,
                            pathRange, i, 1
                    );
                }
            }
            lastChar = c;
        }
        if (lastChar == '\\') {
            highlightWildcardOrEscapeSequence(pathRange, length - 1, 1, holder, false);
            allOk = false;
        }
        if (!allOk) {
            hadWildcards = null;
        }
        return hadWildcards;
    }

    private static void highlightSequence(AnnotationBuilder annotationBuilder, TextAttributesKey attributesKey, TextRange pathRange, int offset, int len) {
        TextRange newRange = TextRange.from(pathRange.getStartOffset() + offset, len);
        if (attributesKey != null) {
            annotationBuilder = annotationBuilder.textAttributes(attributesKey);
        }
        annotationBuilder.range(newRange)
                .create();
    }

    private static void highlightWildcardOrEscapeSequence(TextRange pathRange, int offset, int len, AnnotationHolder holder, boolean isValid) {
        if (isValid) {
            highlightSequence(holder.newSilentAnnotation(HighlightSeverity.INFORMATION),
                    DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE,
                    pathRange, offset, len
            );
        } else {
            highlightSequence(holder.newSilentAnnotation(HighlightSeverity.INFORMATION),
                    DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE,
                    pathRange, offset, len
            );
        }
    }

    private static VirtualFile getRootDir(PsiFile file) {
        VirtualFile debianDirectory = file.getVirtualFile().getParent();
        if (debianDirectory == null) {
            return null;
        }
        return debianDirectory.getParent();
    }

    private static String asString(ASTNodeStringConverter converter, List<ASTNode> wordParts) {
        converter.getStringBuilder().setLength(0);
        for (ASTNode node : wordParts) {
            converter.readTextFromNode(node);
        }
        return converter.getStringBuilder().toString();
    }

    private static TextRange rangeOfWordParts(List<ASTNode> wordParts) {
        if (wordParts.size() == 1) {
            return wordParts.get(0).getTextRange();
        } else {
            ASTNode first = wordParts.get(0);
            ASTNode last = wordParts.get(wordParts.size() - 1);
            int startOffset = first.getStartOffset();
            int len = last.getStartOffsetInParent() + last.getTextLength();
            return TextRange.from(startOffset, len);
        }
    }
}
