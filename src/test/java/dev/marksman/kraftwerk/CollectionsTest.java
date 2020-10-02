package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.constraints.IntRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static dev.marksman.kraftwerk.Generators.chooseOneValueFromCollection;
import static dev.marksman.kraftwerk.Generators.generateArrayList;
import static dev.marksman.kraftwerk.Generators.generateArrayListOfSize;
import static dev.marksman.kraftwerk.Generators.generateHashSet;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateMap;
import static dev.marksman.kraftwerk.Generators.generateNonEmptyArrayList;
import static dev.marksman.kraftwerk.Generators.generateNonEmptyHashSet;
import static dev.marksman.kraftwerk.Generators.generateNonEmptyMap;
import static dev.marksman.kraftwerk.Generators.generateNonEmptyVector;
import static dev.marksman.kraftwerk.Generators.generateNonEmptyVectorOfSize;
import static dev.marksman.kraftwerk.Generators.generateUnit;
import static dev.marksman.kraftwerk.Generators.generateVector;
import static dev.marksman.kraftwerk.Generators.generateVectorOfSize;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.Assert.assertForAll;

class CollectionsTest {
    private static final int TEST_FOR_EMPTY_SAMPLE_COUNT = 500;

    @Nested
    @DisplayName("generateArrayList")
    class GenerateArrayList {
        @Test
        void allElementsInRange() {
            assertForAll(generateArrayList(generateInt(IntRange.from(1).to(3))),
                    list -> all(n -> n == 1 || n == 2 || n == 3, list));
        }

        @Test
        void sometimeEmptyAndSometimesNonEmpty() {
            int[] f = new int[2];
            generateArrayList(generateUnit()).run().take(TEST_FOR_EMPTY_SAMPLE_COUNT).forEach(xs -> f[xs.size() > 0 ? 1 : 0] += 1);
            assertTrue(f[0] > 0 && f[1] > 0);
        }
    }

    @Nested
    @DisplayName("generateNonEmptyArrayList")
    class GenerateNonEmptyArrayList {
        @Test
        void allElementsInRange() {
            assertForAll(generateNonEmptyArrayList(generateInt(IntRange.from(1).to(3))),
                    list -> all(n -> n == 1 || n == 2 || n == 3, list));
        }

        @Test
        void alwaysNonEmpty() {
            assertForAll(generateNonEmptyArrayList(generateUnit()), xs -> xs.size() > 0);
        }
    }

    @Nested
    @DisplayName("generateArrayListOfSize")
    class GenerateArrayListOfSize {
        @Nested
        @DisplayName("constant size")
        class ConstantSize {
            @Test
            void allElementsInRange() {
                assertForAll(generateArrayListOfSize(10, generateInt(IntRange.from(1).to(3))),
                        list -> all(n -> n == 1 || n == 2 || n == 3, list));
            }

            @Test
            void correctSize() {
                assertForAll(generateArrayListOfSize(0, generateUnit()), xs -> xs.size() == 0);
                assertForAll(generateArrayListOfSize(10, generateUnit()), xs -> xs.size() == 10);
            }
        }

        @Nested
        @DisplayName("size in range")
        class SizeInRange {
            @Test
            void allElementsInRange() {
                assertForAll(generateArrayListOfSize(IntRange.from(0).to(3),
                        generateInt(IntRange.from(1).to(3))),
                        list -> all(n -> n == 1 || n == 2 || n == 3, list));
            }

            @Test
            void correctSize() {
                assertForAll(generateArrayListOfSize(IntRange.from(0).to(10),
                        generateUnit()),
                        xs -> xs.size() <= 10);
                assertForAll(generateArrayListOfSize(IntRange.from(1).to(10),
                        generateUnit()),
                        xs -> xs.size() >= 1 && xs.size() <= 10);
            }
        }
    }

    @Nested
    @DisplayName("generateHashSet")
    class GenerateHashSet {
        @Test
        void allElementsInRange() {
            assertForAll(generateHashSet(generateInt(IntRange.from(1).to(3))),
                    list -> all(n -> n == 1 || n == 2 || n == 3, list));
        }

