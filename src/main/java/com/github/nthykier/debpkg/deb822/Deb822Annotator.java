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
    private static final Map<String, Set<String>> FIELD_NAMES_TO_KNOWN_KEYWORDS = new HashMap<>();
    private static final Set<String> KEYWORDS_KNOWN_EXCLUSIVELY = new HashSet<>();
    private static final TokenSet VALUE_OR_SUBSTVAR = TokenSet.create(Deb822Types.VALUE, Deb822Types.SUBSTVAR);
    private static final LocalQuickFix MULTI_ARCH_SAME_ARCH_ALL_FIXER = new MultiarchSameArchitectureAllQuickFix();
    private static final LocalQuickFix[] MULTI_ARCH_SAME_ARCH_ALL_FIXER_AS_ARRAY = new LocalQuickFix[]{MULTI_ARCH_SAME_ARCH_ALL_FIXER};

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
                Annotation anno = holder.createErrorAnnotation(pair, "deb822.files.annotator.fields.arch-all-multi-arch-same");
                anno.setTextAttributes(Deb822SyntaxHighlighter.BAD_CHARACTER);
                anno.registerFix(MULTI_ARCH_SAME_ARCH_ALL_FIXER, null, null, new MultiarchSameArchitectureAllProblemDescriptor(pair.getValueParts()));
            }
        }
    }

    private void checkFieldValuePair(@NotNull Deb822FieldValuePair pair, @NotNull AnnotationHolder holder) {
        String keyOrig = pair.getField().getText();
        String key = keyOrig.toLowerCase();
        Set<String> known_keywords_for_field = FIELD_NAMES_TO_KNOWN_KEYWORDS.get(key);
        Deb822ValueParts valueParts;
        String value;
        ASTNode node;
        ASTNode[] childNodes;
        if (known_keywords_for_field == null) {
            return;
        }
        valueParts = pair.getValueParts();
        value = valueParts.getText().trim();
        node = valueParts.getNode();
        childNodes = node.getChildren(VALUE_OR_SUBSTVAR);
        if (known_keywords_for_field.contains(value)) {
            Annotation anno = holder.createInfoAnnotation(pair.getValueParts(), null);
            anno.setTextAttributes(Deb822SyntaxHighlighter.VALUE_KEYWORD);
        } else if (KEYWORDS_KNOWN_EXCLUSIVELY.contains(key) && (childNodes.length != 1 || childNodes[0].getElementType() == Deb822Types.VALUE)) {
            Annotation anno = holder.createErrorAnnotation(pair.getValueParts(),
                    "deb822.files.annotator.fields.unknown.value"
            );
            anno.setTextAttributes(Deb822SyntaxHighlighter.BAD_CHARACTER);
        }
    }

    private static class MultiarchSameArchitectureAllProblemDescriptor extends ProblemDescriptorBase implements ProblemDescriptor {
        public MultiarchSameArchitectureAllProblemDescriptor(@NotNull PsiElement element) {
            super(element, element, Deb822Bundle.message("deb822.files.quickfix.fields.arch-all-multi-arch-same.description"),
                    MULTI_ARCH_SAME_ARCH_ALL_FIXER_AS_ARRAY, ProblemHighlightType.ERROR, false,
                    null, true, false);
        }
    }

    private static class MultiarchSameArchitectureAllQuickFix implements LocalQuickFix {

        @Override
        public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
            return Deb822Bundle.message("deb822.files.quickfix.fields.arch-all-multi-arch-same.name");
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement valueParts = descriptor.getPsiElement();
            Deb822FieldValuePair kvpair = Deb822ElementFactory.createFieldValuePairFromText(project, "Multi-Arch: foreign");
            valueParts.replace(kvpair.getValueParts());
        }
    }

    static void KEYWORDS_FOR_FIELD(String fieldName, boolean exclusive, String... keywords) {
        String fieldLc = fieldName.toLowerCase().intern();
        FIELD_NAMES_TO_KNOWN_KEYWORDS.put(fieldLc, new LinkedHashSet<>(Arrays.asList(keywords)));
        if (exclusive) {
            KEYWORDS_KNOWN_EXCLUSIVELY.add(fieldLc);
        }
    }

    static {
        /* Exclusive */
        KEYWORDS_FOR_FIELD("Multi-Arch", true,"no", "foreign", "same", "allowed");
        KEYWORDS_FOR_FIELD("Priority", true,"extra", "optional", "important", "required");
        KEYWORDS_FOR_FIELD("X-DH-Build-For-Type", true, "host", "target");

        /* Non-exclusive */
        KEYWORDS_FOR_FIELD("Architecture", false,"all", "any");
        KEYWORDS_FOR_FIELD("Rules-Requires-Root", false,"no", "binary-targets");
        KEYWORDS_FOR_FIELD("Section", false,"libs", "oldlibs", "python", "perl", "devel");
    }
}
