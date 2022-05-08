package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.psi.Deb822FieldValuePair;
import com.github.nthykier.debpkg.deb822.psi.Deb822Paragraph;
import com.github.nthykier.debpkg.deb822.psi.Deb822Visitor;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class DCtrlPackageNameImpliesDifferentArchInspection extends AbstractDctrlInspection {

    private static final Heuristic[] HEURISTICS = {
            Heuristic.of("Documentation package (-doc/-docs)", p -> p.endsWith("-doc") || p.endsWith("-docs")),
            Heuristic.of("Dictionary/wordlist package for aspell (aspell-[a-z]{2} and similar)", Pattern.compile("^aspell-[a-z]{2}(?:-.*)?$")),
    };

    @Override
    protected PsiElementVisitor inspectionVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new Deb822Visitor() {
            public void visitParagraph(@NotNull Deb822Paragraph deb822Paragraph) {
                ProgressIndicatorProvider.checkCanceled();
                if (deb822Paragraph.classifyParagraph().equals(Deb822DialectDebianControlLanguage.PARAGRAPH_TYPE_BINARY_PACKAGE)) {
                    checkBinaryParagraph(holder, deb822Paragraph);
                }
            }
        };
    }

    private void checkBinaryParagraph(ProblemsHolder holder, Deb822Paragraph deb822Paragraph) {
        String architectureValue = deb822Paragraph.getFieldValue("Architecture");
        if (architectureValue == null || architectureValue.equals("all")) {
            return;
        }
        String packageName = deb822Paragraph.getFieldValue("Package");
        Deb822FieldValuePair architectureFVPair = deb822Paragraph.getFieldValuePair("Architecture");
        assert architectureFVPair != null && architectureFVPair.getValueParts() != null;

        Arrays.stream(HEURISTICS)
                .filter(h -> h.packageNamePredicate.test(packageName))
                .findFirst()
                .ifPresent(matchingHeuristic -> holder.registerProblem(
                        architectureFVPair,
                        Deb822Bundle.message("deb822.files.inspection.dctrl-package-name-implies-different-arch.description"),
                        ProblemHighlightType.WARNING,
                        architectureFVPair.getValueParts().getTextRangeInParent(),
                        AnnotatorUtil.replaceFieldValueReplacementFix(
                                Deb822Bundle.message("deb822.files.quickfix.fields.set-architecture-to-all.name"),
                                "all")
                ));

    }

    @Data(staticConstructor = "of")
    private static class Heuristic {
        private final String classification;
        private final Predicate<String> packageNamePredicate;


        public static Heuristic of(String classification, Pattern pattern) {
            // Java 11 - simplify to the built-in asMatchPredicate()
            return of(classification, (s) -> pattern.matcher(s).matches());
        }
    }
}
