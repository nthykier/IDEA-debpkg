package com.github.nthykier.debpkg.deb822.field;

public enum Deb822KnownFieldValueType {
    /**
     * The field contains exactly one value but value is not constrained to a list of predefined values.
     * The value cannot contain space or comma.
     */
    SINGLE_TRIVIAL_VALUE,
    /**
     * The field contains exactly one keyword (there is a fixed set of values allowed for the field).
     */
    SINGLE_KEYWORD,
    /**
     * The field contains one ore more values separated by space.  Values <i>may</i> (but are not
     * required) to match an optional predefined list of keywords.  Unknown values can be validated
     * against a separate value validator.
     */
    SPACE_SEPARATED_VALUE_LIST,
    /**
     * The field contains one ore more values separated by comma.  Values <i>may</i> (but are not
     * required) to match an optional predefined list of keywords.  Unknown values can be validated
     * against a separate value validator.
     */
    COMMA_SEPARATED_VALUE_LIST,
    /**
     * The field is not known to contain structured data or is known to be a "free-text" field a la
     * the Description field.  This is the default for unknown field types.
     */
    FREE_TEXT_VALUE;
}
