package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.psi.Deb822ParagraphSupport;
import org.jetbrains.annotations.NotNull;

public interface Deb822ParagraphClassifier {

    String UNCLASSIFIED = "UNCLASSIFIED";
    Deb822ParagraphClassifier NULL_CLASSIFIER = paragraph -> UNCLASSIFIED;

    @NotNull
    String classifyParagraph(Deb822ParagraphSupport paragraph);
}
