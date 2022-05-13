package com.github.nthykier.debpkg.util;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;
import com.github.nthykier.debpkg.deb822.psi.Deb822ElementFactory;
import com.github.nthykier.debpkg.deb822.psi.Deb822Field;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822HangingContValue;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
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
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class AnnotatorUtil {

    private static final LocalQuickFix INSERT_SPACE_DOT_CONTINUATION_LINE_FIXER = incorrectContinuationLineFixer(
            Deb822Bundle.message("deb822.files.quickfix.fields.whitespace-only-continuation-line-insert-dotspace.name"),
            " .\n "
    );
    private static final LocalQuickFix REMOVE_INCORRECT_CONTINUATION_LINE_FIXER = incorrectContinuationLineFixer(
            Deb822Bundle.message("deb822.files.quickfix.fields.whitespace-only-continuation-line-remove.name"),
            " "
    );

    private static final LocalQuickFix[] CONT_LINE_FIXES_REMOVE_ONLY = new LocalQuickFix[]{REMOVE_INCORRECT_CONTINUATION_LINE_FIXER};
    private static final LocalQuickFix[] CONT_LINE_FIXES_ALL = new LocalQuickFix[]{INSERT_SPACE_DOT_CONTINUATION_LINE_FIXER, REMOVE_INCORRECT_CONTINUATION_LINE_FIXER};

    public static <T extends PsiElement> void createAnnotationWithQuickFix(
            @NotNull AnnotationHolder annoHolder,
            @NotNull HighlightSeverity severity,
            @NotNull Deb822TypeSafeLocalQuickFix<T> quickFix,
            @NotNull String baseName,
            @NotNull T elementToFix,
            @NotNull ProblemHighlightType highlightType,
            Object... params
    ) {
        InspectionManager inspectionManager = InspectionManager.getInstance(elementToFix.getProject());
        ProblemDescriptor problemDescriptor = inspectionManager.createProblemDescriptor(
                elementToFix,
                Deb822Bundle.message("deb822.files.quickfix.fields."  + baseName +".description"),
                quickFix,
                highlightType,
                true
        );
        annoHolder.newAnnotation(severity, getAnnotationText(baseName, params))
                .range(elementToFix)
                .newLocalQuickFix(quickFix, problemDescriptor).registerFix()
                .create();
    }

    public static void createAnnotationWithQuickFixWithoutTypeSafety(
            @NotNull AnnotationHolder annoHolder,
            @NotNull HighlightSeverity severity,
            @NotNull LocalQuickFix quickFix,
            @NotNull String baseName,
            @NotNull PsiElement elementToFix,
            @Nullable TextRange affectedRange,
            @NotNull ProblemHighlightType highlightType,
            Object... params
    ) {
        InspectionManager inspectionManager = InspectionManager.getInstance(elementToFix.getProject());
        TextRange range = affectedRange == null ? elementToFix.getTextRange() : affectedRange;
        ProblemDescriptor problemDescriptor = inspectionManager.createProblemDescriptor(
                elementToFix,
                range,
                Deb822Bundle.message("deb822.files.quickfix.fields."  + baseName +".description"),
                highlightType,
                true,
                quickFix
        );
        annoHolder.newAnnotation(severity, getAnnotationText(baseName, params))
                .range(range)
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
        InspectionManager inspectionManager = InspectionManager.getInstance(elementToFix.getProject());
        final String basename = "whitespace-only-continuation-line";
        final String problemDescription = Deb822Bundle.message("deb822.files.quickfix.fields.whitespace-only-continuation-line.description");
        if (knownField == null || knownField.getFieldValueType() == Deb822KnownFieldValueType.FREE_TEXT_VALUE) {
            fixes = CONT_LINE_FIXES_ALL;
        }
        problemDescriptor = inspectionManager.createProblemDescriptor(elementToFix, problemDescription, true, fixes, ProblemHighlightType.ERROR);
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
    public static <T extends PsiElement> Deb822TypeSafeLocalQuickFix<T> replacementQuickFixer(@IntentionFamilyName  String familyName, final Function<Project, T> factoryMethod) {
        return psiReplacement(familyName, factoryMethod);
    }

    @NotNull
    public static <T extends PsiElement> Deb822TypeSafeLocalQuickFix<T> elementRemovalQuickfixer(@IntentionName String name, final Class<T> clazz) {
        return elementRemovalQuickfixer(name, name, clazz);
    }

    @NotNull
    public static <T extends PsiElement> Deb822TypeSafeLocalQuickFix<T> elementRemovalQuickfixer(@IntentionName String name, @IntentionFamilyName String familyName, final Class<T> clazz) {
        return deleteFieldQuickFix(name, familyName, clazz);
    }

    @NotNull
    public static LocalQuickFix fieldInsertionQuickFix(@NotNull String content, @NotNull String @NotNull ... insertRelativeTo) {
        return new Deb822InsertFieldQuickFix(content, Arrays.asList(insertRelativeTo));
    }

    private static LocalQuickFix incorrectContinuationLineFixer(@IntentionFamilyName String familyName, String replacementText) {
        return tweakFieldWithValueReplacementFix(familyName, (problemDescriptor, fieldValuePair, builder) -> {
            PsiElement hangingContElement = problemDescriptor.getPsiElement();
            TextRange rangeOfElement = hangingContElement.getTextRange();
            int offset = rangeOfElement.getStartOffset() - fieldValuePair.getTextOffset();
            builder.replace(offset, offset + rangeOfElement.getLength(), replacementText);
        });
    }

    public static LocalQuickFix tweakFieldWithValueReplacementFix(@IntentionFamilyName String familyName, TriConsumer<ProblemDescriptor, Deb822FieldValuePair, StringBuilder> contextReplacer) {
        return new Deb822LocalQuickFixImpl(familyName, familyName, (project, problemDescriptor) -> {
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

    public static LocalQuickFix replaceFieldNameFix(String newFieldName) {
        return replaceFieldNameFix(
                Deb822Bundle.message("deb822.files.quickfix.rename-field.name", newFieldName),
                Deb822Bundle.message("deb822.files.quickfix.rename-field.familyName"),
                newFieldName
        );
    }

    public static Deb822TypeSafeLocalQuickFix<Deb822Field> replaceFieldNameFix(@IntentionName String name, @IntentionFamilyName String familyName, String newFieldName) {
        return new Deb822TypeSafeLocalQuickFixImpl<>(name, familyName, (project, problemDescriptor) -> {
            PsiElement problemElement = problemDescriptor.getPsiElement();
            if (! (problemElement instanceof Deb822Field)) {
                throw new IncorrectOperationException("Mismatch between element type being removed vs. the element intended for removal");
            }
            Deb822Field replacement = Deb822ElementFactory.createFieldValuePairFromText(project, newFieldName + ": Text").getField();
            problemElement.replace(replacement);
        });
    }

    public static LocalQuickFix replaceFieldValueReplacementFix(@IntentionName String name, @IntentionFamilyName String familyName, String newValue) {
        return replaceFieldValueReplacementFix(name, familyName, newValue, false);
    }

    public static LocalQuickFix replaceFieldValueReplacementFix(@IntentionFamilyName String familyName, String newValue) {
        return replaceFieldValueReplacementFix(familyName, familyName, newValue, false);
    }

    public static LocalQuickFix replaceFieldValueReplacementFix(@IntentionName String name, @IntentionFamilyName String familyName, String newValue, boolean useValueAsIs) {
        return new Deb822LocalQuickFixImpl(name, familyName, (project, problemDescriptor) -> {
            PsiElement problemElement = problemDescriptor.getPsiElement();
            Deb822FieldValuePair fieldValuePair;
            Deb822FieldValuePair replacement;
            if (problemElement instanceof Deb822FieldValuePair) {
                fieldValuePair = (Deb822FieldValuePair)problemElement;
            } else {
                fieldValuePair = Deb822PsiImplUtil.getAncestorOfType(problemElement, Deb822FieldValuePair.class);
                assert fieldValuePair != null;
            }
            String replacementValue = newValue;
            if (!useValueAsIs) {
                if (!replacementValue.startsWith(" ")) {
                    replacementValue = " " + replacementValue;
                }
                if (!replacementValue.endsWith("\n")) {
                    replacementValue += "\n";
                }
            }
            replacement = Deb822ElementFactory.createFieldValuePairFromText(project, fieldValuePair.getField().getFieldName() + ":" + replacementValue);
            fieldValuePair.replace(replacement);
        });
    }

    @NotNull
    @Nls(capitalization = Nls.Capitalization.Sentence)
    public static String getAnnotationText(@NotNull String baseName, Object... params) {
        return Deb822Bundle.message("deb822.files.annotator.fields." + baseName, params);
    }

    public static class Deb822TypeSafeLocalQuickFixImpl<T extends PsiElement> extends Deb822LocalQuickFixImpl implements Deb822TypeSafeLocalQuickFix<T> {
        protected Deb822TypeSafeLocalQuickFixImpl(@IntentionName String name, @IntentionFamilyName String familyName, BiConsumer<Project, ProblemDescriptor> fix) {
           super(name, familyName, fix);
        }
    }

    private static <T extends PsiElement> Deb822TypeSafeLocalQuickFix<T> deleteFieldQuickFix(@IntentionName String name, @IntentionFamilyName String familyName, Class<T> psiElementClass) {
        return new Deb822TypeSafeLocalQuickFixImpl<>(name, familyName, (p, descriptor) -> {
            PsiElement element = descriptor.getPsiElement();
            if (! psiElementClass.isAssignableFrom(element.getClass())) {
                throw new IncorrectOperationException("Mismatch between element type being removed vs. the element intended for removal");
            }
            element.delete();
        });
    }

    @RequiredArgsConstructor(staticName = "of")
    public static class GenericTypeSafeQuickfix<T extends PsiElement> implements Deb822TypeSafeLocalQuickFix<T> {

        @IntentionFamilyName
        @Getter
        private final String familyName;
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

    private static <T extends PsiElement> Deb822TypeSafeLocalQuickFix<T> psiReplacement(@IntentionFamilyName String familyName, Function<Project, T> replacementGenerator) {
        return GenericTypeSafeQuickfix.of(familyName, replacementGenerator, (p, descriptor, replacement) -> {
            PsiElement originalElement = descriptor.getPsiElement();
            originalElement.replace(replacement);
        });
    }

}
