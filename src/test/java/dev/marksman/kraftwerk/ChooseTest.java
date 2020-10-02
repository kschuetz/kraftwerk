package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.constraints.IntRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static dev.marksman.kraftwerk.Generators.chooseAtLeastOneOf;
import static dev.marksman.kraftwerk.Generators.chooseAtLeastOneOfValues;
import static dev.marksman.kraftwerk.Generators.chooseAtLeastOneValueFromCollection;
import static dev.marksman.kraftwerk.Generators.chooseEntryFromMap;
import static dev.marksman.kraftwerk.Generators.chooseKeyFromMap;
import static dev.marksman.kraftwerk.Generators.chooseOneFromCollection;
import static dev.marksman.kraftwerk.Generators.chooseOneFromCollectionWeighted;
import static dev.marksman.kraftwerk.Generators.chooseOneOf;
import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.chooseOneOfWeighted;
import static dev.marksman.kraftwerk.Generators.chooseOneOfWeightedValues;
import static dev.marksman.kraftwerk.Generators.chooseOneValueFromCollection;
import static dev.marksman.kraftwerk.Generators.chooseOneValueFromDomain;
import static dev.marksman.kraftwerk.Generators.chooseSomeOf;
import static dev.marksman.kraftwerk.Generators.chooseSomeOfValues;
import static dev.marksman.kraftwerk.Generators.chooseSomeValuesFromCollection;
import static dev.marksman.kraftwerk.Generators.chooseValueFromMap;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Weighted.weighted;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.Assert.assertForAll;
import static testsupport.CoversRange.coversRange;

class ChooseTest {
    @Nested
    @DisplayName("chooseOneOfValues")
    class ChooseOneOfValues {
        @Test
        void alwaysInRange() {
            assertForAll(chooseOneOfValues(1, 2, 3), n -> n == 1 || n == 2 || n == 3);
        }

        @Test
        void weightedAlwaysInRange() {
            assertForAll(chooseOneOfWeightedValues(weighted(1, 1),
                    weighted(1, 2),
                    weighted(0, 3)),
                    n -> n == 1 || n == 2);
        }

        @Test
        void coverage() {
            int[] f = new int[5];
            chooseOneOfValues(0, 1, 2, 3, 4).run().take(1000).forEach(n -> f[n] += 1);
            assertTrue(coversRange(f));
        }

        @Test
        void singleCandidate() {
            assertForAll(chooseOneOfValues(1), n -> n == 1);
        }
    }

    @Nested
    @DisplayName("chooseOneOf")
    class ChooseOneOf {
        @Test
        void alwaysInRange() {
            assertForAll(chooseOneOf(generateInt(IntRange.from(0).to(10)),
                    generateInt(IntRange.from(20).to(30))),
                    n -> (n >= 0 && n <= 10) || (n >= 20 && n <= 30));
        }

        @Test
        void weightedAlwaysInRange() {
            assertForAll(chooseOneOfWeighted(generateInt(IntRange.from(0).to(10)).weighted(1),
                    generateInt(IntRange.from(20).to(30)).weighted(0)),
                    n -> (n >= 0 && n <= 10));
        }
    }

    @Nested
    @DisplayName("chooseAtLeastOneOfValues")
    class ChooseAtLeastOneOfValues {
        @Test
        void alwaysInRange() {
            assertForAll(chooseAtLeastOneOfValues(1, 2, 3), xs ->
                    !xs.isEmpty() && all(n -> (n >= 1) && (n <= 3), xs));
        }

        @Test
        void noRepeats() {
            assertForAll(chooseAtLeastOneOfValues(1, 2, 3), xs ->
                    xs.toCollection(HashSet::new).size() == xs.size());
        }

        @Test
        void coverage() {
            int[] f = new int[4];
            chooseAtLeastOneOfValues(0, 1, 2).run().take(100)
                    .forEach(xs -> f[xs.foldLeft(Integer::sum, 0)] += 1);
            assertTrue(coversRange(f));
        }

        @Test
        void singleCandidate() {
            assertForAll(chooseAtLeastOneOfValues(1), xs -> xs.equals(Vector.of(1)));
        }
    }

