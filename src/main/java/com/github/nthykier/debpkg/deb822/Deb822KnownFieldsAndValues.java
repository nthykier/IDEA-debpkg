package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.deplang.DependencyLanguage;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlLanguage;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianCopyrightLanguage;
import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianTestsControlLanguage;
import com.github.nthykier.debpkg.deb822.field.*;
import com.github.nthykier.debpkg.deb822.field.impl.Deb822KnownFieldImpl;
import com.github.nthykier.debpkg.deb822.field.impl.Deb822KnownFieldKeywordImpl;
import com.github.nthykier.debpkg.deb822.field.impl.Deb822KnownRelationFieldImpl;
import com.github.nthykier.debpkg.deb822.field.impl.KnownFieldTableImpl;
import com.github.nthykier.debpkg.deb822.psi.Deb822ParagraphSupport;
import com.intellij.lang.Language;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.nthykier.debpkg.deb822.Deb822YamlDataFileParserUtil.*;
import static com.github.nthykier.debpkg.deb822.field.KnownFields.ANY_PARAGRAPH_TYPES;

public class Deb822KnownFieldsAndValues {

    private static final Map<String, Deb822KnownField> DCTRL_KNOWN_FIELDS = new HashMap<>();
    private static final Map<String, Deb822KnownField> DCOPY_KNOWN_FIELDS = new HashMap<>();
    private static final Map<String, Deb822KnownField> DTCTRL_KNOWN_FIELDS = new HashMap<>();

    private static final KeywordInformation EMPTY_KEYWORD_INFORMATION = KeywordInformation.of(Collections.emptyMap(), false);

    private static final Set<String> KNOWN_VERSION_OPERATORS = new LinkedHashSet<>(Arrays.asList(
            ">>", ">=", "=", "<=", "<<"
    ));

    private static final BiFunction<KnownFieldTable, Deb822ParagraphSupport, Deb822KnownField> CLASSIFICATION_FIELD_NAMING_RULE = (knownFieldTable, paragraph) -> {
        return knownFieldTable.getField(paragraph.classifyParagraph());
    };

    private Deb822KnownFieldsAndValues() {}

    @NotNull
    public static KnownFieldTable getKnownFieldsFor(Language language) {
        if (language.is(Deb822DialectDebianControlLanguage.INSTANCE)) {
            return new KnownFieldTableImpl(DCTRL_KNOWN_FIELDS, true, CLASSIFICATION_FIELD_NAMING_RULE);
        } else if (language.is(Deb822DialectDebianCopyrightLanguage.INSTANCE)) {
            return new KnownFieldTableImpl(DCOPY_KNOWN_FIELDS);
        } else if (language.is(Deb822DialectDebianTestsControlLanguage.INSTANCE)) {
            return new KnownFieldTableImpl(DTCTRL_KNOWN_FIELDS);
        } else if (language.isKindOf(Deb822Language.INSTANCE)) {
            return KnownFieldTable.NULL_TABLE;
        }
        throw new IllegalArgumentException("Language must be a variant of Deb822Language");
    }

    private static void checkedAddField(Map<String, Deb822KnownField> knownFields, String fieldNameLC, Deb822KnownField field) {
        Deb822KnownField existing = knownFields.putIfAbsent(fieldNameLC, field);
        assert existing == null : "Field " + existing.getCanonicalFieldName() + " is declared twice";
    }

    private static void loadKnownFieldDefinitions() {
        loadKnownFieldDefinitionsFromResource("DebianControl.data.yaml", DCTRL_KNOWN_FIELDS);
        loadKnownFieldDefinitionsFromResource("DebianCopyright.data.yaml", DCOPY_KNOWN_FIELDS);
        loadKnownFieldDefinitionsFromResource("DebianTestsControl.data.yaml", DTCTRL_KNOWN_FIELDS);
    }

