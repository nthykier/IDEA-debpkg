package com.github.nthykier.debpkg.util;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface Deb822LocalQuickFix extends LocalQuickFix {

    @NotNull
    String getBaseName();

    @Override
    default @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
        return Deb822Bundle.message("deb822.files.quickfix.fields." + getBaseName() + ".name");
    }

    static Deb822LocalQuickFix of(String baseName, BiConsumer<Project, ProblemDescriptor> fixer) {
        return new Deb822LocalQuickFixImpl(baseName, fixer);
    }
}
