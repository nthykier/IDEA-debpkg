package com.github.nthykier.debpkg.deb822.field;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface Deb822KnownField {

    @NotNull String getCanonicalFieldName();

    boolean hasKnownValues();

    boolean areAllKeywordsKnown();

    @NotNull
    Set<String> getKnownKeywords();

    @Nullable
    Deb822KnownFieldKeyword getKeyword(String name);

    @Contract("_, !null -> !null")
    default Deb822KnownFieldKeyword getKeyword(String name, Deb822KnownFieldKeyword defaultValue) {
        Deb822KnownFieldKeyword value = getKeyword(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    @Nullable String getFieldDescription();

    boolean supportsSubstsvars();

    @NotNull
    Deb822KnownFieldValueType getFieldValueType();

    /**
     * The default value of this field if known.
     *
     * @return The default value for this field (if known) or null (if not known)
     */
    @Nullable
    String getDefaultValue();

    /**
     * Whether it is customary to omit the field if set to the default value.
     *
     * @return true if the field should be omitted when set to the default.
     */
    boolean warnIfSetToDefault();
}
