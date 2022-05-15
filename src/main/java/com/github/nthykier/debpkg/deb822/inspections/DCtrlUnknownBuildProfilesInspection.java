package com.github.nthykier.debpkg.deb822.inspections;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.deb822.Deb822DataSets;
import com.github.nthykier.debpkg.deb822.psi.*;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.github.nthykier.debpkg.util.StringUtil;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class DCtrlUnknownBuildProfilesInspection extends AbstractDctrlInspection {

    // Set.of in Java 11.
    private static final Set<String> KNOWN_PROFILES = Deb822DataSets.getDataSet("build-profiles");


    private static final Pattern PER_PACKAGE_PROFILE_PATTERN = Pattern.compile("pkg[.][a-z0-9][a-z0-9.+-]+[.][a-z0-9-]+");

    @NotNull
    public PsiElementVisitor inspectionVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new Deb822Visitor() {

            @Override
            public void visitParagraph(@NotNull Deb822Paragraph o) {
                ProgressIndicatorProvider.checkCanceled();
                checkParagraph(holder, o);
            }
        };
    }

    private void checkParagraph(@NotNull final ProblemsHolder holder, @NotNull Deb822Paragraph paragraph) {
        Deb822FieldValuePair field = paragraph.getFieldValuePair("Build-Profiles");
        Deb822ValueParts valueParts = field != null ? field.getValueParts() : null;
        if (valueParts != null) {
            for (Deb822BuildProfileGroup group : valueParts.getBuildProfileGroupList()) {
                for (Deb822BuildProfile buildProfile : group.getBuildProfileList()) {
                    String buildProfileName = buildProfile.getProfileName();
                    if (KNOWN_PROFILES.contains(buildProfileName) || PER_PACKAGE_PROFILE_PATTERN.matcher(buildProfileName).matches()) {
                        continue;
                    }
                    if (buildProfileName.startsWith("pkg.")) {
                        // TODO: warn
                        continue;
                    }
                    List<String> matches = StringUtil.possibleMisspellingOf(buildProfileName, KNOWN_PROFILES, true);
                    if (!matches.isEmpty()) {
                        boolean isNegated = buildProfile.isNegated();
;                       holder.registerProblem(buildProfile,
                                Deb822Bundle.message("deb822.files.inspection.possible-typo-of-value.description"),
                                matches.stream()
                                        .map(match -> AnnotatorUtil.replacementQuickFix(
                                                Deb822Bundle.message("deb822.files.quickfix.change-to.name", match),
                                                Deb822Bundle.message("deb822.files.quickfix.change-to.familyName"),
                                                (p, pd) -> Deb822ElementFactory.createBuildProfileFromText(
                                                        p,
                                                        pd.getPsiElement().getContainingFile().getFileType(),
                                                        isNegated ? "Build-Profiles: <!" + match+ ">" : "Build-Profiles: <" + match+ ">"
                                                ))
                                        ).toArray(LocalQuickFix[]::new)
                        );
                    } else {
                        holder.registerProblem(buildProfile,
                                Deb822Bundle.message("dctrl.files.inspection.dctrl-unknown-build-profile.description")
                        );
                    }

                }
            }

        }
    }

}
