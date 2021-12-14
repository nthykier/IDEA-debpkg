package com.github.nthykier.debpkg.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final Pattern PACKAGE_NAME_REGEX = Pattern.compile("[a-z0-9][a-z0-9.+-]+");
    public static final Pattern VERSION_NUMBER_REGEX = Pattern.compile("(?:[0-9]+:)?[0-9][a-z0-9.+~]*([-][a-z0-9.+~-]+)?");

}
