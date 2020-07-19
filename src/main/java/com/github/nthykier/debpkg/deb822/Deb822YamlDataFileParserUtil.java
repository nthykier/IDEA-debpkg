package com.github.nthykier.debpkg.deb822;

import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Deb822YamlDataFileParserUtil {

    static <T> List<T> getList(Map<String, Object> map, String fieldName) {
        @SuppressWarnings("unchecked")
        List<T> res = getTypedObject(map, fieldName, List.class, Collections.EMPTY_LIST);
        return res;
    }

    static String getRequiredString(Map<String, Object> map, String fieldName) {
        String val = getOptionalString(map, fieldName, null);
        if (val == null) {
            throw new IllegalArgumentException("Missing required String parameter " + fieldName);
        }
        return val;
    }

    static String getOptionalString(Map<String, Object> map, String fieldName, String defaultValue) {
        return getTypedObject(map, fieldName, String.class, defaultValue);
    }

    static boolean getBool(Map<String, Object> map, String fieldName, boolean defaultValue) {
        return getTypedObject(map, fieldName, Boolean.class, defaultValue ? Boolean.TRUE : Boolean.FALSE);
    }

    @Contract("_, _, _, !null -> !null")
    static <T> T getTypedObject(Map<String, Object> map, String fieldName, Class<T> clazz, T defaultValue) {
        Object value = map.get(fieldName);
        if (value == null) {
            return defaultValue;
        }
        if (clazz.isAssignableFrom(value.getClass())) {
            return clazz.cast(value);
        }
        throw new IllegalArgumentException(fieldName + " was defined and a " + value.getClass().getCanonicalName()
                + " (expected a " + clazz.getCanonicalName() + ")");
    }
}
