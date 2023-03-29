package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.AbstractDeb822ParserDefinition;

public class Deb822DialectAptSourcesParserDefinition extends AbstractDeb822ParserDefinition {

    public Deb822DialectAptSourcesParserDefinition() {
        super(Deb822DialectAptSourcesFileType.INSTANCE);
    }
}
