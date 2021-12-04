package com.github.nthykier.debpkg;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class Deb822Bundle extends DynamicBundle {

    @NonNls
    public static final String BUNDLE_NAME = "messages.DebpkgBundle";
    private static final Deb822Bundle INSTANCE = new Deb822Bundle();

    private Deb822Bundle() {
        super(BUNDLE_NAME);
    }

    @Contract(pure = true)
    public static String message(@PropertyKey(resourceBundle=BUNDLE_NAME) @NotNull String key,
                                 @NotNull Object ... params) {
        return INSTANCE.getMessage(key, params);
    }
}
