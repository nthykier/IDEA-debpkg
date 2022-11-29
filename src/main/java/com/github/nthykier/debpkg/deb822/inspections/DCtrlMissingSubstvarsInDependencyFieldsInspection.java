package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.Language;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.SmartList;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DCtrlMissingSubstvarsInDependencyFieldsInspection extends AbstractDctrlInspection {

    public DCtrlMissingSubstvarsInDependencyFieldsInspection() {
        super(Deb822DialectDebianControlLanguage.INSTANCE);
    }

    @Override
    protected PsiElementVisitor inspectionVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new Deb822Visitor() {
            public void visitAllParagraphs(@NotNull Deb822AllParagraphs allParagraphs) {
                ProgressIndicatorProvider.checkCanceled();
                List<Deb822Paragraph> paragraphs = allParagraphs.getParagraphList();
                if (paragraphs.size() > 1) {
                    checkAllParagraphs(holder, paragraphs);
                }

            }
        };
    }

    private void checkAllParagraphs(ProblemsHolder holder, @NotNull List<Deb822Paragraph> allParagraphs) {
        Deb822Paragraph source = allParagraphs.get(0);
        assert source.classifyParagraph().equals(Deb822DialectDebianControlLanguage.PARAGRAPH_TYPE_SOURCE);
        for (Deb822Paragraph packageParagraph : allParagraphs.subList(1, allParagraphs.size())) {
            if (!packageParagraph.classifyParagraph().equals(Deb822DialectDebianControlLanguage.PARAGRAPH_TYPE_BINARY_PACKAGE)) {
                continue;
            }
            String architecture = packageParagraph.getFieldValue("Architecture");
            if (architecture == null || architecture.equals("all")) {
                continue;
            }
            PsiElement packageField = packageParagraph.getNameIdentifier();
            if (packageField == null) {
                // Let's not bother with a paragraph without a package field.
                continue;
            }
            Deb822FieldValuePair depends = packageParagraph.getFieldValuePair("Depends");

            if (depends == null) {
                holder.registerProblem(
                        packageField,
                        Deb822Bundle.message("deb822.files.inspection.dctrl-likely-missing-shlibs-depends-in-depends-field.description"),
                        ProblemHighlightType.WARNING
                );
                continue;
            }
            List<Deb822Substvar> subvars = depends.getValueParts() != null ? depends.getValueParts().getSubstvarList() : Collections.emptyList();
            if (subvars.stream().map(Deb822Substvar::getText).noneMatch("${shlibs:Depends}"::equals)) {
                holder.registerProblem(
                        depends,
                        Deb822Bundle.message("deb822.files.inspection.dctrl-likely-missing-shlibs-depends-in-depends-field.description"),
                        ProblemHighlightType.WARNING
                );
            }
        }
    }
}
