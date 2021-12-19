package com.github.nthykier.debpkg.dch.psi.impl;

import com.github.nthykier.debpkg.dch.psi.DchChangelogEntry;
import com.github.nthykier.debpkg.dch.psi.DchDistribution;
import com.github.nthykier.debpkg.dch.psi.DchSource;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import java.util.List;

public class DchPsiImplUtil {

    public static PsiReference getReference(DchSource dchSource) {
        DchChangelogEntry change = Deb822PsiImplUtil.getAncestorOfType(dchSource, DchChangelogEntry.class);
        if (change == null) {
            return null;
        }
        PsiElement previous = Deb822PsiImplUtil.getPreviousSiblingOfType(change.getPrevSibling(), DchChangelogEntry.class);
        if (previous != null) {
            // Only the latest entry can be renamed (refactor).
            return null;
        }
        List<DchDistribution> dists = change.getVersionLine().getDistributionList();
        // Only UNRELEASED ones can be renamed (refactor).
        if (dists.size() != 1 || !dists.get(0).getText().equals("UNRELEASED")) {
            return null;
        }
        return new DchDCtrlReference(dchSource);
    }

}
