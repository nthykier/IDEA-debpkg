package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.dch.psi.DchChangelogEntry;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class DchStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {


    public DchStructureViewModel(@NotNull PsiFile psiFile) {
        super(psiFile, new DchStructureViewElement(psiFile));
    }


    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element.getValue() instanceof DchChangelogEntry;
    }
}
