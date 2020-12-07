package com.github.nthykier.debpkg.deb822.dialects.dep5;

enum PathType {
    NAME_PART,
    DIRECTORY_SEPARATOR,
    WILDCARD_STAR,
    WILDCARD_QUESTION_MARK;

    public boolean isWildcard() {
        return this == WILDCARD_QUESTION_MARK || this == WILDCARD_STAR;
    }

    public boolean isNamePart() {
        return this == NAME_PART;
    }

    public boolean isDirectorySeparator() {
        return this == DIRECTORY_SEPARATOR;
    }
}