package com.github.nthykier.debpkg.deb822.field;

import com.github.nthykier.debpkg.deb822.field.impl.Deb822KnownFieldImpl;

import java.util.Collections;

public class KnownFields {

    private KnownFields() {}

    public static final Deb822KnownField NULL_FIELD = new Deb822KnownFieldImpl(
            "DUMMY-FIELD", Deb822KnownFieldValueType.FREE_TEXT_VALUE,
            false, Collections.emptyMap(), "Dummy field; if you see this text, it is a bug!",
            false, null, false
    );
}
