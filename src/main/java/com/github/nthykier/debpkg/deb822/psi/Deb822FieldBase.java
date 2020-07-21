// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.deb822.psi;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Deb822FieldBase extends PsiElement {

    /**
     * The field name
     *
     * More telling name for {@link #getText()}.
     *
     * @return The field name.
     */
    @NotNull
    String getFieldName();

    /**
     * Convenience method for looking the field up as a {@link Deb822KnownField}
     *
     * Note if you are looking up multiple fields, it may be faster to lookup the
     * {@link com.github.nthykier.debpkg.deb822.field.KnownFieldTable} for the relevant
     * language directly.
     *
     * @return The {@link Deb822KnownField} instance for this field in the given Deb822 language
     */
    @Nullable
    Deb822KnownField getDeb822KnownField();
}
