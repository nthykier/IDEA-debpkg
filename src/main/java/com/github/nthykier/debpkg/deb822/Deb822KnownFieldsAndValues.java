package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.field.*;
import com.github.nthykier.debpkg.deb822.field.impl.Deb822KnownFieldImpl;
import com.github.nthykier.debpkg.deb822.field.impl.Deb822KnownFieldKeywordImpl;
import com.github.nthykier.debpkg.deb822.field.impl.KnownFieldTableImpl;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

import static com.github.nthykier.debpkg.deb822.Deb822YamlDataFileParserUtil.*;
import static com.github.nthykier.debpkg.deb822.field.KnownFields.ANY_PARAGRAPH_TYPES;

public class Deb822KnownFieldsAndValues {

    private static final Map<Language, KnownFieldTable> LANGUAGE2KNOWN_FIELDS = new HashMap<>();
    private static final Map<String, Deb822KnownField> KNOWN_FIELDS = new HashMap<>();

    private Deb822KnownFieldsAndValues() {}

    @NotNull
    public static KnownFieldTable getKnownFieldsFor(Language language) {
        synchronized (LANGUAGE2KNOWN_FIELDS) {
            KnownFieldTable knownFieldTable = LANGUAGE2KNOWN_FIELDS.get(language);
            if (knownFieldTable != null) {
                return knownFieldTable;
            }
            if (language == Deb822DialectDebianControlLanguage.INSTANCE) {
                knownFieldTable = new KnownFieldTableImpl(language, KNOWN_FIELDS);
            } else if (language.isKindOf(Deb822Language.INSTANCE)) {
                knownFieldTable = new KnownFieldTableImpl(language, Collections.emptyMap());
            }
            if (knownFieldTable != null) {
                LANGUAGE2KNOWN_FIELDS.put(language, knownFieldTable);
                return knownFieldTable;
            }
        }
        throw new IllegalArgumentException("Language must be a variant of Deb822Language");
    }

    static void ADD_KNOWN_FIELDS(Deb822KnownFieldValueType valueType, String ... fieldNames) {
        for (String fieldName : fieldNames) {
            String fieldLc = fieldName.toLowerCase().intern();
            Deb822KnownField field = new Deb822KnownFieldImpl(fieldName, valueType, false,
                    Collections.emptyMap(), null, true, null, false,
                    ANY_PARAGRAPH_TYPES);
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

    private static Set<String> parseSupportedParagraphTypes(List<String> valuesAsList) {
        Set<String> values;
        if (valuesAsList.isEmpty()) {
            return ANY_PARAGRAPH_TYPES;
        }
        values = new HashSet<>(valuesAsList);
        if (values.contains(KnownFields.ANY_PARAGRAPH)) {
            if (values.size() == 1) {
                return ANY_PARAGRAPH_TYPES;
            }
            throw new IllegalArgumentException("The value ANY must appear on its own in onlyInParagraphType");
        }
        return Collections.unmodifiableSet(values);
    }

    private static Deb822KnownField parseKnownFieldDefinition(Map<String, Object> fieldDef) {
        String canonicalName = getRequiredString(fieldDef, "canonicalName");
        Deb822KnownFieldValueType valueType = Deb822KnownFieldValueType.valueOf(
                getOptionalString(fieldDef, "valueType", "FREE_TEXT_VALUE")
        );
        List<Object> keywordList = getList(fieldDef, "keywordList");
        String docs = getOptionalString(fieldDef, "description", null);
        String defaultValue = getOptionalString(fieldDef, "defaultValue", null);
        Map<String, Deb822KnownFieldKeyword> keywordMap;
        boolean allKeywordsKnown = false;
        boolean supportsSubstvars = getBool(fieldDef, "supportsSubstvars", true);
        boolean warnIfDefault = getBool(fieldDef, "warnIfDefault", false);
        Set<String> supportedParagraphTypes = parseStringOrListAsList(fieldDef,
                "onlyInParagraphType",
                Deb822KnownFieldsAndValues::parseSupportedParagraphTypes,
                ANY_PARAGRAPH_TYPES);
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
                if (defaultValue != null || warnIfDefault) {
                    throw new IllegalArgumentException("Field " + canonicalName + " is an FREE_TEXT_VALUE but has a defined"
                            + " defaultValue or warnIfDefault.");
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
                supportsSubstvars, defaultValue, warnIfDefault, supportedParagraphTypes);
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
        ADD_KNOWN_FIELDS(Deb822KnownFieldValueType.FREE_TEXT_VALUE, "Vcs-Git", "Vcs-Svn", "Vcs-Browser",
                "Vcs-Arch", "Vcs-Bzr", "Vcs-Cvs", "Vcs-Darcs", "Vcs-Hg", "Vcs-Mtn");
        ADD_KNOWN_FIELDS(Deb822KnownFieldValueType.COMMA_SEPARATED_VALUE_LIST_TRAILING_COMMA_OK,
                "Build-Depends", "Build-Depends-Indep", "Build-Depends-Arch",
                "Build-Conflicts", "Build-Conflicts-Indep", "Build-Conflicts-Arch",
                "Pre-Depends", "Depends", "Recommends", "Suggests", "Enhances",
                "Conflicts", "Replaces", "Breaks", "Provides"
                );

        loadKnownFieldDefinitions();
    }
}

