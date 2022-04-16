package com.github.nthykier.debpkg.deb822.field;

import com.github.nthykier.debpkg.deb822.psi.Deb822ValueParts;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueTypeUtil.*;

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
     *
     * This variant does <i>not</i> accept trailing commas.  Use
     * {@link #COMMA_SEPARATED_VALUE_LIST_TRAILING_COMMA_OK} for that.
     */
    COMMA_SEPARATED_VALUE_LIST,

    /**
     * This is similar to {@link #COMMA_SEPARATED_VALUE_LIST} but accepts values with trailing
     * commas.
     *
     * @see #COMMA_SEPARATED_VALUE_LIST
     */
    COMMA_SEPARATED_VALUE_LIST_TRAILING_COMMA_OK,

    /**
     * The field is not known to contain structured data or is known to be a "free-text" field a la
     * the Description field.  This is the default for unknown field types.
     */
    FREE_TEXT_VALUE;


    public @NotNull List<List<ASTNode>> splitValue(@NotNull Deb822ValueParts valueParts) {
        List<List<ASTNode>> result;
        List<ASTNode> valueElements = Arrays.asList(valueParts.getNode().getChildren(TokenSet.ANY));

        if (this == FREE_TEXT_VALUE) {
            return Collections.singletonList(valueElements.stream().filter(IS_COMMENT).collect(Collectors.toList()));
        }

        valueElements = trimList(valueElements, IS_WHITESPACE);
        if (valueElements.isEmpty()) {
            return Collections.emptyList();
        }

        switch (this) {
            case SINGLE_KEYWORD:
            case SINGLE_TRIVIAL_VALUE:
                result = Collections.singletonList(valueElements);
                break;
            case COMMA_SEPARATED_VALUE_LIST_TRAILING_COMMA_OK:
                valueElements = pruneTrailingComma(valueElements);
                // FALL-THROUGH
            case COMMA_SEPARATED_VALUE_LIST:
                result = partitionList(valueElements, IS_COMMA);
                break;
            case SPACE_SEPARATED_VALUE_LIST:
                result = partitionList(valueElements, IS_WHITESPACE);
                break;
            default:
                throw new AssertionError("Uncovered enum: " + this);
        }
        return result;
    }

    private static @NotNull List<ASTNode> pruneTrailingComma(@NotNull List<ASTNode> valueElements) {
        // We only chomp the trailing comma if it is not the only thing there is
        if (valueElements.size() > 1) {
            // Remove the last comma and any whitespace behind it
            valueElements = trimList(chompList(valueElements, IS_COMMA), IS_WHITESPACE);
        }
        return valueElements;
    }

    private static @NotNull List<List<ASTNode>> partitionList(@NotNull List<ASTNode> values,
                                                              @NotNull Predicate<ASTNode> separator) {
        List<List<ASTNode>> result = new ArrayList<>();
        List<ASTNode> currentValue = null;
        for (ASTNode e : values) {
            if (IS_COMMENT.test(e)) {
                continue;
            }
            if (currentValue == null) {
                if (Deb822KnownFieldValueTypeUtil.IS_WHITESPACE.test(e)) {
                    continue;
                }
                currentValue = new ArrayList<>();
            }
            if (separator.test(e)) {
                result.add(trimList(currentValue, Deb822KnownFieldValueTypeUtil.IS_WHITESPACE));
                currentValue = null;
            } else {

                currentValue.add(e);
            }
        }
        if (currentValue != null) {
            result.add(currentValue);
        }
        return result;
    }

    private static <T> @NotNull List<T> chompList(@NotNull List<T> list, @NotNull Predicate<T> chompIf) {
        final int limit;
        if (list.isEmpty()) {
            return list;
        }
        limit = list.size() - 1;
        if (chompIf.test(list.get(limit))) {
            return list.subList(0, limit);
        }
        return list;
    }

    private static <T> @NotNull List<T> trimList(@NotNull List<T> list, @NotNull Predicate<T> pruneable) {
        int i = 0;
        int limit = list.size();
        for (; i < limit ; i++) {
            if (!pruneable.test(list.get(i))) {
                break;
            }
        }
        if (i >= limit) {
            return Collections.emptyList();
        }
        /* We know that at least one element will be kept now */
        --limit;
        while (pruneable.test(list.get(limit))) {
            --limit;
        }
        return list.subList(i, limit + 1);
    }

}
