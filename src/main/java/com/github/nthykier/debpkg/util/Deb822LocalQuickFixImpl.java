package com.github.nthykier.debpkg.util;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

@RequiredArgsConstructor(access = AccessLevel.MODULE)
public class Deb822LocalQuickFixImpl implements Deb822LocalQuickFix {

    @NonNull
    @Getter
    private final String baseName;
    @NonNull
    private final BiConsumer<Project, ProblemDescriptor> fixCode;


    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        fixCode.accept(project, descriptor);
    }


    static Deb822LocalQuickFixImpl of(String baseName, BiConsumer<Project, ProblemDescriptor> fixCode) {
        return new Deb822LocalQuickFixImpl(baseName, fixCode);
    }
}