        @Test
        void sometimeEmptyAndSometimesNonEmpty() {
            int[] f = new int[2];
            generateHashSet(generateUnit()).run().take(TEST_FOR_EMPTY_SAMPLE_COUNT).forEach(xs -> f[xs.size() > 0 ? 1 : 0] += 1);
            assertTrue(f[0] > 0 && f[1] > 0);
        }
    }

    @Nested
    @DisplayName("generateNonEmptyHashSet")
    class GenerateNonEmptyHashSet {
        @Test
        void allElementsInRange() {
            assertForAll(generateNonEmptyHashSet(generateInt(IntRange.from(1).to(3))),
                    list -> all(n -> n == 1 || n == 2 || n == 3, list));
        }

        @Test
        void alwaysNonEmpty() {
            assertForAll(generateNonEmptyHashSet(generateUnit()), xs -> xs.size() > 0);
        }
    }

    @Nested
    @DisplayName("generateVector")
    class GenerateVector {
        @Test
        void allElementsInRange() {
            assertForAll(generateVector(generateInt(IntRange.from(1).to(3))),
                    vector -> all(n -> n == 1 || n == 2 || n == 3, vector));
        }

        @Test
        void sometimeEmptyAndSometimesNonEmpty() {
            int[] f = new int[2];
            generateVector(generateUnit()).run().take(TEST_FOR_EMPTY_SAMPLE_COUNT).forEach(xs -> f[xs.size() > 0 ? 1 : 0] += 1);
            assertTrue(f[0] > 0 && f[1] > 0);
        }
    }

    @Nested
    @DisplayName("generateVectorOfSize")
    class GenerateVectorOfSize {
        @Nested
        @DisplayName("constant size")
        class ConstantSize {
            @Test
            void allElementsInRange() {
                assertForAll(generateVectorOfSize(10, generateInt(IntRange.from(1).to(3))),
                        list -> all(n -> n == 1 || n == 2 || n == 3, list));
            }

            @Test
            void correctSize() {
                assertForAll(generateVectorOfSize(0, generateUnit()), xs -> xs.size() == 0);
                assertForAll(generateVectorOfSize(10, generateUnit()), xs -> xs.size() == 10);
            }
        }

        @Nested
        @DisplayName("size in range")
        class SizeInRange {
            @Test
            void allElementsInRange() {
                assertForAll(generateVectorOfSize(IntRange.from(0).to(3),
                        generateInt(IntRange.from(1).to(3))),
                        vector -> all(n -> n == 1 || n == 2 || n == 3, vector));
            }

            @Test
            void correctSize() {
                assertForAll(generateVectorOfSize(IntRange.from(0).to(10),
                        generateUnit()),
                        xs -> xs.size() <= 10);
                assertForAll(generateVectorOfSize(IntRange.from(1).to(10),
                        generateUnit()),
                        xs -> xs.size() >= 1 && xs.size() <= 10);
            }
        }
    }

    @Nested
    @DisplayName("generateNonEmptyVector")
    class GenerateNonEmptyVector {
        @Test
        void allElementsInRange() {
            assertForAll(generateNonEmptyVector(generateInt(IntRange.from(1).to(3))),
                    list -> all(n -> n == 1 || n == 2 || n == 3, list));
        }
    }

    @Nested
    @DisplayName("generateNonEmptyVectorOfSize")
    class GenerateNonEmptyVectorOfSize {
        @Nested
        @DisplayName("constant size")
        class ConstantSize {
            @Test
            void throwsIfSizeIsZero() {
                assertThrows(IllegalArgumentException.class, () -> generateNonEmptyVectorOfSize(0, generateUnit()));
            }

            @Test
            void allElementsInRange() {
                assertForAll(generateNonEmptyVectorOfSize(10, generateInt(IntRange.from(1).to(3))),
                        list -> all(n -> n == 1 || n == 2 || n == 3, list));
            }

            @Test
            void correctSize() {
                assertForAll(generateNonEmptyVectorOfSize(10, generateUnit()), xs -> xs.size() == 10);
            }
        }

