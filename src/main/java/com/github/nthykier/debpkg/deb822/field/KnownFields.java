package com.github.nthykier.debpkg.deb822.field;

import com.github.nthykier.debpkg.deb822.field.impl.Deb822KnownFieldImpl;

import java.util.Collections;
import java.util.Set;

public class KnownFields {

    private KnownFields() {}

    public static final String ANY_PARAGRAPH = "ANY";

    public static final Set<String> ANY_PARAGRAPH_TYPES = Collections.singleton(ANY_PARAGRAPH);

    public static final Deb822KnownField NULL_FIELD = new Deb822KnownFieldImpl(
            "DUMMY-FIELD", Deb822KnownFieldValueType.FREE_TEXT_VALUE,
            Deb822KnownFieldValueLanguage.REGULAR_FIELD_VALUE, false, Collections.emptyMap(),
            "Dummy field; if you see this text, it is a bug!",
            false, null, false, ANY_PARAGRAPH_TYPES, false,
            false
    );
}
