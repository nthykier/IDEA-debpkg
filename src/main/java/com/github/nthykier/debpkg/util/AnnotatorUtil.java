package com.github.nthykier.debpkg.util;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class AnnotatorUtil {

    private static final Deb822TypeSafeLocalQuickFix<Deb822HangingContValue> INSERT_SPACE_DOT_CONTINUATION_LINE_FIXER = incorrectContinuationLineFixer(
            "whitespace-only-continuation-line-insert-dotspace",
            " .\n "
    );
    private static final Deb822TypeSafeLocalQuickFix<Deb822HangingContValue> REMOVE_INCORRECT_CONTINUATION_LINE_FIXER = incorrectContinuationLineFixer(
            "whitespace-only-continuation-line-remove",
            " "
    );

    private static final LocalQuickFix[] CONT_LINE_FIXES_REMOVE_ONLY = new LocalQuickFix[]{REMOVE_INCORRECT_CONTINUATION_LINE_FIXER};
    private static final LocalQuickFix[] CONT_LINE_FIXES_ALL = new LocalQuickFix[]{INSERT_SPACE_DOT_CONTINUATION_LINE_FIXER, REMOVE_INCORRECT_CONTINUATION_LINE_FIXER};

    public static <T extends PsiElement> void createAnnotationWithQuickFix(
            @NotNull AnnotationHolder annoHolder,
            @NotNull HighlightSeverity severity,
            @NotNull Function<String, Deb822TypeSafeLocalQuickFix<T>> quickfixer,
            @NotNull String baseName,
            @NotNull T elementToFix,
            @NotNull ProblemHighlightType highlightType,
            Object... params
    ) {
        LocalQuickFix quickFix = quickfixer.apply(baseName);
        ProblemDescriptor problemDescriptor = new Deb822ProblemDescriptor(quickFix, baseName, elementToFix, highlightType);
        annoHolder.newAnnotation(severity, getAnnotationText(baseName, params))
                .range(elementToFix)
                .newLocalQuickFix(quickFix, problemDescriptor).registerFix()
                .create();
    }

    public static void createAnnotationWithQuickFixForBrokenContinuationLine(
            @NotNull AnnotationHolder annoHolder,
            @NotNull Deb822HangingContValue elementToFix) {
        Deb822FieldValuePair fieldValuePair = Deb822PsiImplUtil.getAncestorOfType(elementToFix, Deb822FieldValuePair.class);
        assert fieldValuePair != null;
        Deb822KnownField knownField = fieldValuePair.getField().getDeb822KnownField();
        LocalQuickFix[] fixes = CONT_LINE_FIXES_REMOVE_ONLY;
        ProblemDescriptor problemDescriptor;
        AnnotationBuilder annotationBuilder;
        final String basename = "whitespace-only-continuation-line";
        if (knownField == null || knownField.getFieldValueType() == Deb822KnownFieldValueType.FREE_TEXT_VALUE) {
            fixes = CONT_LINE_FIXES_ALL;
        }
        problemDescriptor = new Deb822ProblemDescriptor(fixes, basename, elementToFix, ProblemHighlightType.ERROR);
        annotationBuilder = annoHolder.newAnnotation(HighlightSeverity.ERROR, getAnnotationText(basename))
                .range(elementToFix);
        for (LocalQuickFix localQuickFix : fixes) {
            annotationBuilder = annotationBuilder
                    .newLocalQuickFix(localQuickFix, problemDescriptor)
                    .registerFix();
        }
        annotationBuilder.create();
    }

    @NotNull
    public static <T extends PsiElement> Function<String, Deb822TypeSafeLocalQuickFix<T>> replacementQuickFixer(final Function<Project, T> factoryMethod) {
        return (String s) -> psiReplacement(s, factoryMethod);
    }

    @NotNull
    public static <T extends PsiElement> Function<String, Deb822TypeSafeLocalQuickFix<T>> elementRemovalQuickfixer(final Class<T> clazz) {
        return (String s) -> deleteFieldQuickFix(s, clazz);
    }

    @NotNull
    public static <T extends Deb822FieldValuePair> Function<String, Deb822TypeSafeLocalQuickFix<T>> fieldInsertionQuickFix(final Function<Project, T> factoryMethod, String... insertRelativeTo) {
        return (String s) -> insertField(s, factoryMethod, insertRelativeTo);
    }

    private static Deb822TypeSafeLocalQuickFix<Deb822HangingContValue> incorrectContinuationLineFixer(String baseName, String replacementText) {
        return new Deb822TypeSafeLocalQuickFixImpl<>(baseName, (project, problemDescriptor) -> {
            /*
             * Currently it is easier just to rewrite the entire Deb822FieldValuePair rather than just replacing the
             * element(s) with "repaired" elements.  Therefore we basically stringify the entire Deb822FieldValuePair,
             * find the broken continuation part and replace it with " .\n ".  The new string is then parsed as a
             * (new) Deb822FieldValuePair, which replaces the old Deb822FieldValuePair.
             */
            PsiElement hangingContElement = problemDescriptor.getPsiElement();
            Deb822FieldValuePair fieldValuePair = Deb822PsiImplUtil.getAncestorOfType(hangingContElement, Deb822FieldValuePair.class);

            StringBuilder builder = new StringBuilder();
            ASTNodeStringConverter stringConverter = new ASTNodeStringConverter(builder);
            int offset;
            TextRange rangeOfElement = hangingContElement.getTextRange();
            Deb822FieldValuePair replacement;
            assert fieldValuePair != null;

            stringConverter.readTextFromNode(fieldValuePair.getNode());

            offset = rangeOfElement.getStartOffset() - fieldValuePair.getTextOffset();

            builder.replace(offset, offset + rangeOfElement.getLength(), replacementText);

            replacement = Deb822ElementFactory.createFieldValuePairFromText(project, builder.toString());
            fieldValuePair.replace(replacement);
        });
    }

    @NotNull
    @Nls(capitalization = Nls.Capitalization.Sentence)
    public static String getAnnotationText(@NotNull String baseName, Object... params) {
        return Deb822Bundle.message("deb822.files.annotator.fields." + baseName, params);
    }

    public static class Deb822TypeSafeLocalQuickFixImpl<T extends PsiElement> extends Deb822LocalQuickFixImpl implements Deb822TypeSafeLocalQuickFix<T> {
        protected Deb822TypeSafeLocalQuickFixImpl(String baseName, BiConsumer<Project, ProblemDescriptor> fix) {
           super(baseName, fix);
        }
    }

    public static class Deb822TypeSafeProblemDescriptor<T extends PsiElement> extends Deb822ProblemDescriptor {
        public Deb822TypeSafeProblemDescriptor(@NotNull LocalQuickFix fixer,
                                               @NotNull String baseName,
                                               @NotNull T element,
                                               @NotNull ProblemHighlightType highlightType) {
            super(fixer, baseName, element, highlightType);
        }
    }

    private static <T extends PsiElement> Deb822TypeSafeLocalQuickFix<T> deleteFieldQuickFix(String baseName, Class<T> psiElementClass) {
        return new Deb822TypeSafeLocalQuickFixImpl<>(baseName, (p, descriptor) -> {
            PsiElement element = descriptor.getPsiElement();
            if (! psiElementClass.isAssignableFrom(element.getClass())) {
                throw new IncorrectOperationException("Mismatch between element type being removed vs. the element intended for removal");
            }
            element.delete();
        });
    }

    private interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }

    @RequiredArgsConstructor(staticName = "of")
    public static class GenericTypeSafeQuickfix<T extends PsiElement> implements Deb822TypeSafeLocalQuickFix<T> {

        @Getter
        private final String baseName;
        private final Function<Project, T> replacementGenerator;
        private final TriConsumer<Project, ProblemDescriptor, T> fixerWithReplacement;

        @NotNull
        protected T getCorrectedElement(@NotNull Project project) {
            return Objects.requireNonNull(this.replacementGenerator.apply(project),"Bug in "
                    + this.getClass().getCanonicalName() + " - Replacement is null");
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            T replacement = getCorrectedElement(project);
            Objects.requireNonNull(replacement, "Bug in "
                    + this.getClass().getCanonicalName() + " - Replacement is null");

            fixerWithReplacement.accept(project, descriptor, replacement);
        }
    }

    private static <T extends PsiElement> Deb822TypeSafeLocalQuickFix<T> psiReplacement(String baseName, Function<Project, T> replacementGenerator) {
        return GenericTypeSafeQuickfix.of(baseName, replacementGenerator, (p, descriptor, replacement) -> {
            PsiElement originalElement = descriptor.getPsiElement();
            originalElement.replace(replacement);
        });
    }

    private static void insertBefore(PsiElement paragraph, PsiElement anchorElement, PsiElement newField, PsiElement newline) {
        paragraph.addBefore(newField, anchorElement);
        paragraph.addBefore(newline, anchorElement);
    }

    private static <T extends Deb822FieldValuePair> Deb822TypeSafeLocalQuickFix<T> insertField(String baseName, Function<Project, T> newFieldGenerator, String ... beforeFields) {
        return GenericTypeSafeQuickfix.of(baseName, newFieldGenerator, (project, descriptor, newField) -> {
            Deb822Paragraph paragraph = Deb822PsiImplUtil.getAncestorOfType(descriptor.getPsiElement(), Deb822Paragraph.class);
            // A Deb822FieldValuePair does not contain a trailing newline; force it in to avoid breaking the next field
            // The "Foo: bar" part is only there to avoid a parser error from missing a field / paragraph.  Without it
            // we get a Psi Error element rather than the newline whitespace element that we want.
            PsiElement whitespace = Deb822ElementFactory.createFile(project, "\nFoo: bar\n").getFirstChild();
            Deb822FieldValuePair insertRelativeTo = null;

            if (paragraph == null) {
                throw new IncorrectOperationException("Insertion failed; could not determine which paragraph should have the new field");
            }

            for (String fieldName : beforeFields) {
                insertRelativeTo = paragraph.getFieldValuePair(fieldName);
                if (insertRelativeTo != null) {
                    break;
                }
            }

            if (insertRelativeTo != null) {
                insertBefore(paragraph, insertRelativeTo, newField, whitespace);
            } else {
                List<Deb822FieldValuePair> fieldPairs = paragraph.getFieldValuePairList();
                Deb822FieldValuePair lastPair = fieldPairs.get(fieldPairs.size() - 1);
                PsiElement lastChild = paragraph.getLastChild();
                /* If the last element is not a PARAGRAPH_FINISH (happens at EOF) then this logic inserts the tokens
                 * wrong.  Prefer insert it as the second last field instead of breaking the file.
                 */
                boolean canInsertAtEnd = lastChild.getNode().getElementType() == Deb822Types.PARAGRAPH_FINISH;
                /* Prefer Description as the last field as it is conventionally in the end of d/control paragraphs */
                if (!canInsertAtEnd || lastPair.getField().getFieldName().equalsIgnoreCase("description")) {
                    insertBefore(paragraph, lastPair, newField, whitespace);
                } else {
                    insertBefore(paragraph, lastChild, newField, whitespace);
                }
            }
        });
    }
}
