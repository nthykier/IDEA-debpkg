package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.psi.Deb822File;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElementVisitor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
abstract class AbstractDctrlInspection extends LocalInspectionTool {

    private final Language language;

    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        /* This only makes sense for debian/control files, but we are invoked for all Deb822 files */
        if (!language.is(holder.getFile().getLanguage())) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        if (!(holder.getFile() instanceof Deb822File)) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        return inspectionVisitor(holder, isOnTheFly);
    }

    protected abstract PsiElementVisitor inspectionVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly);

}
