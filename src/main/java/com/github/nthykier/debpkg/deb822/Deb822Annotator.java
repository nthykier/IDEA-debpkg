package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.intellij.codeInspection.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Deb822Annotator implements Annotator {
    private static final TokenSet VALUE_OR_SUBSTVAR = TokenSet.create(Deb822Types.VALUE, Deb822Types.SUBSTVAR);
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
        String value;
        ASTNode node;
        ASTNode[] childNodes;

        /* Ignore unknown fields or fields where we have no knowledge of the values (e.g. Description) */
        if (knownField == null || !knownField.hasKnownValues()) {
            return;
        }
        valueParts = pair.getValueParts();
        value = valueParts.getText().trim();
        node = valueParts.getNode();
        childNodes = node.getChildren(VALUE_OR_SUBSTVAR);
        if (knownField.getKnownKeywords().contains(value)) {
            Annotation anno = holder.createInfoAnnotation(pair.getValueParts(), null);
            anno.setTextAttributes(Deb822SyntaxHighlighter.VALUE_KEYWORD);
            if (knownField.getCanonicalFieldName().equals("Priority") && value.equals("extra")) {
                Annotation weakAnno = holder.createWeakWarningAnnotation(valueParts, PRIORITY_EXTRA_IS_OBSOLETE_FIXER.getAnnotationText());
                weakAnno.registerFix(PRIORITY_EXTRA_IS_OBSOLETE_FIXER, null, null, new PriorityExtraIsObsoleteProblemDescriptor(pair.getValueParts()));
            }
        } else if (knownField.areAllKeywordsKnown() && (childNodes.length != 1 || childNodes[0].getElementType() == Deb822Types.VALUE)) {
            Annotation anno = holder.createErrorAnnotation(pair.getValueParts(),
                    "deb822.files.annotator.fields.unknown.value"
            );
            anno.setTextAttributes(Deb822SyntaxHighlighter.BAD_CHARACTER);
        }
    }

    private static abstract class FieldValueReplacingLocalQuickFix implements LocalQuickFix {

        protected abstract String getBaseName();
        protected abstract String getCorrectionString();

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement valueParts = descriptor.getPsiElement();
            Deb822FieldValuePair kvpair = Deb822ElementFactory.createFieldValuePairFromText(project, getCorrectionString());
            valueParts.replace(kvpair.getValueParts());
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
