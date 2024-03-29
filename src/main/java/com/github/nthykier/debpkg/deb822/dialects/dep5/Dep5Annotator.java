package com.github.nthykier.debpkg.deb822.dialects.dep5;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822SyntaxHighlighter;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianCopyrightLanguage;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.github.nthykier.debpkg.util.ASTNodeStringConverter;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.github.nthykier.debpkg.util.TriConsumer;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.github.nthykier.debpkg.util.CommonPsiUtil.isCorrectFileLanguage;

public class Dep5Annotator implements Annotator {

    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (!isCorrectFileLanguage(element, Deb822DialectDebianCopyrightLanguage.INSTANCE)) {
            return;
        }
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
        for (Deb822Value deb822Value : valueParts.getValueList()) {
            String word = deb822Value.getText();
            TextRange range = deb822Value.getTextRange();
            PathPart[] pathParts;

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

            pathParts = parsePathIntoParts(word, range, holder);
            checkPathParts(filesField, rootDir, word, pathParts, range, holder);
        }
    }


    private static void checkWildcard(PathPart part, @NotNull AnnotationHolder holder) {
        String wildcardText = part.getPath();

        if (part.getPathType() == PathType.WILDCARD_STAR && wildcardText.length() > 1) {
            String message = Deb822Bundle.message("deb822.files.annotator.fields.files-pattern-double-star-not-supported");
            holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message)
                    .range(part.getTextRange())
                    .create();
        } else {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(part.getTextRange())
                    .textAttributes(Deb822SyntaxHighlighter.WILDCARD_CHARACTER)
                    .create();
        }
    }

    private static String partsToString(PathPart[] pathParts, int offset, int length) {
        StringBuilder builder = new StringBuilder();
        final int fullLength = offset + length;
        for (int i = offset ; i < fullLength ; i++) {
            PathPart part = pathParts[i];
            assert !part.getPathType().isWildcard();
            builder.append(part.getPath());
        }
        return builder.toString();
    }

    static boolean isStarWildcardOrEndsWithSlashStarWildCard(PathPart[] parts) {
        if (parts.length == 1 && parts[0].getPathType() == PathType.WILDCARD_STAR) {
            return true;
        }
        if (parts.length < 3 || parts[parts.length - 1].getPathType() != PathType.WILDCARD_STAR) {
            return false;
        }
        return parts[parts.length - 2].getPathType() == PathType.DIRECTORY_SEPARATOR;
    }

    private static void checkPathParts(Deb822FieldValuePair affectedField, VirtualFile rootDir, String fullPath, PathPart[] pathParts, TextRange fullRange, @NotNull AnnotationHolder holder) {
        VirtualFile currentDir = rootDir;
        int length;
        PathPart finalPartWildcard = null;

        /* Special-case ".../*" and "*", which are very common patterns */
        if (isStarWildcardOrEndsWithSlashStarWildCard(pathParts)) {
            length = pathParts.length - 1;
            finalPartWildcard = pathParts[length];
            checkWildcard(finalPartWildcard, holder);
        } else {
            length = pathParts.length;
        }

        for (int i = 0 ; i < length ; i++) {
            PathPart pathPart = pathParts[i];
            PathPart nextPathPart = i + 1 < length ? pathParts[i + 1] : null;
            switch (pathPart.getPathType()) {
                case NAME_PART:
                    if (currentDir != null) {
                        VirtualFile nextElement = currentDir.findChild(pathPart.getPath());
                        boolean reportMiss = true;
                        if (nextElement == null && nextPathPart != null && nextPathPart.getPathType().isWildcard()) {
                            String prefix = pathPart.getPath();
                            for (VirtualFile path : currentDir.getChildren()) {
                                if (path.getName().startsWith(prefix)) {
                                    reportMiss = false;
                                    break;
                                }
                            }
                        }
                        if (nextElement == null && reportMiss) {
                            String message;
                            TextRange badRange;
                            if (i == 0) {
                                message = Deb822Bundle.message("deb822.files.annotator.fields.path-in-files-field-does-not-match-anything", fullPath);
                                badRange = fullRange;
                            } else {
                                String directoryPart = partsToString(pathParts, 0, i - 1);
                                message = Deb822Bundle.message("deb822.files.annotator.fields.path-in-files-field-does-not-match-anything-after-x",
                                        fullPath,
                                        directoryPart,
                                        pathPart.getPath()
                                );
                                badRange = new TextRange(pathPart.getTextRange().getStartOffset(), fullRange.getEndOffset());
                            }
                            holder.newAnnotation(
                                    HighlightSeverity.WARNING,
                                    message
                            )
                                    .range(badRange)
                                    .create();
                        }
                        currentDir = nextElement;
                    }
                    break;
                case DIRECTORY_SEPARATOR:
                    if (pathPart.getPath().length() > 1) {
                        holder.newSilentAnnotation(HighlightSeverity.WEAK_WARNING)
                                .range(pathPart.getTextRange())
                                .create();
                    }
                    if (currentDir != null && !currentDir.isDirectory()) {
                        TextRange badRange = new TextRange(pathPart.getTextRange().getStartOffset(), fullRange.getEndOffset());
                        String text = partsToString(pathParts, 0, i);
                        String message = Deb822Bundle.message("deb822.files.annotator.fields.path-in-files-field-used-as-dir-but-is-not",
                                fullPath,
                                text
                        );
                        holder.newAnnotation(HighlightSeverity.WARNING, message)
                                .range(badRange)
                                .create();
                        currentDir = null;
                    }
                    break;
                case WILDCARD_QUESTION_MARK:
                case WILDCARD_STAR:
                    checkWildcard(pathPart, holder);
                    /* we cannot handle wild cards at the moment */
                    currentDir = null;
                    break;
            }
        }

        /* It must not end on a directory though (though account for our special casing of ".../*") */
        if (currentDir != null && currentDir.isDirectory() && finalPartWildcard == null) {
            TriConsumer<ProblemDescriptor, Deb822FieldValuePair, StringBuilder> contextReplacer = (pd, pair, builder) -> {
                int offset = fullRange.getEndOffset() - pair.getTextOffset();
                PathPart lastPath = pathParts[length - 1];
                /* Insert "*" (if it ends with a slash already) or "/*" (otherwise) */
                if (lastPath.getPathType().isDirectorySeparator()) {
                    builder.insert(offset, "*");
                } else {
                    builder.insert(offset, "/*");
                }
            };
            LocalQuickFix quickFix = AnnotatorUtil.tweakFieldWithValueReplacementFix(
                    Deb822Bundle.message("deb822.files.quickfix.fields.paths-in-files-field-must-match-files.name"),
                    contextReplacer
            );
            InspectionManager inspectionManager = InspectionManager.getInstance(affectedField.getProject());
            ProblemDescriptor problemDescriptor = inspectionManager.createProblemDescriptor(
                    affectedField,
                    Deb822Bundle.message("deb822.files.quickfix.fields.paths-in-files-field-must-match-files.description"),
                    quickFix,
                    ProblemHighlightType.WARNING,
                    true
            );
            holder.newAnnotation(
                    HighlightSeverity.WARNING,
                    Deb822Bundle.message("deb822.files.annotator.fields.paths-in-files-field-must-match-files", fullPath)
            )
                    .range(fullRange)
                    .newLocalQuickFix(quickFix, problemDescriptor)
                    .registerFix()
                    .create();
        } else if (currentDir == null) {
            /* If we do _not_ have a complete match, then highlight the path */
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(Deb822SyntaxHighlighter.VALUE_KEYWORD)
                    .range(fullRange)
                    .create();
        }
    }

    static PathPart[] parsePathIntoParts(String path, TextRange pathRange, @Nullable AnnotationHolder holder) {
        final int length = path.length();
        char lastChar = 0;
        PathBuilder pathBuilder = PathBuilder.of(pathRange);

        for (int i = 0 ; i < length ; i++) {
            char c = path.charAt(i);
            if (c == '/') {
                pathBuilder.currentPathType(PathType.DIRECTORY_SEPARATOR);
                pathBuilder.addChar(c);

                if (lastChar == '\\' && holder != null) {
                    // The sequence "\\/" is also an invalid escape sequence
                    highlightWildcardOrEscapeSequence(pathRange, i - 1, 2, holder, false);
                }

                lastChar = c;
                continue;
            }

            if (lastChar == '\\') {
                boolean valid = true;
                switch (c) {
                    case '*':
                    case '?':
                    case '\\':
                        /* Ok */
                        break;
                    default:
                        valid = false;
                        break;
                }
                pathBuilder.addChar(c);
                if (holder != null) {
                    highlightWildcardOrEscapeSequence(pathRange, i - 1, 2, holder, valid);
                }
                // we need it to "not" be '\\' to avoid flagging the "\\x" in "\\\\x" as invalid.
                lastChar = '\0';
                continue;
            }

            switch (c) {
                case '*':
                    pathBuilder.currentPathType(PathType.WILDCARD_STAR);
                    break;
                case '?':
                    pathBuilder.currentPathType(PathType.WILDCARD_QUESTION_MARK);
                    break;
                default:
                    pathBuilder.currentPathType(PathType.NAME_PART);
                    break;
            }

            if (c == '\\') {
                pathBuilder.skipChar();
            } else {
                pathBuilder.addChar(c);
            }
            lastChar = c;
        }
        if (lastChar == '\\' && holder != null) {
            highlightWildcardOrEscapeSequence(pathRange, length - 1, 1, holder, false);
        }

        pathBuilder.finishCurrentElement();
        return pathBuilder.getPathParts();
    }

    private static void highlightSequence(@NotNull AnnotationBuilder annotationBuilder, TextAttributesKey attributesKey, TextRange pathRange, int offset, int len) {
        TextRange newRange = TextRange.from(pathRange.getStartOffset() + offset, len);
        if (attributesKey != null) {
            annotationBuilder = annotationBuilder.textAttributes(attributesKey);
        }
        annotationBuilder.range(newRange)
                .create();
    }

    private static void highlightWildcardOrEscapeSequence(TextRange pathRange, int offset, int len, @NotNull AnnotationHolder holder, boolean isValid) {
        if (isValid) {
            highlightSequence(holder.newSilentAnnotation(HighlightSeverity.INFORMATION),
                    DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE,
                    pathRange, offset, len
            );
        } else {
            String message = Deb822Bundle.message("deb822.files.annotator.fields.files-pattern-invalid-escape-sequence");
            highlightSequence(holder.newAnnotation(HighlightSeverity.ERROR, message),
                    DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE,
                    pathRange, offset, len
            );
        }
    }

    static VirtualFile getRootDir(PsiFile file) {
        VirtualFile path = file.getVirtualFile();
        if (path == null) {
            // This often happens if we are called during code completion.
            path = file.getOriginalFile().getVirtualFile();
        }
        if (path == null) {
            return null;
        }
        path = path.getParent();
        if (path == null) {
            return null;
        }
        return path.getParent();
    }

    static String asString(ASTNodeStringConverter converter, List<ASTNode> wordParts) {
        converter.getStringBuilder().setLength(0);
        for (ASTNode node : wordParts) {
            converter.readTextFromNode(node);
        }
        return converter.getStringBuilder().toString();
    }

    static TextRange rangeOfWordParts(List<ASTNode> wordParts) {
        return rangeOfWordParts(wordParts, false, 0);
    }

    static TextRange rangeOfWordParts(List<ASTNode> wordParts, boolean relativeToParent, int offset) {
        if (wordParts.size() == 1) {
            ASTNode node = wordParts.get(0);
            return relativeToParent
                    ? TextRange.from(node.getStartOffsetInParent() + offset, node.getTextLength())
                    : node.getTextRange();
        } else {
            ASTNode first = wordParts.get(0);
            ASTNode last = wordParts.get(wordParts.size() - 1);
            int startOffset = relativeToParent ? first.getStartOffsetInParent() + offset : first.getStartOffset();
            int len = last.getStartOffsetInParent() + last.getTextLength();
            return TextRange.from(startOffset, len);
        }
    }



    @RequiredArgsConstructor(staticName = "of")
    private static class PathBuilder {

        private static final PathPart[] EMPTY = new PathPart[0];

        private final StringBuilder builder = new StringBuilder(25);

        private final List<PathPart> pathParts = new ArrayList<>();

        private final TextRange enclosingTextRange;
        private int index = 0;
        private int segmentStart = -1;
        private PathType currentPathType = null;

        public void addChar(char c) {
            this.builder.append(c);
            index++;
        }

        public void skipChar() {
            index++;
        }

        public void currentPathType(PathType expectedPathType) {
            if (currentPathType != null && expectedPathType != currentPathType) {
                finishCurrentElement();
            }
            currentPathType = expectedPathType;
            if (segmentStart == -1) {
                segmentStart = index;
            }
        }

        public void finishCurrentElement() {
            assert currentPathType != null;
            TextRange rangeOfSegment = TextRange.from(enclosingTextRange.getStartOffset() + segmentStart, builder.length());
            pathParts.add(PathPart.of(currentPathType, builder.toString(), rangeOfSegment));
            currentPathType = null;
            segmentStart = -1;
            builder.setLength(0);
        }

        public PathPart[] getPathParts() {
            return pathParts.toArray(EMPTY);
        }
    }

}
