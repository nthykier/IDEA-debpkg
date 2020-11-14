package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.AbstractDeb822ParserDefinition;

public class Deb822DialectDebianCopyrightParserDefinition extends AbstractDeb822ParserDefinition {

    public Deb822DialectDebianCopyrightParserDefinition() {
        super(Deb822DialectDebianCopyrightLanguage.INSTANCE, Deb822DialectDebianCopyrightFileType.INSTANCE);
    }
}
