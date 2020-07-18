package com.github.nthykier.debpkg.deb822;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class Deb822KnownFieldsAndValues {

    private static final Map<String, Deb822KnownField> KNOWN_FIELDS = new HashMap<>();
    private static List<String> KNOWN_FIELD_NAMES = Collections.emptyList();

    private Deb822KnownFieldsAndValues() {}

    @Nullable
    public static Deb822KnownField lookupDeb822Field(@NotNull String fieldName) {
        return lookupDeb822Field(fieldName, null);
    }

    @Contract("_, !null -> !null")
    public static Deb822KnownField lookupDeb822Field(@NotNull String fieldName, @Nullable Deb822KnownField fallback) {
        return KNOWN_FIELDS.getOrDefault(fieldName.toLowerCase(), fallback);
    }

    @NotNull
    public static List<String> getAllKnownFieldNames() {
        if (KNOWN_FIELD_NAMES.size() != KNOWN_FIELDS.size()) {
            // When 1.10 can be assumed; use ".collect(Collectors.toUnmodifiableList())"
            KNOWN_FIELD_NAMES = Collections.unmodifiableList(KNOWN_FIELDS.values()
                    .stream()
                    .map(Deb822KnownField::getCanonicalFieldName)
                    .sorted(String::compareToIgnoreCase)
                    .collect(Collectors.toList())
            );
        }
        return KNOWN_FIELD_NAMES;
    }

    static void KEYWORDS_FOR_FIELD(String fieldName, boolean exclusive, String... keywords) {
        String fieldLc = fieldName.toLowerCase().intern();
        NavigableSet<String> allKnownKeywords = new TreeSet<>(Arrays.asList(keywords));
        Deb822KnownField field = new Deb822KnownFieldImpl(fieldName, exclusive, allKnownKeywords);
        checkedAddField(fieldLc, field);
    }

    static void ADD_KNOWN_FIELDS(String ... fieldNames) {
        for (String fieldName : fieldNames) {
            String fieldLc = fieldName.toLowerCase().intern();
            Deb822KnownField field = new Deb822KnownFieldImpl(fieldName, false, Collections.emptyNavigableSet());
            checkedAddField(fieldLc, field);
        }
    }

    private static void checkedAddField(String fieldNameLC, Deb822KnownField field) {
        Deb822KnownField existing = KNOWN_FIELDS.putIfAbsent(fieldNameLC, field);
        assert existing == null : "Field " + existing.getCanonicalFieldName() + " is declared twice";
    }

    public static class Deb822KnownFieldImpl implements Deb822KnownField {
        private final String canonicalFieldName;
        private final boolean areAllKeywordsKnown;
        private final boolean hasKnownValues;
        private final NavigableSet<String> allKnownKeywords;

        public Deb822KnownFieldImpl(@NotNull String canonicalFieldName, boolean areAllKeywordsKnown, @NotNull NavigableSet<String> allKnownKeywords) {
            this.canonicalFieldName = canonicalFieldName;
            this.areAllKeywordsKnown = areAllKeywordsKnown;
            this.allKnownKeywords = Collections.unmodifiableNavigableSet(allKnownKeywords) ;
            this.hasKnownValues = areAllKeywordsKnown || !this.allKnownKeywords.isEmpty();
        }

        @NotNull
        @Override
        public String getCanonicalFieldName() {
            return canonicalFieldName;
        }

        @Override
        public boolean areAllKeywordsKnown() {
            return areAllKeywordsKnown;
        }


        @Override
        public boolean hasKnownValues() {
            return hasKnownValues;
        }

        @NotNull
        @Override
        public NavigableSet<String> getKnownKeywords() {
            return allKnownKeywords;
        }
    }

    static {
        /* Exclusive */
        KEYWORDS_FOR_FIELD("Multi-Arch", true,"no", "foreign", "same", "allowed");
        KEYWORDS_FOR_FIELD("Priority", true,"extra", "optional", "important", "required");
        KEYWORDS_FOR_FIELD("X-DH-Build-For-Type", true, "host", "target");

        /* Non-exclusive */
        KEYWORDS_FOR_FIELD("Architecture", false,"all", "any");
        KEYWORDS_FOR_FIELD("Rules-Requires-Root", false,"no", "binary-targets");
        KEYWORDS_FOR_FIELD("Section", false,
                "admin", "cli-mono", "comm", "database", "debian-installer", "debug", "devel", "doc",
                "editors", "education", "eletronics", "embedded", "fonts", "games", "gnome", "gnu-r", "gnustep",
                "graphics", "hamradio", "haskell", "interpreters", "introspection", "java", "javascript", "kde",
                "kernel", "libdevel", "libs", "lisp", "localization", "mail", "math", "metapackages", "misc",
                "net", "news", "ocaml", "oldlibs", "otherosfs", "perl", "php", "python", "ruby", "rust", "science",
                "shells", "sound", "tasks", "tex", "text", "utils", "vcs", "video", "virtual", "web", "x11",
                "xfce", "zope");

        /* Fields with structured content we know but currently cannot validate at the moment */
        ADD_KNOWN_FIELDS("Source", "Package", "Maintainer", "Uploaders", "Testsuite", "Standards-Version",
                "Vcs-Git", "Vcs-Svn", "Vcs-Browser");
        ADD_KNOWN_FIELDS(
                "Build-Depends", "Build-Depends-Indep", "Build-Depends-Arch",
                "Build-Conflicts", "Build-Conflicts-Indep", "Build-Conflicts-Arch",
                "Pre-Depends", "Depends", "Recommends", "Suggests", "Enhances",
                "Conflicts", "Replaces", "Breaks"
                );

        /* Fields we know but cannot say much about their content */
        ADD_KNOWN_FIELDS("Description");
    }
}

