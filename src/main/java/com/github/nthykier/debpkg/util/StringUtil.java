package com.github.nthykier.debpkg.util;

import com.intellij.util.SmartList;
import lombok.Value;
import org.apache.commons.text.similarity.EditDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {

    private static final EditDistance<Integer> EDIT_DISTANCE_2 = new LevenshteinDistance(2);
    private static final EditDistance<Integer> EDIT_DISTANCE_3 = new LevenshteinDistance(3);
    private static final Comparator<ValidAlternative> ORDER_BY_EDIT_DISTANCE_THEN_VALUE = Comparator.comparingInt(ValidAlternative::getEditDistance).thenComparing(ValidAlternative::getValue);

    public static List<String> possibleMisspellingOf(String providedValue, Collection<String> correctValues, boolean shortWords) {
        List<ValidAlternative> result = null;
        EditDistance<Integer> editDistance = shortWords ? EDIT_DISTANCE_2 : EDIT_DISTANCE_3;

        for (String correctValue : correctValues) {
            int distance = editDistance.apply(providedValue, correctValue);
            if (distance < 0) {
                continue;
            }
            assert distance != 0;
            if (result == null) {
                result = new SmartList<>(ValidAlternative.of(correctValue, distance));
            } else {
                result.add(ValidAlternative.of(correctValue, distance));
            }
        }

        if (result == null) {
            return shortWords ? possibleMisspellingOf(providedValue, correctValues, false) : Collections.emptyList();
        }
        if (result.size() > 1) {
            return result.stream()
                    .sorted(ORDER_BY_EDIT_DISTANCE_THEN_VALUE)
                    .map(ValidAlternative::getValue)
                    .collect(Collectors.toList());
        }
        assert !result.isEmpty();
        return Collections.singletonList(result.get(0).getValue());
    }

    @Value(staticConstructor = "of")
    private static class ValidAlternative {
        String value;
        int editDistance;
    }
}
