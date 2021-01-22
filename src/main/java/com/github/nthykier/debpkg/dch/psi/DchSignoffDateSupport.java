package com.github.nthykier.debpkg.dch.psi;

import com.intellij.psi.PsiElement;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public interface DchSignoffDateSupport extends PsiElement {
    DateTimeFormatter[] VALID_DATE_FORMATS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("EE, dd MMM yyyy HH:mm:ss xx"),
            // Technically invalid, but used in tons of entries prior to 2005 and understood
            // by any tool that wants to work with Debian packages.
            DateTimeFormatter.ofPattern("EE, [ ]d MMM yyyy HH:mm:ss xx"),
    };

    DateTimeFormatter PREFERRED_DATE_FORMAT = VALID_DATE_FORMATS[0];

    ZonedDateTime getSignoffDate();
}