    @Nested
    @DisplayName("chooseAtLeastOneOf")
    class ChooseAtLeastOneOf {
        @Test
        void alwaysInRange() {
            assertForAll(chooseAtLeastOneOf(generateInt(IntRange.from(0).to(10)),
                    generateInt(IntRange.from(20).to(30))),
                    xs -> (xs.size() == 1 || xs.size() == 2)
                            && all(n -> (n >= 0 && n <= 10) || (n >= 20 && n <= 30), xs));
        }
    }

    @Nested
    @DisplayName("chooseSomeOfValues")
    class ChooseSomeOfValues {
        @Test
        void alwaysInRange() {
            assertForAll(chooseSomeOfValues(1, 2, 3), xs ->
                    all(n -> (n >= 1) && (n <= 3), xs));
        }

        @Test
        void noRepeats() {
            assertForAll(chooseSomeOfValues(1, 2, 3), xs ->
                    xs.toCollection(HashSet::new).size() == xs.size());
        }

        @Test
        void coverage() {
            int[] f = new int[4];
            chooseSomeOfValues(0, 1, 2).run().take(100)
                    .forEach(xs -> f[xs.foldLeft(Integer::sum, 0)] += 1);
            assertTrue(coversRange(f));
        }

        @Test
        void singleCandidate() {
            assertForAll(chooseAtLeastOneOfValues(1), xs -> xs.isEmpty() || xs.equals(Vector.of(1)));
        }
    }

    @Nested
    @DisplayName("chooseSomeOf")
    class ChooseSomeOf {
        @Test
        void alwaysInRange() {
            assertForAll(chooseSomeOf(generateInt(IntRange.from(0).to(10)),
                    generateInt(IntRange.from(20).to(30))),
                    xs -> (xs.size() <= 2)
                            && all(n -> (n >= 0 && n <= 10) || (n >= 20 && n <= 30), xs));
        }
    }

    @Nested
    @DisplayName("chooseOneValueFromCollection")
    class ChooseOneValueFromCollection {
        @Test
        void throwsForEmptyCollection() {
            assertThrows(IllegalArgumentException.class, () -> chooseOneValueFromCollection(emptyList()));
        }

        @Test
        void alwaysInRange() {
            assertForAll(chooseOneValueFromCollection(asList(1, 2, 3)), n -> n == 1 || n == 2 || n == 3);
        }

        @Test
        void coverage() {
            int[] f = new int[5];
            chooseOneValueFromCollection(asList(0, 1, 2, 3, 4)).run().take(1000).forEach(n -> f[n] += 1);
            assertTrue(coversRange(f));
        }

        @Test
        void singleCandidate() {
            assertForAll(chooseOneValueFromCollection(singletonList(1)), n -> n == 1);
        }
    }

    @Nested
    @DisplayName("chooseOneFromCollection")
    class ChooseOneFromCollection {
        @Test
        void throwsForEmptyCollection() {
            assertThrows(IllegalArgumentException.class, () -> chooseOneFromCollection(emptyList()));
        }

        @Test
        void alwaysInRange() {
            assertForAll(chooseOneFromCollection(asList(generateInt(IntRange.from(0).to(10)),
                    generateInt(IntRange.from(20).to(30)))),
                    n -> (n >= 0 && n <= 10) || (n >= 20 && n <= 30));
        }

        @Test
        void weightedAlwaysInRange() {
            assertForAll(chooseOneFromCollectionWeighted(asList(generateInt(IntRange.from(0).to(10)).weighted(1),
                    generateInt(IntRange.from(20).to(30)).weighted(0))),
                    n -> (n >= 0 && n <= 10));
        }
    }

    @Nested
    @DisplayName("chooseOneValueFromDomain")
    class ChooseOneValueFromDomain {
        @Test
        void alwaysInRange() {
            assertForAll(chooseOneValueFromDomain(Vector.of(1, 2, 3)), n -> n == 1 || n == 2 || n == 3);
        }

        @Test
        void coverage() {
            int[] f = new int[5];
            chooseOneValueFromDomain(Vector.of(0, 1, 2, 3, 4)).run().take(1000).forEach(n -> f[n] += 1);
            assertTrue(coversRange(f));
        }

