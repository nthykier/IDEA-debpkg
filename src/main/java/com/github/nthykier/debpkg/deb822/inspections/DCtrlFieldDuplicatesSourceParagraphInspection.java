package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.util.ASTNodeStringConverter;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.Language;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DCtrlFieldDuplicatesSourceParagraphInspection extends AbstractDctrlInspection {

    public DCtrlFieldDuplicatesSourceParagraphInspection() {
        super(Deb822DialectDebianControlLanguage.INSTANCE);
    }

    public boolean runForWholeFile() {
        return true;
    }


    @NotNull
    public PsiElementVisitor inspectionVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new Deb822Visitor() {
            public void visitAllParagraphs(@NotNull Deb822AllParagraphs o) {
                ProgressIndicatorProvider.checkCanceled();
                List<Deb822Paragraph> paragraphList = o.getParagraphList();
                Deb822Paragraph sourceParagraph;
                Map<String, Deb822FieldValuePair> sourceFieldMap;
                Map<String, String> sourceValueCache;
                ASTNodeStringConverter stringConverter;
                if (paragraphList.size() < 2) {
                    // Need at least one source and one binary paragraph
                    // for this to do this.
                    return;
                }
                sourceParagraph = paragraphList.get(0);
                sourceFieldMap = sourceParagraph.getFieldMap();
                sourceValueCache = new HashMap<>();
                stringConverter = new ASTNodeStringConverter();
                for (Deb822Paragraph binaryParagraph : (Iterable<Deb822Paragraph>)paragraphList.stream().skip(1)::iterator) {
                    ProgressIndicatorProvider.checkCanceled();
                    checkDuplicateFields(holder, sourceFieldMap, sourceValueCache, binaryParagraph, stringConverter);
                }
            }
        };
    }

    private void checkDuplicateFields(@NotNull final ProblemsHolder holder,
                                      @NotNull Map<String, Deb822FieldValuePair> sourceFieldMap,
                                      @NotNull Map<String, String> sourceValueCache,
                                      @NotNull Deb822Paragraph binaryParagraph,
                                      @NotNull ASTNodeStringConverter stringConverter
    ) {

        for (Map.Entry<String, Deb822FieldValuePair> entry : binaryParagraph.getFieldMap().entrySet()) {
            String fieldNameLC = entry.getKey().toLowerCase();
            Deb822FieldValuePair sourceValuePair = sourceFieldMap.get(fieldNameLC);
            Deb822FieldValuePair binaryValuePair;
            String sourceValue;
            String binaryValue;
            if (sourceValuePair == null) {
                continue;
            }
            binaryValuePair = entry.getValue();
            sourceValue = sourceValueCache.computeIfAbsent(fieldNameLC, k -> getValueFromField(sourceValuePair, stringConverter));
            binaryValue = getValueFromField(binaryValuePair, stringConverter);
            if (sourceValue.equals(binaryValue)) {
                LocalQuickFix[] fixes = new LocalQuickFix[] {
                        AnnotatorUtil.elementRemovalQuickfixer(
                                Deb822Bundle.message("deb822.files.quickfix.fields.dctrl-field-duplicates-source-paragraph-remove-field.name"),
                                Deb822FieldValuePair.class
                        )
                };
                holder.registerProblem(binaryValuePair,
                        Deb822Bundle.message("deb822.files.suggested-field.dctrl-field-duplicates-source-paragraph"),
                        fixes
                );
            }
        }
    }

    private String getValueFromField(@NotNull Deb822FieldValuePair valuePair, @NotNull ASTNodeStringConverter stringConverter) {
        StringBuilder builder = stringConverter.getStringBuilder();
        Deb822ValueParts valueParts = valuePair.getValueParts();
        builder.setLength(0);
        if (valueParts == null) {
            return "";
        }
        stringConverter.readTextFromNode(valueParts.getNode());
        return builder.toString();
    }
}
