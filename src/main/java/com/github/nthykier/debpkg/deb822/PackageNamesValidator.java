package com.github.nthykier.debpkg.deb822;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.github.nthykier.debpkg.util.Constants.PACKAGE_NAME_REGEX;

public class PackageNamesValidator implements NamesValidator {

    @Override
    public boolean isKeyword(@NotNull String name, Project project) {
        return false;
    }

    @Override
    public boolean isIdentifier(@NotNull String name, Project project) {
        return PACKAGE_NAME_REGEX.matcher(name).matches();
    }
}