        @Test
        void singleCandidate() {
            assertForAll(chooseOneValueFromDomain(Vector.of(1)), n -> n == 1);
        }
    }

    @Nested
    @DisplayName("chooseAtLeastOneValueFromCollection")
    class ChooseAtLeastOneValueFromCollection {
        @Test
        void throwsForEmptyCollection() {
            assertThrows(IllegalArgumentException.class, () -> chooseAtLeastOneValueFromCollection(emptyList()));
        }

        @Test
        void alwaysInRange() {
            assertForAll(chooseAtLeastOneValueFromCollection(asList(1, 2, 3)), xs ->
                    !xs.isEmpty() && all(n -> (n >= 1) && (n <= 3), xs));
        }

        @Test
        void noRepeats() {
            assertForAll(chooseAtLeastOneValueFromCollection(asList(1, 2, 3)), xs ->
                    xs.toCollection(HashSet::new).size() == xs.size());
        }

        @Test
        void coverage() {
            int[] f = new int[4];
            chooseAtLeastOneValueFromCollection(asList(0, 1, 2)).run().take(100)
                    .forEach(xs -> f[xs.foldLeft(Integer::sum, 0)] += 1);
            assertTrue(coversRange(f));
        }

        @Test
        void singleCandidate() {
            assertForAll(chooseAtLeastOneValueFromCollection(singletonList(1)), xs -> xs.equals(Vector.of(1)));
        }
    }

    @Nested
    @DisplayName("chooseSomeValuesFromCollection")
    class ChooseSomeValuesFromCollection {
        @Test
        void alwaysInRange() {
            assertForAll(chooseSomeValuesFromCollection(asList(1, 2, 3)), xs ->
                    all(n -> (n >= 1) && (n <= 3), xs));
        }

        @Test
        void noRepeats() {
            assertForAll(chooseSomeValuesFromCollection(asList(1, 2, 3)), xs ->
                    xs.toCollection(HashSet::new).size() == xs.size());
        }

        @Test
        void coverage() {
            int[] f = new int[4];
            chooseSomeValuesFromCollection(asList(0, 1, 2)).run().take(100)
                    .forEach(xs -> f[xs.foldLeft(Integer::sum, 0)] += 1);
            assertTrue(coversRange(f));
        }

        @Test
        void emptyCollection() {
            assertForAll(chooseSomeValuesFromCollection(emptyList()), Vector::isEmpty);
        }

        @Test
        void singletonCollection() {
            assertForAll(chooseSomeValuesFromCollection(singletonList(1)), xs -> xs.isEmpty() || xs.equals(Vector.of(1)));
        }
    }

    @Nested
    @DisplayName("chooseEntryFromMap")
    class ChooseEntryFromMap {
        @Test
        void throwsForEmptyMap() {
            assertThrows(IllegalArgumentException.class, () -> chooseEntryFromMap(new HashMap<>()));
        }

        @Test
        void alwaysInRange() {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("foo", 1);
            map.put("bar", 2);
            map.put("baz", 3);
            Set<Map.Entry<String, Integer>> entries = map.entrySet();

            assertForAll(chooseEntryFromMap(map), entries::contains);
        }
    }

    @Nested
    @DisplayName("chooseKeyFromMap")
    class ChooseKeyFromMap {
        @Test
        void throwsForEmptyMap() {
            assertThrows(IllegalArgumentException.class, () -> chooseKeyFromMap(new HashMap<>()));
        }

        @Test
        void alwaysInRange() {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("foo", 1);
            map.put("bar", 2);
            map.put("baz", 3);
            Set<String> keys = map.keySet();

            assertForAll(chooseKeyFromMap(map), keys::contains);
        }
    }

    @Nested
    @DisplayName("chooseValueFromMap")
    class ChooseValueFromMap {
        @Test
        void throwsForEmptyMap() {
            assertThrows(IllegalArgumentException.class, () -> chooseValueFromMap(new HashMap<>()));
        }

        @Test
        void alwaysInRange() {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("foo", 1);
            map.put("bar", 2);
            map.put("baz", 3);

            assertForAll(chooseValueFromMap(map), map::containsValue);
        }
    }
}
