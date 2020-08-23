package com.github.nthykier.debpkg.util;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.psi.Deb822ElementFactory;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemDescriptorBase;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public class AnnotatorUtil {

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
        ProblemDescriptor problemDescriptor = new Deb822ProblemDescriptor<>(quickFix, baseName, elementToFix, highlightType);
        annoHolder.newAnnotation(severity, getAnnotationText(baseName, params))
                .range(elementToFix)
                .newLocalQuickFix(quickFix, problemDescriptor).registerFix()
                .create();
    }

    @NotNull
    public static <T extends PsiElement> Function<String, Deb822TypeSafeLocalQuickFix<T>> replacementQuickFixer(final Function<Project, T> factoryMethod) {
        return (String s) -> new PsiElementReplacingLocalQuickFix<>(s, factoryMethod);
    }

    @NotNull
    public static <T extends PsiElement> Function<String, Deb822TypeSafeLocalQuickFix<T>> elementRemovalQuickfixer(final Class<T> clazz) {
        return (String s) -> new FieldDeletingQuickFix<>(s, clazz);
    }

    @NotNull
    public static <T extends Deb822FieldValuePair> Function<String, Deb822TypeSafeLocalQuickFix<T>> fieldInsertionQuickFix(final Function<Project, T> factoryMethod, String... insertRelativeTo) {
        return (String s) -> new InsertFieldInParagraphLocalQuickFix<>(s, factoryMethod, insertRelativeTo);
    }


    @NotNull
    @Nls(capitalization = Nls.Capitalization.Sentence)
    public static String getAnnotationText(@NotNull String baseName, Object... params) {
        return Deb822Bundle.message("deb822.files.annotator.fields." + baseName, params);
    }

    public static abstract class AbstractDeb822TypeSafeLocalQuickFix<T extends PsiElement> implements Deb822TypeSafeLocalQuickFix<T> {
        private final String baseName;

        protected AbstractDeb822TypeSafeLocalQuickFix(String baseName) {
           this.baseName = baseName;
        }

        @NotNull
        @Override
        public String getBaseName() {
            return this.baseName;
        }
    }

    public static class Deb822ProblemDescriptor<T extends PsiElement> extends ProblemDescriptorBase implements ProblemDescriptor {
        public Deb822ProblemDescriptor(@NotNull LocalQuickFix fixer,
                                       @NotNull String baseName,
                                       @NotNull T element,
                                       @NotNull ProblemHighlightType highlightType) {
            super(element, element,
                    Deb822Bundle.message("deb822.files.quickfix.fields."  + baseName +".description"),
                    new LocalQuickFix[]{fixer}, highlightType, false,
                    null, true, false
            );
        }
    }

    private static class FieldDeletingQuickFix<T extends PsiElement> extends AnnotatorUtil.AbstractDeb822TypeSafeLocalQuickFix<T> {

        private final Class<T> elementClass;

        FieldDeletingQuickFix(String baseName, Class<T> elementClass) {
            super(baseName);
            this.elementClass = elementClass;
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement element = descriptor.getPsiElement();
            if (! elementClass.isAssignableFrom(element.getClass())) {
                throw new IncorrectOperationException("Mismatch between element type being removed vs. the element intended for removal");
            }
            element.delete();
        }
    }

    public static abstract class AbstractPsiElementReplacingLocalQuickFix<T extends PsiElement> implements Deb822TypeSafeLocalQuickFix<T> {

        @NotNull
        protected abstract T getCorrectedElement(@NotNull Project project);

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement valueParts = descriptor.getPsiElement();
            T replacement = getCorrectedElement(project);
            valueParts.replace(Objects.requireNonNull(replacement, "Bug in "
                    + this.getClass().getCanonicalName() + " - Replacement is null"));
        }

        @Override
        public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
            return Deb822Bundle.message("deb822.files.quickfix.fields." + getBaseName() + ".name");
        }
    }

    private static class PsiElementReplacingLocalQuickFix<T extends PsiElement> extends AbstractPsiElementReplacingLocalQuickFix<T> {

        private final String basename;
        private final Function<Project, T> elementGenerator;

        PsiElementReplacingLocalQuickFix(@NotNull String basename, @NotNull Function<Project, T> elementGenerator) {
            this.basename = basename;
            this.elementGenerator = elementGenerator;
        }

        @NotNull
        public String getBaseName() {
            return this.basename;
        }

        @NotNull
        protected T getCorrectedElement(@NotNull Project project) {
            return this.elementGenerator.apply(project);
        }
    }

    private static class InsertFieldInParagraphLocalQuickFix<T extends Deb822FieldValuePair> implements Deb822TypeSafeLocalQuickFix<T> {

        private final String basename;
        private final Function<Project, T> elementGenerator;
        private final String[] beforeFields;

        InsertFieldInParagraphLocalQuickFix(@NotNull String basename, @NotNull Function<Project, T> elementGenerator, String... beforeFields) {
            this.basename = basename;
            this.elementGenerator = elementGenerator;
            this.beforeFields = beforeFields;
        }

        @NotNull
        protected T getCorrectedElement(@NotNull Project project) {
            return Objects.requireNonNull(this.elementGenerator.apply(project),"Bug in "
                    + this.getClass().getCanonicalName() + " - Replacement is null");
        }

        @NotNull
        public String getBaseName() {
            return this.basename;
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            Deb822Paragraph paragraph = Deb822PsiImplUtil.getAncestorOfType(descriptor.getPsiElement(), Deb822Paragraph.class);
            T newField;
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

            newField = getCorrectedElement(project);
            if (insertRelativeTo != null) {
                paragraph.addBefore(newField, insertRelativeTo);
                paragraph.addBefore(whitespace, insertRelativeTo);
            } else {
                paragraph.add(newField);
                paragraph.add(whitespace);
            }
        }
    }
}
