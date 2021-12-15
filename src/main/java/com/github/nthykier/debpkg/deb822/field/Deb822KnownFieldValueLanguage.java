package com.github.nthykier.debpkg.deb822.field;

import com.github.nthykier.debpkg.deb822.deplang.DependencyLanguage;
import com.intellij.lang.Language;
import org.jetbrains.annotations.Nullable;

public enum Deb822KnownFieldValueLanguage {

    /**
     * The field contains exactly one value but value is not constrained to a list of predefined values.
     * The value cannot contain space or comma.
     */
    REGULAR_FIELD_VALUE(null, false),

    /**
     * The field should be parsed as a dependency field for the source package (e.g. Build-Depends)
     */
    DEPENDENCY_LANGUAGE_SOURCE_DEPENDENCY(DependencyLanguage.INSTANCE, false),

    /**
     * The field should be parsed as a negative dependency field for the source package (e.g. Build-Conflicts)
     */
    DEPENDENCY_LANGUAGE_SOURCE_NEGATIVE_DEPENDENCY(DependencyLanguage.INSTANCE, true),

    /**
     * The field should be parsed as a dependency field for a binary package (e.g. Depends)
     */
    DEPENDENCY_LANGUAGE_BINARY_DEPENDENCY(DependencyLanguage.INSTANCE, false),

    /**
     * The field should be parsed as a dependency field for the source package (e.g. Breaks)
     */
    DEPENDENCY_LANGUAGE_BINARY_NEGATIVE_DEPENDENCY(DependencyLanguage.INSTANCE, true),

    /**
     * The field should be parsed as the Provides field for binary packages
     */
    DEPENDENCY_LANGUAGE_BINARY_PROVIDES(DependencyLanguage.INSTANCE, false);

    private final Language language;
    private final boolean negativeDependency;

    Deb822KnownFieldValueLanguage(Language language, boolean isNegativeDependency) {
        this.language = language;
        this.negativeDependency = isNegativeDependency;
        assert !negativeDependency || this.isAKindOfDependencyField();
    }

    /**
     * @return The language to parse this as or null, if the file-based parser is sufficient.
     */
    @Nullable
    public Language getLanguage() {
        return this.language;
    }

    public boolean isAKindOfDependencyField() {
        return language != null && DEPENDENCY_LANGUAGE_BINARY_PROVIDES != this;
    }

    public boolean isPositiveDependency() {
        return !negativeDependency && isAKindOfDependencyField();
    }

    public boolean isNegativeDependency() {
        return negativeDependency;
    }
}
