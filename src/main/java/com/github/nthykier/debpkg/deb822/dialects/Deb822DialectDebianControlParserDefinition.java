package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.AbstractDeb822ParserDefinition;

public class Deb822DialectDebianControlParserDefinition extends AbstractDeb822ParserDefinition {

    public Deb822DialectDebianControlParserDefinition() {
        super(Deb822DialectDebianControlLanguage.INSTANCE, Deb822DialectDebianControlFileType.INSTANCE);
    }
}
