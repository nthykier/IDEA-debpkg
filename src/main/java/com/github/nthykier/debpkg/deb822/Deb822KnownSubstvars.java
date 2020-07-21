package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.nthykier.debpkg.deb822.Deb822YamlDataFileParserUtil.*;

public class Deb822KnownSubstvars {

    private static final Map<String, Deb822KnownSubstvar> KNOWN_SUBSTVARS = new HashMap<>();
    private static List<String> KNOWN_SUBSTVARS_NAMES = Collections.emptyList();

    private Deb822KnownSubstvars() {}

    @Nullable
    public static Deb822KnownSubstvar lookupSubstvar(@NotNull String substvar) {
        return lookupSubstvar(substvar, null);
    }

    @Contract("_, !null -> !null")
    public static Deb822KnownSubstvar lookupSubstvar(@NotNull String substvar, @Nullable Deb822KnownSubstvar fallback) {
        return KNOWN_SUBSTVARS.getOrDefault(substvar, fallback);
    }

    @NotNull
    public static List<String> getAllKnownSubstvarNames() {
        if (KNOWN_SUBSTVARS_NAMES.size() != KNOWN_SUBSTVARS.size()) {
            // When 1.10 can be assumed; use ".collect(Collectors.toUnmodifiableList())"
            KNOWN_SUBSTVARS_NAMES = Collections.unmodifiableList(KNOWN_SUBSTVARS.values()
                    .stream()
                    .map(Deb822KnownSubstvar::getName)
                    .sorted(String::compareToIgnoreCase)
                    .collect(Collectors.toList())
            );
        }
        return KNOWN_SUBSTVARS_NAMES;
    }

    private static void checkedAddSubstvar(Deb822KnownSubstvar substvar) {
        Deb822KnownSubstvar existing = KNOWN_SUBSTVARS.putIfAbsent(substvar.getName(), substvar);
        assert existing == null : "Substvar " + existing.getName() + " is declared twice";
    }

    public static class Deb822KnownSubstvarImpl implements Deb822KnownSubstvar {
        private final String name;
        private final String predefinedValue;
        private final String docs;

        public Deb822KnownSubstvarImpl(@NotNull String name,
                                    String predefinedValue,
                                    String docs) {
            this.name = name;
            this.predefinedValue = predefinedValue;
            this.docs = docs;
        }

        @NotNull
        @Override
        public String getName() {
            return name;
        }

        @Nullable
        @Override
        public String getPredefinedValue() {
            return this.predefinedValue;
        }

        @Nullable
        @Override
        public String getDescription() {
            return this.docs;
        }

    }

    private static void loadKnownSubstvarDefinitions() {
        InputStream s = Deb822KnownSubstvars.class.getResourceAsStream("DebianControl.data.yaml");
        Yaml y = new Yaml();
        Map<String, Object> data = y.load(s);
        List<Map<String, Object>> fieldDefinitions = getList(data, "substvars");
        KnownFieldTable knownFieldTable = Deb822KnownFieldsAndValues.getKnownFieldsFor(Deb822DialectDebianControlLanguage.INSTANCE);
        for (Map<String, Object> fieldDefinition : fieldDefinitions) {
            for (Deb822KnownSubstvar field : parseKnownSubstvarDefinition(fieldDefinition, knownFieldTable)) {
                checkedAddSubstvar(field);
            }
        }
    }

    private static Iterable<Deb822KnownSubstvar> parseKnownSubstvarDefinition(@NotNull Map<String, Object> fieldDef,
                                                                              @NotNull KnownFieldTable knownFieldTable) {
        String name = getOptionalString(fieldDef, "name", null);
        String docs = getOptionalString(fieldDef, "description", null);
        String predefinedValue = getOptionalString(fieldDef, "value", null);
        if (name == null) {
            String patternName = getOptionalString(fieldDef, "pattern", null);
            List<String> fieldNames = getList(fieldDef, "fieldNames");
            List<Deb822KnownSubstvar> definedSubstvars = new ArrayList<>(fieldNames.size());
            if (patternName == null) {
                throw new IllegalArgumentException("Missing required \"name\" or \"patternName\" field");
            }
            if (fieldNames.isEmpty()) {
                throw new IllegalArgumentException("fieldNames must be a non-empty list when pattern is defined (pattern: " + patternName + ")");
            }
            if (predefinedValue != null) {
                throw new IllegalArgumentException("Pattern " + patternName + " has a predefined value!"
                        + " (Each substvar should have a unique value, so the value make no sense)");
            }
            for (String fieldName : fieldNames) {
                Deb822KnownField field = knownFieldTable.getField(fieldName);
                String generatedName;
                if (field == null || !field.getCanonicalFieldName().equals(fieldName)) {
                    if (field == null) {
                        throw new IllegalArgumentException("Unknown field " + fieldName + "; ensure it is defined in the fields section");
                    }
                    throw new IllegalArgumentException("Incorrect case for " + field.getCanonicalFieldName()
                            + ".  Is \"" + field + "\" but should be \"" + field.getCanonicalFieldName() + "\"");
                }
                generatedName = patternName.replace("<FIELD_NAME>", fieldName);
                if (generatedName.equals(patternName)) {
                    throw new IllegalArgumentException("Invalid pattern " + patternName + " - it must contain <FIELD_NAME> to be substituted");
                }
                definedSubstvars.add(new Deb822KnownSubstvarImpl(generatedName, null, docs));
            }
            return definedSubstvars;
        }
        return Collections.singletonList(new Deb822KnownSubstvarImpl(name, predefinedValue, docs));
    }

    static {
        loadKnownSubstvarDefinitions();
    }
}