        @Nested
        @DisplayName("size in range")
        class SizeInRange {
            @Test
            void throwsIfSizeRangeContainsZero() {
                assertThrows(IllegalArgumentException.class, () -> generateNonEmptyVectorOfSize(IntRange.from(0).to(3), generateUnit()));
            }

            @Test
            void allElementsInRange() {
                assertForAll(generateNonEmptyVectorOfSize(IntRange.from(1).to(3),
                        generateInt(IntRange.from(1).to(3))),
                        vector -> all(n -> n == 1 || n == 2 || n == 3, vector));
            }

            @Test
            void correctSize() {
                assertForAll(generateNonEmptyVectorOfSize(IntRange.from(1).to(10),
                        generateUnit()),
                        xs -> xs.size() >= 1 && xs.size() <= 10);
            }
        }
    }

    @Nested
    @DisplayName("generateMap")
    class GenerateMap {
        @Nested
        @DisplayName("with generated keys")
        class WithGeneratedKeys {
            @Test
            void allElementsInRange() {
                Set<String> keys = new HashSet<>();
                keys.add("foo");
                keys.add("bar");
                keys.add("baz");

                IntRange valueRange = IntRange.from(1).to(10);

                assertForAll(generateMap(chooseOneValueFromCollection(keys),
                        generateInt(valueRange)),
                        map -> all(entry -> keys.contains(entry.getKey()) && valueRange.includes(entry.getValue()),
                                map.entrySet()));
            }

            @Test
            void sometimeEmptyAndSometimesNonEmpty() {
                int[] f = new int[2];
                generateMap(generateUnit(), generateUnit()).run().take(TEST_FOR_EMPTY_SAMPLE_COUNT).forEach(xs -> f[xs.size() > 0 ? 1 : 0] += 1);
                assertTrue(f[0] > 0 && f[1] > 0);
            }
        }

        @Nested
        @DisplayName("with supplied keys")
        class WithSuppliedKeys {
            @Nested
            @DisplayName("in a vector")
            class InAVector {
                @Test
                void allElementsInRange() {
                    ImmutableNonEmptyVector<String> keys = Vector.of("foo", "bar", "baz");
                    HashSet<String> keySet = keys.toCollection(HashSet::new);
                    IntRange valueRange = IntRange.from(1).to(10);

                    assertForAll(generateMap(keys, generateInt(valueRange)),
                            map -> all(entry -> keySet.contains(entry.getKey()) && valueRange.includes(entry.getValue()),
                                    map.entrySet()));
                }

                @Test
                void allKeysRepresented() {
                    ImmutableNonEmptyVector<String> keys = Vector.of("foo", "bar", "baz");
                    assertForAll(generateMap(keys, generateUnit()),
                            map -> all(map::containsKey, keys));
                }
            }

            @Nested
            @DisplayName("in a collection")
            class InACollection {
                @Test
                void allElementsInRange() {
                    List<String> keys = asList("foo", "bar", "baz");
                    IntRange valueRange = IntRange.from(1).to(10);

                    assertForAll(generateMap(keys, generateInt(valueRange)),
                            map -> all(entry -> keys.contains(entry.getKey()) && valueRange.includes(entry.getValue()),
                                    map.entrySet()));
                }

                @Test
                void allKeysRepresented() {
                    List<String> keys = asList("foo", "bar", "baz");
                    assertForAll(generateMap(keys, generateUnit()),
                            map -> all(map::containsKey, keys));
                }
            }
        }
    }

    @Nested
    @DisplayName("generateNonEmptyMap")
    class GenerateNonEmptyMap {
        @Test
        void allElementsInRange() {
            Set<String> keys = new HashSet<>();
            keys.add("foo");
            keys.add("bar");
            keys.add("baz");

            IntRange valueRange = IntRange.from(1).to(10);

            assertForAll(generateNonEmptyMap(chooseOneValueFromCollection(keys),
                    generateInt(valueRange)),
                    map -> all(entry -> keys.contains(entry.getKey()) && valueRange.includes(entry.getValue()),
                            map.entrySet()));
        }

        @Test
        void alwaysNonEmpty() {
            assertForAll(generateNonEmptyMap(generateUnit(), generateUnit()), map -> map.size() > 0);
        }
    }
}