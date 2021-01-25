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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class AnnotatorUtil {

    private static final Deb822LocalQuickFix INSERT_SPACE_DOT_CONTINUATION_LINE_FIXER = incorrectContinuationLineFixer(
            "whitespace-only-continuation-line-insert-dotspace",
            " .\n "
    );
    private static final Deb822LocalQuickFix REMOVE_INCORRECT_CONTINUATION_LINE_FIXER = incorrectContinuationLineFixer(
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

    public static void createAnnotationWithQuickFixWithoutTypeSafety(
            @NotNull AnnotationHolder annoHolder,
            @NotNull HighlightSeverity severity,
            @NotNull Function<String, Deb822LocalQuickFix> quickfixer,
            @NotNull String baseName,
            @NotNull PsiElement elementToFix,
            @Nullable TextRange affectedRange,
            @NotNull ProblemHighlightType highlightType,
            Object... params
    ) {
        LocalQuickFix quickFix = quickfixer.apply(baseName);
        ProblemDescriptor problemDescriptor = new Deb822ProblemDescriptor(quickFix, baseName, elementToFix, highlightType);
        annoHolder.newAnnotation(severity, getAnnotationText(baseName, params))
                .range(affectedRange == null ? elementToFix.getTextRange() : affectedRange)
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
    public static LocalQuickFix fieldInsertionQuickFix(@NotNull String content, @NotNull String @NotNull ... insertRelativeTo) {
        return new Deb822InsertFieldQuickFix(content, Arrays.asList(insertRelativeTo));
    }

    private static Deb822LocalQuickFix incorrectContinuationLineFixer(String baseName, String replacementText) {
        return fieldValueReplacementFix((problemDescriptor, fieldValuePair, builder) -> {
            PsiElement hangingContElement = problemDescriptor.getPsiElement();
            TextRange rangeOfElement = hangingContElement.getTextRange();
            int offset = rangeOfElement.getStartOffset() - fieldValuePair.getTextOffset();
            builder.replace(offset, offset + rangeOfElement.getLength(), replacementText);
        }).apply(baseName);
    }

    public static Function<String, Deb822LocalQuickFix> fieldValueReplacementFix(TriConsumer<ProblemDescriptor, Deb822FieldValuePair, StringBuilder> contextReplacer) {
        return baseName ->
                Deb822LocalQuickFixImpl.of(baseName, (project, problemDescriptor) -> {
                    PsiElement problemElement = problemDescriptor.getPsiElement();
                    Deb822FieldValuePair fieldValuePair;
                    StringBuilder builder = new StringBuilder();
                    ASTNodeStringConverter stringConverter = new ASTNodeStringConverter(builder);
                    Deb822FieldValuePair replacement;
                    if (problemElement instanceof Deb822FieldValuePair) {
                        fieldValuePair = (Deb822FieldValuePair)problemElement;
                    } else {
                        fieldValuePair = Deb822PsiImplUtil.getAncestorOfType(problemElement, Deb822FieldValuePair.class);
                        assert fieldValuePair != null;
                    }

                    stringConverter.readTextFromNode(fieldValuePair.getNode());

                    contextReplacer.accept(problemDescriptor, fieldValuePair, builder);

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

    private static <T extends PsiElement> Deb822TypeSafeLocalQuickFix<T> deleteFieldQuickFix(String baseName, Class<T> psiElementClass) {
        return new Deb822TypeSafeLocalQuickFixImpl<>(baseName, (p, descriptor) -> {
            PsiElement element = descriptor.getPsiElement();
            if (! psiElementClass.isAssignableFrom(element.getClass())) {
                throw new IncorrectOperationException("Mismatch between element type being removed vs. the element intended for removal");
            }
            element.delete();
        });
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

}
