package com.github.nthykier.debpkg.deb822.dialects.dep5;

import com.intellij.openapi.util.TextRange;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
class PathPart {
    private final PathType pathType;
    private final String path;
    private final TextRange textRange;
}