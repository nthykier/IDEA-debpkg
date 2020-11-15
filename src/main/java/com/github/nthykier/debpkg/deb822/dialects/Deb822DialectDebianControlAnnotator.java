package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.field.*;
import com.github.nthykier.debpkg.deb822.Deb822KnownFieldsAndValues;
import com.github.nthykier.debpkg.deb822.Deb822SyntaxHighlighter;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.github.nthykier.debpkg.util.Deb822TypeSafeLocalQuickFix;
import com.intellij.codeInspection.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class Deb822DialectDebianControlAnnotator implements Annotator {

    private static final TokenSet SPACE_OR_COMMA = TokenSet.create(TokenType.WHITE_SPACE, Deb822Types.COMMA);

    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof Deb822Paragraph) {
            checkParagraph(holder, (Deb822Paragraph)element);
        }
    }

    private void checkMultiArchVsArchitecture(@NotNull AnnotationHolder holder, @NotNull Map<String, Deb822FieldValuePair> fieldMap) {
        Deb822FieldValuePair archFieldPair = fieldMap.get("architecture");
        Deb822FieldValuePair multiarchFieldPair = fieldMap.get("multi-arch");
        Deb822ValueParts archValueParts, multiarchValueParts;
        String arch, multiarchValue;
        if (archFieldPair == null || multiarchFieldPair == null) {
            return;
        }
        archValueParts = archFieldPair.getValueParts();
        multiarchValueParts = multiarchFieldPair.getValueParts();
        if (archValueParts == null || multiarchValueParts == null) {
            return;
        }
        arch = archValueParts.getText().trim();
        multiarchValue = multiarchValueParts.getText().trim();
        if (arch.equals("all") && multiarchValue.equals("same")) {
            Function<String, Deb822TypeSafeLocalQuickFix<Deb822ValueParts>> quickfixer =
                    AnnotatorUtil.replacementQuickFixer(
                            (Project p) -> Deb822ElementFactory.createValuePartsFromText(p, "Multi-Arch: foreign")
                    );

            AnnotatorUtil.createAnnotationWithQuickFix(
                    holder,
                    HighlightSeverity.ERROR,
                    quickfixer,
                    "arch-all-multi-arch-same",
                    multiarchFieldPair.getValueParts(),
                    ProblemHighlightType.ERROR
            );
        }
    }

    private void checkParagraph(@NotNull AnnotationHolder holder, @NotNull Deb822Paragraph paragraph) {
        Map<String, Deb822FieldValuePair> field2pair = new HashMap<>();
        KnownFieldTable knownFieldTable = Deb822DialectDebianControlLanguage.INSTANCE.getKnownFieldTable();
        String paragraphType = paragraph.isFirstParagraph() ? "Source" : "Package";
        Map<String, Deb822FieldValuePair> fieldMap = paragraph.getFieldMap();
        for (Deb822FieldValuePair pair : fieldMap.values()) {
            String keyOrig = pair.getField().getFieldName();
            String keyLc = keyOrig.toLowerCase();
            checkFieldValuePair(knownFieldTable, pair, holder);
            field2pair.putIfAbsent(keyLc, pair);
        }
        checkMultiArchVsArchitecture(holder, fieldMap);
        if (! field2pair.containsKey(paragraphType.toLowerCase())) {
            holder.newAnnotation(HighlightSeverity.ERROR, Deb822Bundle.message("deb822.files.annotator.fields.missing-mandatory-field", paragraphType))
                    .range(paragraph)
                    .create();
        }
    }

    private void checkFieldValuePair(@NotNull KnownFieldTable knownFieldTable,
                                     @NotNull Deb822FieldValuePair pair,
                                     @NotNull AnnotationHolder holder) {
        String fieldName = pair.getField().getFieldName();
        Deb822KnownField knownField = knownFieldTable.getField(fieldName);
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
                holder.newAnnotation(HighlightSeverity.ERROR, Deb822Bundle.message("deb822.files.annotator.fields.substvars.not.supported"))
                        .range(substvar)
                        .create();
            }
            /* Additional errors here are not useful */
            return;
        }
        this.validateFieldValue(knownField, pair, valueParts, holder);
    }

    private void validateFieldValue(@NotNull Deb822KnownField knownField,
                                    @NotNull Deb822FieldValuePair pair,
                                    @NotNull Deb822ValueParts valueParts,
                                    @NotNull AnnotationHolder holder) {

        Deb822KnownFieldValueType fieldValueType = knownField.getFieldValueType();
        List<List<ASTNode>> parts;

        if (fieldValueType == Deb822KnownFieldValueType.FREE_TEXT_VALUE) {
            return;
        }
        parts = fieldValueType.splitValue(valueParts);

        for (List<ASTNode> valueTokens : parts) {
            validateFieldToken(knownField, valueParts, valueTokens, holder, parts.size() == 1);
        }
        if (knownField.warnIfSetToDefault()) {
            String value = valueParts.getText().trim();
            if (value.equals(knownField.getDefaultValue())) {
                AnnotatorUtil.createAnnotationWithQuickFix(
                        holder,
                        HighlightSeverity.WARNING,
                        AnnotatorUtil.elementRemovalQuickfixer(Deb822FieldValuePair.class),
                        "field-is-unnecessary-when-value-is-default",
                        pair,
                        ProblemHighlightType.WARNING,
                        value, knownField.getCanonicalFieldName()
                );
            }
        }
    }

    private static void validateFieldToken(@NotNull Deb822KnownField field,
                                           @NotNull Deb822ValueParts valueParts,
                                           @NotNull List<ASTNode> valueTokens,
                                           @NotNull AnnotationHolder holder,
                                           boolean isSoleValue
    ) {
        String value = null;
        ASTNode token;
        IElementType elementType = null;
        Deb822KnownFieldKeyword knownFieldKeyword = null;
        if (valueTokens.size() == 0) {
            // FIXME: warn about duplicate separators at the concrete separator
            holder.newAnnotation(HighlightSeverity.ERROR, Deb822Bundle.message("deb822.files.annotator.fields.empty.list.value"))
                    .range(valueParts)
                    .create();
            /* not much else we can say here */
            return;
        }
        if (valueTokens.size() == 1) {
            token = valueTokens.get(0);
            elementType = token.getElementType();
            if (elementType == Deb822Types.VALUE) {
                value = token.getText();
                knownFieldKeyword = field.getKeyword(value);
            }
            if (knownFieldKeyword != null) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(token)
                        .textAttributes(Deb822SyntaxHighlighter.VALUE_KEYWORD)
                        .create();
                if (knownFieldKeyword.isExclusive() && !isSoleValue) {
                    holder.newAnnotation(HighlightSeverity.ERROR, Deb822Bundle.message("deb822.files.annotator.fields.value-is-exclusive",
                            knownFieldKeyword.getValueName(), field.getCanonicalFieldName()))
                            .range(token)
                            .create();
                }
                if (field.getCanonicalFieldName().equals("Priority") && value.equals("extra")) {
                    Function<String, Deb822TypeSafeLocalQuickFix<Deb822ValueParts>> quickfixer =
                            AnnotatorUtil.replacementQuickFixer(
                                    (Project p) -> Deb822ElementFactory.createValuePartsFromText(p, "Priority: optional")
                            );

                    AnnotatorUtil.createAnnotationWithQuickFix(
                            holder,
                            HighlightSeverity.WARNING,
                            quickfixer,
                            "priority-extra-is-obsolete",
                            valueParts,
                            ProblemHighlightType.WEAK_WARNING
                    );
                }
                /* Validated; skip to next element */
                return;
            }
        }
        if (field.getFieldValueType() == Deb822KnownFieldValueType.SINGLE_KEYWORD
                || field.getFieldValueType() == Deb822KnownFieldValueType.SINGLE_TRIVIAL_VALUE) {
            int i = 0;
            final int size = valueTokens.size();
            for (; i < size ; i++) {
                ASTNode e = valueTokens.get(i);
                IElementType et = e.getElementType();
                if (SPACE_OR_COMMA.contains(et)) {
                    /* not a single value then */
                    holder.newAnnotation(
                            HighlightSeverity.ERROR,
                            Deb822Bundle.message("deb822.files.annotator.fields.field-is-single-value-field")
                    )
                            .range(e)
                            .create();
                    return;
                }
            }
        }
        /* Not a known keyword - forgive substvars through */
        if (field.areAllKeywordsKnown() && !(Deb822Types.SUBSTVAR_TOKEN.equals(elementType))) {
            holder.newAnnotation(HighlightSeverity.ERROR, Deb822Bundle.message("deb822.files.annotator.fields.unknown.value"))
                    .range(valueParts)
                    .create();
        }
    }

}
