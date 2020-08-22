package com.github.nthykier.debpkg.deb822.field;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Deb822KnownRelationField extends Deb822KnownField {

    @NotNull Set<String> supportedVersionOperators();

    boolean supportsBuildProfileRestriction();

}
