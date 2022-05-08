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

public class DCtrlPackageNameImpliesDifferentSectionInspection extends AbstractDctrlInspection {

    private static final Heuristic DEBUG_PACKAGE_HEURISTIC = Heuristic.of( "debug", endsWith("-dbg", "-dbgsym"));

    // Hoisted from lintian
    // Order matters - first "match" decides.
    private static final Heuristic[] HEURISTICS = {
            Heuristic.of( "doc", endsWith("-doc", "-docs")),
            DEBUG_PACKAGE_HEURISTIC,
            Heuristic.of( "httpd", startsWith("libapache2-mod-", "libnginx-mod-", "lighttpd-mod")),
            Heuristic.of( "gnustep", startsWith("gnustep-")),
            Heuristic.of( "gnustep", Pattern.compile("\\.(?:framework|tool|app)(?:-common)?$")),
            Heuristic.of( "embedded", startsWith("moblin-")),
            Heuristic.of( "javascript", startsWith("node-")),
            Heuristic.of( "zope", startsWith("python-zope", "zope")),
            Heuristic.of( "python", startsWith("python-", "python3-")),
            Heuristic.of( "gnu-r", startsWith("r-cran-", "r-bioc-", "r-other-")),
            Heuristic.of( "editors", startsWith("elpa-")),
            Heuristic.of( "lisp", startsWith("cl-", "guile-")),
            Heuristic.of( "lisp", startsWith("lib").and(endsWith("-guile"))),
            Heuristic.of( "lisp", Pattern.compile("elisp(?:-.*)?$")),
            Heuristic.of( "perl", startsWith("lib").and(endsWith("-perl"))),
            Heuristic.of( "cli-mono", startsWith("lib").and(endsWith("-cil", "-cil-dev"))),
            Heuristic.of( "java", startsWith("lib").and(endsWith("-java", "-jni", "-gcj"))),
            Heuristic.of( "php", startsWith("lib-").and(endsWith("php"))),
            Heuristic.of( "php", Pattern.compile("^(?:lib)?php(?:\\d(?:\\.\\d)?)?-")),
            Heuristic.of( "haskell", startsWith("haskell-", "libhugs-", "libghc6-")),
            Heuristic.of( "ruby", startsWith("ruby-")),
            Heuristic.of( "ruby", Pattern.compile("^lib.*-ruby(?:1\\.\\d)?$")),
            Heuristic.of( "rust", startsWith("rust-").or(startsWith("librust-").and(endsWith("-dev")))),
            Heuristic.of( "ocaml", startsWith("lib-").and(endsWith("-ocaml-dev", "-camlp4-dev"))),
            Heuristic.of( "javascript", startsWith("libjs-")),
            Heuristic.of( "interpreters", startsWith("lib").and(endsWith("-tcl", "-lua", "-gst"))),
            Heuristic.of( "introspection", Pattern.compile("^gir\\d+\\.\\d+-.*-\\d+\\.\\d+$")),
            Heuristic.of( "fonts", startsWith("fonts-", "xfonts-", "ttf-")),
            Heuristic.of( "admin", startsWith("libnss-", "libpam-")),
            Heuristic.of( "localization", startsWith("aspell-", "hunspell-", "myspell-", "mythes-", "dict-freedict-", "gcompris-sound-")),
            Heuristic.of( "localization", Pattern.compile("^hyphen-[a-z]{2}(?:-[a-z]{2})?$")),
            Heuristic.of( "localization", Pattern.compile("-l10n(?:-.*)?$")),
            Heuristic.of( "kernel", endsWith("-dkms", "-firmware")),
            Heuristic.of( "libdevel", startsWith("lib").and(endsWith("-dev", "-headers"))),
            Heuristic.of( "libs", Pattern.compile("^lib.*\\d[ad]?$")),
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
        String sectionValue = deb822Paragraph.getFieldValue("Section");
        if (sectionValue == null || sectionValue.equals("oldlibs")) {
            // oldlibs is a bit of a "free for all".
            return;
        }
        String packageName = deb822Paragraph.getFieldValue("Package");
        Deb822FieldValuePair sectionFVPair = deb822Paragraph.getFieldValuePair("Section");
        String sectionPrefix = "";
        assert sectionFVPair != null && sectionFVPair.getValueParts() != null;

        if (sectionValue.contains("/")) {
            String[] parts = sectionValue.split("/", 2);
            sectionPrefix = parts[0] + "/";
            sectionValue = parts[1];
        }

        Heuristic matchingHeuristic = Arrays.stream(HEURISTICS)
                .filter(h -> h.packageNamePredicate.test(packageName))
                .findFirst()
                .orElse(null);
        if (matchingHeuristic != null && !sectionValue.equals(matchingHeuristic.section)) {
            String newValue = sectionPrefix + matchingHeuristic.section;
            holder.registerProblem(
                    sectionFVPair,
                    Deb822Bundle.message("deb822.files.inspection.dctrl-package-name-implies-different-section.description", newValue),
                    ProblemHighlightType.WARNING,
                    sectionFVPair.getValueParts().getTextRangeInParent(),
                    AnnotatorUtil.replaceFieldValueReplacementFix(
                            Deb822Bundle.message("deb822.files.quickfix.fields.set-section-to-new-value.name", newValue),
                            Deb822Bundle.message("deb822.files.quickfix.fields.set-section-to-new-value.familyName"),
                            newValue)
            );
        } else if (DEBUG_PACKAGE_HEURISTIC.section.equals(sectionValue) && !DEBUG_PACKAGE_HEURISTIC.packageNamePredicate.test(packageName)) {
            // For the debug section, packages are also expected to be named -dbg/-dbgsym
            holder.registerProblem(
                    sectionFVPair,
                    Deb822Bundle.message("deb822.files.inspection.dctrl-debug-section-implies-different-package-name.description"),
                    ProblemHighlightType.WARNING,
                    sectionFVPair.getValueParts().getTextRangeInParent()
            );
        }
    }

    @Data(staticConstructor = "of")
    private static class Heuristic {
        private final String section;
        private final Predicate<String> packageNamePredicate;


        public static Heuristic of(String section, Pattern pattern) {
            // Java 11 - simplify to the built-in asMatchPredicate()
            return of(section, (s) -> pattern.matcher(s).matches());
        }
    }

    private static Predicate<String> startsWith(String ... values) {
        for (String v : values) {
            if (v.startsWith("-") || v.startsWith("^")) {
                throw new IllegalArgumentException("Packages never start with \"-\" / \"^\". Bad pattern: " + v);
            }
        }
        return (s) -> Arrays.stream(values).anyMatch(s::startsWith);
    }

    private static Predicate<String> endsWith(String ... values) {
        for (String v : values) {
            if (v.endsWith("-") || v.endsWith("$")) {
                throw new IllegalArgumentException("Packages never ends with \"-\" / \"$\". Bad pattern: " + v);
            }
        }
        return (s) -> Arrays.stream(values).anyMatch(s::endsWith);
    }

}