    private static void loadKnownFieldDefinitionsFromResource(String resourceName,
                                                              Map<String, Deb822KnownField> knownFieldMap) {
        InputStream s = Deb822KnownFieldsAndValues.class.getResourceAsStream(resourceName);
        Yaml y = new Yaml();
        Map<String, Object> data = y.load(s);
        List<Map<String, Object>> fieldDefinitions = getList(data, "fields");
        for (Map<String, Object> fieldDefinition : fieldDefinitions) {
            Deb822KnownField field = parseKnownFieldDefinition(fieldDefinition);
            String key = KnownFieldTable.withXPrefixStripped(field.getCanonicalFieldName());
            if (key != null) {
                key = "X-" + key;
            } else {
                key = field.getCanonicalFieldName();
            }
            checkedAddField(knownFieldMap, key.toLowerCase().intern(), field);
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

    private static Set<String> convertToSet(List<String> valuesAsList) {
        if (valuesAsList.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new LinkedHashSet<>(valuesAsList));
    }

    private static KeywordInformation getKeywordInformation(Map<String, Object> fieldDef,
                                                            String canonicalName) {
        List<?> keywordList = getList(fieldDef, "keywordList");
        String keywordReference = getOptionalString(fieldDef, "keywordListFromDataSet", null);
        Map<String, Deb822KnownFieldKeyword> keywordMap;
        boolean allKeywordsKnown = false;
        if (keywordReference != null) {
            Collection<String> dataSet = Deb822DataSets.getDataSet(keywordReference);
            if (!keywordList.isEmpty()) {
                throw new IllegalArgumentException("Field " + canonicalName
                        + " has both keywordList and keywordListFromDataSet.  Please use at most one of them");
            }
            keywordMap = dataSet.stream()
                    .map(w -> new Deb822KnownFieldKeywordImpl(w, null, false))
                    .collect(Collectors.toMap(Deb822KnownFieldKeyword::getValueName, Function.identity()));
        } else if (!keywordList.isEmpty()) {
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
            return EMPTY_KEYWORD_INFORMATION;
        }
        return KeywordInformation.of(keywordMap, allKeywordsKnown);
    }

    private static Deb822KnownField parseKnownFieldDefinition(Map<String, Object> fieldDef) {
        String canonicalName = getRequiredString(fieldDef, "canonicalName");
        Deb822KnownFieldValueType valueType = Deb822KnownFieldValueType.valueOf(
                getOptionalString(fieldDef, "valueType", "FREE_TEXT_VALUE")
        );
        Deb822KnownFieldValueLanguage fieldValueLanguage = Deb822KnownFieldValueLanguage.valueOf(
                getOptionalString(fieldDef, "valueLanguage", "REGULAR_FIELD_VALUE")
        );

        String docs = getOptionalString(fieldDef, "description", null);
        String defaultValue = getOptionalString(fieldDef, "defaultValue", null);
        KeywordInformation keywordInformation = getKeywordInformation(fieldDef, canonicalName);
        boolean supportsSubstvars = getBool(fieldDef, "supportsSubstvars", false);
        boolean warnIfDefault = getBool(fieldDef, "warnIfDefault", false);
        boolean isFoldedByDefault = getBool(fieldDef, "foldedByDefault", false);
        boolean spellcheckField = getBool(fieldDef, "spellcheckValue", false);
        Set<String> supportedParagraphTypes = parseStringOrListAsList(fieldDef,
                "onlyInParagraphType",
                Deb822KnownFieldsAndValues::parseSupportedParagraphTypes,
                ANY_PARAGRAPH_TYPES);
        Set<String> supportedVersionOperators = parseStringOrListAsList(fieldDef,
                "supportedVersionOperators",
                Deb822KnownFieldsAndValues::convertToSet,
                null);
        boolean supportsBuildProfileRestriction = getBool(fieldDef, "supportsBuildProfileRestriction", false);
        switch (valueType) {
            case SINGLE_TRIVIAL_VALUE:
                if (!keywordInformation.keywordMap.isEmpty()) {
                    throw new IllegalArgumentException("Field " + canonicalName + " has keywords but is a SINGLE_VALUE"
                    + " (should it have been a SINGLE_KEYWORD instead?)");
                }
                break;
            case SINGLE_KEYWORD:
                if (keywordInformation.keywordMap.isEmpty()) {
                    throw new IllegalArgumentException("Field " + canonicalName + " has no keywords but is a SINGLE_KEYWORD"
                            + " (should it have been a SINGLE_VALUE instead?)");
                }
                break;
            case BUILD_PROFILES_FIELD:
            case COMMA_SEPARATED_VALUE_LIST_TRAILING_COMMA_OK:
            case COMMA_SEPARATED_VALUE_LIST:
            case SPACE_SEPARATED_VALUE_LIST:
                /* OK with or without keywords */
                break;
            case FREE_TEXT_VALUE:
                if (!keywordInformation.keywordMap.isEmpty()) {
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

        if (fieldValueLanguage.getLanguage() == DependencyLanguage.INSTANCE) {
            if (supportedVersionOperators == null) {
                supportedVersionOperators = KNOWN_VERSION_OPERATORS;
            }
            if (spellcheckField) {
                throw new IllegalArgumentException("Field " + canonicalName + " has spellcheckValue enabled but is"
                        + " a dependency field (i.e. no free form text).");
            }

            return new Deb822KnownRelationFieldImpl(canonicalName, valueType, fieldValueLanguage,
                    keywordInformation.allKeywordsKnown, keywordInformation.keywordMap, docs,
                    supportsSubstvars, defaultValue, warnIfDefault, supportedParagraphTypes, isFoldedByDefault,
                    supportedVersionOperators, supportsBuildProfileRestriction
            );
        } else {
            if (supportedVersionOperators != null) {
                throw new IllegalArgumentException("Field " + canonicalName + " has supportedVersionOperators but is"
                        + " not a language field (valueLanguage)");
            }
            if (supportsBuildProfileRestriction) {
                throw new IllegalArgumentException("Field " + canonicalName + " has supportsBuildProfileRestriction but is"
                        + " not a language field (valueLanguage)");
            }

            return new Deb822KnownFieldImpl(canonicalName, valueType, fieldValueLanguage,
                    keywordInformation.allKeywordsKnown, keywordInformation.keywordMap, docs,
                    supportsSubstvars, defaultValue, warnIfDefault, supportedParagraphTypes, isFoldedByDefault,
                    spellcheckField);
        }
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

    @Data(staticConstructor = "of")
    private static class KeywordInformation {
        final Map<String, Deb822KnownFieldKeyword> keywordMap;
        final boolean allKeywordsKnown;
    }

    static {
        loadKnownFieldDefinitions();
    }
}

