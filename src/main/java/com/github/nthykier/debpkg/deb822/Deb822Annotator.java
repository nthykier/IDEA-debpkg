package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.intellij.codeInspection.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Deb822Annotator implements Annotator {
    private static final TokenSet SPACE_OR_COMMA = TokenSet.create(TokenType.WHITE_SPACE, Deb822Types.COMMA);
    private static final FieldValueReplacingLocalQuickFix MULTI_ARCH_SAME_ARCH_ALL_FIXER = new MultiarchSameArchitectureAllQuickFix();
    private static final FieldValueReplacingLocalQuickFix PRIORITY_EXTRA_IS_OBSOLETE_FIXER = new PriorityExtraIsObsoleteQuickFix();

    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof Deb822Paragraph) {
            Deb822Paragraph paragraph = (Deb822Paragraph)element;
            Map<String, String> field2values = new HashMap<>();
            Map<String, Deb822FieldValuePair> field2pair = new HashMap<>();
            String arch, multivalue;
            for (Deb822FieldValuePair pair : paragraph.getFieldValuePairList()) {
                String keyOrig = pair.getField().getText();
                String keyLc = keyOrig.toLowerCase();
                String value = pair.getValueParts().getText().trim();
                checkFieldValuePair(pair, holder);
                field2values.putIfAbsent(keyLc, value);
                field2pair.putIfAbsent(keyLc, pair);
            }
            arch = field2values.getOrDefault("architecture", "any");
            multivalue = field2values.getOrDefault("multi-arch", "no");
            if (arch.equals("all") && multivalue.equals("same")) {
                Deb822FieldValuePair pair = field2pair.get("multi-arch");
                assert pair != null;
                Annotation anno = holder.createErrorAnnotation(pair.getValueParts(),
                        MULTI_ARCH_SAME_ARCH_ALL_FIXER.getAnnotationText());
                anno.registerFix(MULTI_ARCH_SAME_ARCH_ALL_FIXER, null, null, new MultiarchSameArchitectureAllProblemDescriptor(pair.getValueParts()));
            }
        }
    }

    private void checkFieldValuePair(@NotNull Deb822FieldValuePair pair, @NotNull AnnotationHolder holder) {
        String fieldName = pair.getField().getText();
        Deb822KnownField knownField = Deb822KnownFieldsAndValues.lookupDeb822Field(fieldName);
        Deb822ValueParts valueParts;
        List<Deb822Substvar> substvars;

        /* Ignore unknown fields or fields where we have no knowledge of the values (e.g. Description) */
        if (knownField == null) {
            return;
        }
        valueParts = pair.getValueParts();
        if (valueParts == null) {
            /* The parser will flag this as an error already */
            return;
        }
        substvars = valueParts.getSubstvarList();
        if (!knownField.supportsSubstsvars() && !substvars.isEmpty()) {
            for (Deb822Substvar substvar : substvars) {
                holder.createErrorAnnotation(substvar,
                        Deb822Bundle.message("deb822.files.annotator.fields.substvars.not.supported")
                );
            }
            /* Additional errors here are not useful */
            return;
        }
        this.validateFieldValue(knownField, valueParts, holder);
    }

    private void validateFieldValue(@NotNull Deb822KnownField knownField, @NotNull Deb822ValueParts valueParts,
                                    @NotNull AnnotationHolder holder) {

        Deb822KnownFieldValueType fieldValueType = knownField.getFieldValueType();
        List<List<ASTNode>> parts;

        if (fieldValueType == Deb822KnownFieldValueType.FREE_TEXT_VALUE) {
            return;
        }
        parts = fieldValueType.splitValue(valueParts);

        for (List<ASTNode> valueTokens : parts) {
            validateFieldToken(knownField, valueParts, valueTokens, holder);
        }
    }

    private static void validateFieldToken(@NotNull Deb822KnownField field,
                                           @NotNull Deb822ValueParts valueParts,
                                           @NotNull List<ASTNode> valueTokens,
                                           @NotNull AnnotationHolder holder) {
        String value = null;
        ASTNode token = null;
        IElementType elementType = null;
        if (valueTokens.size() == 0) {
            // FIXME: warn about duplicate separators at the concrete separator
            holder.createErrorAnnotation(valueParts,
                    Deb822Bundle.message("deb822.files.annotator.fields.empty.list.value")
            );
            /* not much else we can say here */
            return;
        }
        if (valueTokens.size() == 1) {
            token = valueTokens.get(0);
            elementType = token.getElementType();
            if (elementType == Deb822Types.VALUE) {
                value = token.getText();
            }
            if (value != null && field.getKnownKeywords().contains(value)) {
                Annotation anno = holder.createInfoAnnotation(token, null);
                anno.setTextAttributes(Deb822SyntaxHighlighter.VALUE_KEYWORD);
                if (field.getCanonicalFieldName().equals("Priority") && value.equals("extra")) {
                    Annotation weakAnno = holder.createWeakWarningAnnotation(valueParts, PRIORITY_EXTRA_IS_OBSOLETE_FIXER.getAnnotationText());
                    weakAnno.registerFix(PRIORITY_EXTRA_IS_OBSOLETE_FIXER, null, null, new PriorityExtraIsObsoleteProblemDescriptor(valueParts));
                }
                /* Validated; skip to next element */
                return;
            }
        }
        if (field.getFieldValueType() == Deb822KnownFieldValueType.SINGLE_KEYWORD
                || field.getFieldValueType() == Deb822KnownFieldValueType.SINGLE_TRIVIAL_VALUE) {
            int i = 0;
            final int size = valueTokens.size();
            boolean bad = false;
            for (; i < size ; i++) {
                ASTNode e = valueTokens.get(i);
                if (!bad) {
                    IElementType et = e.getElementType();
                    if (SPACE_OR_COMMA.contains(et)) {
                        /* not a single value then */
                        bad = true;
                    }
                }
                if (bad) {
                    holder.createErrorAnnotation(e,
                            Deb822Bundle.message("deb822.files.annotator.fields.field-is-single-value-field"));
                }
            }
            if (bad) {
                return;
            }
        }
        /* Not a known keyword - forgive substvars through */
        if (field.areAllKeywordsKnown() && !(Deb822Types.SUBSTVAR_TOKEN.equals(elementType))) {
            holder.createErrorAnnotation(valueParts,
                    Deb822Bundle.message("deb822.files.annotator.fields.unknown.value")
            );
        }
    }

    private static abstract class FieldValueReplacingLocalQuickFix implements LocalQuickFix {

        protected abstract String getBaseName();
        protected abstract String getCorrectionString();

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement valueParts = descriptor.getPsiElement();
            Deb822FieldValuePair kvpair = Deb822ElementFactory.createFieldValuePairFromText(project, getCorrectionString());
            valueParts.replace(Objects.requireNonNull(kvpair.getValueParts(), "Bug in "
                    + this.getClass().getCanonicalName() + " - Replacement is null"));
        }

        @Override
        public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
            return Deb822Bundle.message("deb822.files.quickfix.fields." + getBaseName() + ".name");
        }

        public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getAnnotationText() {
            return Deb822Bundle.message("deb822.files.annotator.fields." + getBaseName());
        }

    }

    private static abstract class FieldValueReplacingProblemDescriptor extends ProblemDescriptorBase implements ProblemDescriptor {
        public FieldValueReplacingProblemDescriptor(@NotNull FieldValueReplacingLocalQuickFix fixer, @NotNull PsiElement element,
                                                    @NotNull ProblemHighlightType highlightType) {
            super(element, element,
                  Deb822Bundle.message("deb822.files.quickfix.fields."  + fixer.getBaseName() +".description"),
                  new LocalQuickFix[]{fixer}, highlightType, false,
                    null, true, false
                  );
        }
    }

    private static class MultiarchSameArchitectureAllProblemDescriptor extends FieldValueReplacingProblemDescriptor {
        public MultiarchSameArchitectureAllProblemDescriptor(@NotNull PsiElement element) {
            super(MULTI_ARCH_SAME_ARCH_ALL_FIXER, element, ProblemHighlightType.ERROR);
        }
    }

    private static class MultiarchSameArchitectureAllQuickFix extends FieldValueReplacingLocalQuickFix {

        @Override
        protected String getBaseName() {
            return "arch-all-multi-arch-same";
        }

        @Override
        protected String getCorrectionString() {
            return "Multi-Arch: foreign";
        }
    }

    private static class PriorityExtraIsObsoleteProblemDescriptor extends FieldValueReplacingProblemDescriptor implements ProblemDescriptor {
        public PriorityExtraIsObsoleteProblemDescriptor(@NotNull PsiElement element) {
            super(PRIORITY_EXTRA_IS_OBSOLETE_FIXER, element, ProblemHighlightType.WEAK_WARNING);
        }
    }

    private static class PriorityExtraIsObsoleteQuickFix extends FieldValueReplacingLocalQuickFix {

        @Override
        protected String getBaseName() {
            return "priority-extra-is-obsolete";
        }

        @Override
        protected String getCorrectionString() {
            return "Priority: optional";
        }
    }

}
