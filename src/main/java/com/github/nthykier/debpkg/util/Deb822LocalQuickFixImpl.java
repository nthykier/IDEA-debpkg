package com.github.nthykier.debpkg.util;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

@RequiredArgsConstructor(access = AccessLevel.MODULE)
public class Deb822LocalQuickFixImpl implements LocalQuickFix {


    @IntentionName
    @NonNull
    @Getter
    private final String name;

    @IntentionFamilyName
    @NonNull
    @Getter
    private final String familyName;

    @NonNull
    private final BiConsumer<Project, ProblemDescriptor> fixCode;

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        fixCode.accept(project, descriptor);
    }


    static Deb822LocalQuickFixImpl of(String name, BiConsumer<Project, ProblemDescriptor> fixCode) {
        return new Deb822LocalQuickFixImpl(name, name, fixCode);
    }
}
