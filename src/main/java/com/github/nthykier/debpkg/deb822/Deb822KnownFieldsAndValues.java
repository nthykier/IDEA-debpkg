package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;
import com.github.nthykier.debpkg.deb822.field.impl.Deb822KnownFieldImpl;
import com.github.nthykier.debpkg.deb822.field.impl.Deb822KnownFieldKeywordImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.nthykier.debpkg.deb822.Deb822YamlDataFileParserUtil.*;

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

    static void ADD_KNOWN_FIELDS(Deb822KnownFieldValueType valueType, String ... fieldNames) {
        for (String fieldName : fieldNames) {
            String fieldLc = fieldName.toLowerCase().intern();
            Deb822KnownField field = new Deb822KnownFieldImpl(fieldName, valueType, false,
                    Collections.emptyMap(), null, true);
            checkedAddField(fieldLc, field);
        }
    }

    private static void checkedAddField(String fieldNameLC, Deb822KnownField field) {
        Deb822KnownField existing = KNOWN_FIELDS.putIfAbsent(fieldNameLC, field);
        assert existing == null : "Field " + existing.getCanonicalFieldName() + " is declared twice";
    }

    private static void loadKnownFieldDefinitions() {
        InputStream s = Deb822KnownFieldsAndValues.class.getResourceAsStream("DebianControl.data.yaml");
        Yaml y = new Yaml();
        Map<String, Object> data = y.load(s);
        List<Map<String, Object>> fieldDefinitions = getList(data, "fields");
        for (Map<String, Object> fieldDefinition : fieldDefinitions) {
            Deb822KnownField field = parseKnownFieldDefinition(fieldDefinition);
            checkedAddField(field.getCanonicalFieldName().toLowerCase().intern(), field);
        }
    }

    private static Deb822KnownField parseKnownFieldDefinition(Map<String, Object> fieldDef) {
        String canonicalName = getRequiredString(fieldDef, "canonicalName");
        Deb822KnownFieldValueType valueType = Deb822KnownFieldValueType.valueOf(
                getOptionalString(fieldDef, "valueType", "FREE_TEXT_VALUE")
        );
        List<Object> keywordList = getList(fieldDef, "keywordList");
        String docs = getOptionalString(fieldDef, "description", null);
        Map<String, Deb822KnownFieldKeyword> keywordMap;
        boolean allKeywordsKnown = false;
        boolean supportsSubstvars = getBool(fieldDef, "supportsSubstvars", true);
        switch (valueType) {
            case SINGLE_TRIVIAL_VALUE:
                if (!keywordList.isEmpty()) {
                    throw new IllegalArgumentException("Field " + canonicalName + " has keywords but is a SINGLE_VALUE"
                    + " (should it have been a SINGLE_KEYWORD instead?)");
                }
                break;
            case SINGLE_KEYWORD:
                if (keywordList.isEmpty()) {
                    throw new IllegalArgumentException("Field " + canonicalName + " has no keywords but is a SINGLE_KEYWORD"
                            + " (should it have been a SINGLE_VALUE instead?)");
                }
                break;
            case COMMA_SEPARATED_VALUE_LIST:
            case SPACE_SEPARATED_VALUE_LIST:
                /* OK with or without keywords */
                break;
            case FREE_TEXT_VALUE:
                if (!keywordList.isEmpty()) {
                    throw new IllegalArgumentException("Field " + canonicalName + " has keywords but is a FREE_TEXT_VALUE");
                }
                break;
            default:
                throw new AssertionError("Unsupported but declared valueType: " + valueType);
        }
        if (!keywordList.isEmpty()) {
            allKeywordsKnown = true;
            if (keywordList.get(keywordList.size() - 1).equals("...")) {
                allKeywordsKnown = false;
                keywordList.remove(keywordList.size() - 1);
            }
            keywordMap = new HashMap<>(keywordList.size());
            for (Object keywordDef : keywordList) {
                Deb822KnownFieldKeyword keyword = parseKeyword(keywordDef);
                Deb822KnownFieldKeyword existing = keywordMap.putIfAbsent(keyword.getValueName(), keyword);
                assert existing == null : "Duplicate keyword " + keyword.getValueName() + " for field " + canonicalName;
            }
        } else {
            keywordMap = Collections.emptyMap();
        }
        return new Deb822KnownFieldImpl(canonicalName, valueType, allKeywordsKnown, keywordMap, docs,
                supportsSubstvars);
    }

    private static Deb822KnownFieldKeyword parseKeyword(Object keywordDefRaw) {
        String docs = null;
        String valueName;
        boolean isExclusive = false;
        if (keywordDefRaw instanceof String) {
            valueName = (String)keywordDefRaw;
        } else if (keywordDefRaw instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> keywordDef = (Map<String, Object>)keywordDefRaw;
            valueName = getRequiredString(keywordDef, "value");
            docs = getOptionalString(keywordDef, "description", null);
            isExclusive = getBool(keywordDef, "isExclusive", false);
        } else {
            throw new IllegalArgumentException("Do not know how parse keyword: " + keywordDefRaw);
        }
        return new Deb822KnownFieldKeywordImpl(valueName, docs, isExclusive);
    }

    static {
        /* Fields with structured content we know but currently cannot validate at the moment */
        ADD_KNOWN_FIELDS(Deb822KnownFieldValueType.FREE_TEXT_VALUE, "Vcs-Git", "Vcs-Svn", "Vcs-Browser");
        ADD_KNOWN_FIELDS(Deb822KnownFieldValueType.COMMA_SEPARATED_VALUE_LIST_TRAILING_COMMA_OK,
                "Build-Depends", "Build-Depends-Indep", "Build-Depends-Arch",
                "Build-Conflicts", "Build-Conflicts-Indep", "Build-Conflicts-Arch",
                "Pre-Depends", "Depends", "Recommends", "Suggests", "Enhances",
                "Conflicts", "Replaces", "Breaks"
                );

        loadKnownFieldDefinitions();
    }
}

