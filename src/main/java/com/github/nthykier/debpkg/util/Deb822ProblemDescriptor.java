package com.github.nthykier.debpkg.util;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemDescriptorBase;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Deb822ProblemDescriptor extends ProblemDescriptorBase implements ProblemDescriptor {

    @Getter
    private final String baseName;

    public Deb822ProblemDescriptor(@NotNull LocalQuickFix fixer,
                                   @NotNull String baseName,
                                   @NotNull PsiElement element,
                                   @NotNull ProblemHighlightType highlightType) {
        super(element, element,
                Deb822Bundle.message("deb822.files.quickfix.fields."  + baseName +".description"),
                new LocalQuickFix[]{fixer}, highlightType, false,
                null, true, false
        );
        this.baseName = baseName;
    }
}
