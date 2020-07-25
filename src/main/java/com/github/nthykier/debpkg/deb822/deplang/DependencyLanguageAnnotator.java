package com.github.nthykier.debpkg.deb822.deplang;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvar;
import com.github.nthykier.debpkg.deb822.Deb822KnownSubstvars;
import com.github.nthykier.debpkg.deb822.deplang.psi.*;
import com.github.nthykier.debpkg.deb822.psi.Deb822Substvar;
import com.github.nthykier.debpkg.deb822.psi.Deb822SubstvarBase;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.github.nthykier.debpkg.util.Deb822TypeSafeLocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DependencyLanguageAnnotator implements Annotator {

    private static final Map<String, String> INVALID_VERSION_OPERATORS;
    private static final Pattern PACKAGE_NAME_REGEX = Pattern.compile("[a-z0-9][a-z0-9.+-]+");
    private static final Pattern VERSION_NUMBER_REGEX = Pattern.compile("(?:[0-9]+:)?[0-9][a-z0-9.+~]*([-][a-z0-9.+~-]+)?");

    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof DepLangVersionOperator) {
            checkVersionOperator(holder, (DepLangVersionOperator)element);
        } else if (element instanceof DepLangVersion) {
            checkVersion(holder, (DepLangVersion)element);
        } else if (element instanceof DepLangPackageName) {
            checkPackageName(holder, (DepLangPackageName)element);
        } else if (element instanceof Deb822SubstvarBase) {
            checkSubstvar(holder, (Deb822SubstvarBase)element);
        }
    }

    private void checkSubstvar(@NotNull AnnotationHolder holder, @NotNull Deb822SubstvarBase element) {
        String name = element.getText();
        if (!name.endsWith("}") || !name.startsWith("${")) {
            holder.createErrorAnnotation(element, "");
        }
    }

    private void checkPackageName(@NotNull AnnotationHolder holder, @NotNull DepLangPackageName element) {
        Deb822SubstvarBase substvar = element.getSubstvar();
        String packageName;
        int colonIndex;
        if (substvar != null) {
            // Not checked for now; the substvars allowed depends on whether there are restrictions or not
            // e.g. ${misc:Depends} is ok only when it is on its own.
            /*
            String substvarName = substvar.getText();
            Deb822KnownSubstvar knownSubstvar = Deb822KnownSubstvars.lookupSubstvar(substvarName);
            if (knownSubstvar != null && !substvarName.contains("Package")) {
                holder.createWarningAnnotation(substvar,
                        Deb822Bundle.message("deb822.probably-incorrect-substvar-used-as-package-name", substvarName));
            }
            */
            return;
        }
        packageName = element.getText();
        colonIndex = packageName.indexOf(':');
        if (colonIndex > -1) {
            // Ignore ":any" and other architecture qualifier
            packageName = packageName.substring(0, colonIndex);
        }
        checkStringAgainstPattern(PACKAGE_NAME_REGEX, element, packageName, holder, "deb822.invalid-package-name");
    }

    private void checkStringAgainstPattern(@NotNull Pattern pattern,
                                           @NotNull PsiElement element,
                                           @NotNull String tokenText,
                                           @NotNull AnnotationHolder holder,
                                           @NotNull @PropertyKey(resourceBundle=Deb822Bundle.BUNDLE_NAME) String annotationTextKey) {
        if (!pattern.matcher(tokenText).matches()) {
            Matcher matcher = pattern.matcher(tokenText);
            TextRange brokenRange = textRangeOfBrokenTailBit(matcher, tokenText.length());

            if (brokenRange != null) {
                int startOffset = element.getTextOffset();
                holder.createErrorAnnotation(brokenRange.shiftRight(startOffset),
                        Deb822Bundle.message(annotationTextKey, tokenText));
            } else {
                holder.createErrorAnnotation(element,
                        Deb822Bundle.message(annotationTextKey, tokenText));
            }
        }
    }

    private void checkVersion(@NotNull AnnotationHolder holder, @NotNull DepLangVersion versionToken) {
        Deb822SubstvarBase substvar = versionToken.getSubstvar();
        String versionNumber;
        if (substvar != null) {
            String substvarName = substvar.getText();
            Deb822KnownSubstvar knownSubstvar = Deb822KnownSubstvars.lookupSubstvar(substvarName);
            if (knownSubstvar != null && !substvarName.contains("Version")) {
                holder.createWarningAnnotation(substvar,
                        Deb822Bundle.message("deb822.probably-incorrect-substvar-used-as-version", substvarName));
            }
            return;
        }
        versionNumber = versionToken.getText();
        checkStringAgainstPattern(VERSION_NUMBER_REGEX, versionToken, versionNumber, holder, "deb822.invalid-version-number");
    }

    @Nullable
    private static TextRange textRangeOfBrokenTailBit(@NotNull Matcher matcher, int tokenLength) {
        if (matcher.find() && matcher.start() == 0) {
            return new TextRange(
                    matcher.end(),
                    tokenLength - matcher.end()
            );
        }
        return null;
    }

    private void checkVersionOperator(@NotNull AnnotationHolder holder, @NotNull DepLangVersionOperator operatorToken) {
        String operator = operatorToken.getText();
        String replacement = INVALID_VERSION_OPERATORS.get(operator);
        if (replacement != null) {
            if (replacement.equals("")) {
                // Invalid without replacement
                holder.createErrorAnnotation(operatorToken,
                        Deb822Bundle.message("deb822.files.annotator.fields.unknown-version-operator-in-dependency", operator));
            } else {
                Function<String, Deb822TypeSafeLocalQuickFix<DepLangVersionOperator>> quickfixer =
                        AnnotatorUtil.replacementQuickFixer(
                                (Project p) -> DepLangElementFactory.createVersionPart(p, "debhelper (" + replacement + " 1.0)").getVersionOperator()
                        );

                AnnotatorUtil.createAnnotationWithQuickFix(
                        holder::createErrorAnnotation,
                        quickfixer,
                        "incorrect-version-operator-in-dependency-with-known-replacement",
                        operatorToken,
                        ProblemHighlightType.ERROR,
                        operator, replacement
                );
            }
        }
    }

    static {
        Map<String, String> invalidVersionOperators = new HashMap<>();
        invalidVersionOperators.put("<", "<=");
        invalidVersionOperators.put(">", ">=");
        invalidVersionOperators.put("==", "=");
        invalidVersionOperators.put("!=", "");
        /* SQL variant of "!=" */
        invalidVersionOperators.put("<>", "");
        INVALID_VERSION_OPERATORS = Collections.unmodifiableMap(invalidVersionOperators);
    }
}
