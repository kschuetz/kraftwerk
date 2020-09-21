package dev.marksman.kraftwerk;

import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static dev.marksman.kraftwerk.Constant.constant;
import static dev.marksman.kraftwerk.StringGeneratorBuilder.builder;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringGeneratorBuilderTest {
    @Nested
    @DisplayName("add")
    class Add {
        @Test
        void testCase1() {
            assertExpectedString("foo", builder()
                    .add("foo")
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("foobar", builder()
                    .add("foo")
                    .add("bar")
                    .build());
        }

        @Test
        void testCase3() {
            assertExpectedString("foobar", builder()
                    .add(constant("foo"))
                    .add(constant("bar"))
                    .build());
        }
    }

    @Nested
    @DisplayName("addMaybe")
    class AddMaybe {
        @Test
        void testCase1() {
            assertExpectedString("", builder()
                    .addMaybe(nothing())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("foobar", builder()
                    .addMaybe(just("foo"))
                    .addMaybe(nothing())
                    .addMaybe(just("bar"))
                    .build());
        }

        @Test
        void testCase3() {
            assertExpectedString("foobar", builder()
                    .addMaybe(constant("foo").just())
                    .addMaybe(constant(nothing()))
                    .addMaybe(constant("bar").just())
                    .build());
        }
    }

    @Nested
    @DisplayName("addMany")
    class AddMany {
        @Test
        void testCase1() {
            assertExpectedString("", builder()
                    .addMany(emptyList())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("foobarbaz", builder()
                    .addMany(asList(constant("foo"), constant("bar"), constant("baz")))
                    .build());
        }
    }

    @Nested
    @DisplayName("addManyValues")
    class AddManyValues {
        @Test
        void testCase1() {
            assertExpectedString("", builder()
                    .addManyValues(emptyList())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("foobarbaz", builder()
                    .addManyValues(asList("foo", "bar", "baz"))
                    .build());
        }
    }

    @Nested
    @DisplayName("addManyMaybe")
    class AddManyMaybe {
        @Test
        void testCase1() {
            assertExpectedString("", builder()
                    .addManyMaybe(emptyList())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("", builder()
                    .addManyMaybe(asList(constant(nothing()), constant(nothing()), constant(nothing())))
                    .build());
        }


        @Test
        void testCase3() {
            assertExpectedString("foobarbaz", builder()
                    .addManyMaybe(asList(constant("foo").just(), constant(nothing()), constant("bar").just(),
                            constant(nothing()), constant("baz").just()))
                    .build());
        }
    }

    @Nested
    @DisplayName("addManyMaybeValues")
    class AddManyMaybeValues {
        @Test
        void testCase1() {
            assertExpectedString("", builder()
                    .addManyMaybeValues(emptyList())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("", builder()
                    .addManyMaybeValues(asList(nothing(), nothing(), nothing()))
                    .build());
        }


        @Test
        void testCase3() {
            assertExpectedString("foobarbaz", builder()
                    .addManyMaybeValues(asList(just("foo"), nothing(), just("bar"),
                            nothing(), just("baz")))
                    .build());
        }
    }

    @Nested
    @DisplayName("withSeparator")
    class WithSeparator {
        @Nested
        @DisplayName("constant")
        class ConstantSeparator {
            @Test
            void testCase1() {
                assertExpectedString("", builder()
                        .withSeparator(",")
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("foo", builder()
                        .withSeparator(",")
                        .add("foo")
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("foobarbaz", builder()
                        .withSeparator("")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foo,bar,baz", builder()
                        .withSeparator(",")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("foo,bar", builder()
                        .withSeparator(",")
                        .addMaybe(just("foo"))
                        .addMaybe(nothing())
                        .addMaybe(just("bar"))
                        .build());
            }
        }

        @Nested
        @DisplayName("generated")
        class GeneratedSeparator {
            @Test
            void testCase1() {
                assertExpectedString("", builder()
                        .withSeparator(constant(","))
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("foo", builder()
                        .withSeparator(constant(","))
                        .add("foo")
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("foobarbaz", builder()
                        .withSeparator("")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foo,bar,baz", builder()
                        .withSeparator(constant(","))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("foo,bar", builder()
                        .withSeparator(constant(","))
                        .addMaybe(just("foo"))
                        .addMaybe(nothing())
                        .addMaybe(just("bar"))
                        .build());
            }
        }
    }

    @Nested
    @DisplayName("withStartDelimiter")
    class WithStartDelimiter {
        @Nested
        @DisplayName("constant")
        class ConstantStartDelimiter {
            @Test
            void testCase1() {
                assertExpectedString("", builder()
                        .withStartDelimiter("")
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("[", builder()
                        .withStartDelimiter("[")
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("[foo", builder()
                        .withStartDelimiter("[")
                        .add("foo")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foobarbaz", builder()
                        .withStartDelimiter("")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("[foobarbaz", builder()
                        .withStartDelimiter("[")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase6() {
                assertExpectedString("[foobar", builder()
                        .withStartDelimiter("[")
                        .addMaybe(just("foo"))
                        .addMaybe(nothing())
                        .addMaybe(just("bar"))
                        .build());
            }
        }

        @Nested
        @DisplayName("generated")
        class GeneratedStartDelimiter {
            @Test
            void testCase1() {
                assertExpectedString("", builder()
                        .withStartDelimiter(constant(""))
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("[", builder()
                        .withStartDelimiter(constant("["))
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("[foo", builder()
                        .withStartDelimiter(constant("["))
                        .add("foo")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foobarbaz", builder()
                        .withStartDelimiter(constant(""))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("[foobarbaz", builder()
                        .withStartDelimiter(constant("["))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase6() {
                assertExpectedString("[foobar", builder()
                        .withStartDelimiter(constant("["))
                        .addMaybe(just("foo"))
                        .addMaybe(nothing())
                        .addMaybe(just("bar"))
                        .build());
            }
        }
    }

    @Nested
    @DisplayName("withEndDelimiter")
    class WithEndDelimiter {
        @Nested
        @DisplayName("constant")
        class ConstantEndDelimiter {
            @Test
            void testCase1() {
                assertExpectedString("", builder()
                        .withEndDelimiter("")
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("]", builder()
                        .withEndDelimiter("]")
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("foo]", builder()
                        .withEndDelimiter("]")
                        .add("foo")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foobarbaz", builder()
                        .withEndDelimiter("")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("foobarbaz]", builder()
                        .withEndDelimiter("]")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase6() {
                assertExpectedString("foobar]", builder()
                        .withEndDelimiter("]")
                        .addMaybe(just("foo"))
                        .addMaybe(nothing())
                        .addMaybe(just("bar"))
                        .build());
            }
        }

        @Nested
        @DisplayName("generated")
        class GeneratedEndDelimiter {
            @Test
            void testCase1() {
                assertExpectedString("", builder()
                        .withEndDelimiter(constant(""))
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("]", builder()
                        .withEndDelimiter(constant("]"))
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("foo]", builder()
                        .withEndDelimiter(constant("]"))
                        .add("foo")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foobarbaz", builder()
                        .withEndDelimiter(constant(""))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("foobarbaz]", builder()
                        .withEndDelimiter(constant("]"))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase6() {
                assertExpectedString("foobar]", builder()
                        .withEndDelimiter(constant("]"))
                        .addMaybe(just("foo"))
                        .addMaybe(nothing())
                        .addMaybe(just("bar"))
                        .build());
            }
        }
    }

    @Nested
    @DisplayName("complexExamples")
    class ComplexExamples {
        @Test
        void testCase1() {
            assertExpectedString("[The,quick,brown,fox,jumped,over,the,lazy,sleeping,dogs]",
                    builder()
                            .withStartDelimiter("[")
                            .withSeparator(",")
                            .withEndDelimiter("]")
                            .add("")
                            .add("The")
                            .addManyValues(asList("quick", "brown", "fox"))
                            .add(constant("jumped"))
                            .addManyMaybe(asList(constant("over").just(), constant(nothing()), constant("the").just()))
                            .addMaybe(constant("lazy").just())
                            .addMaybe(nothing())
                            .addMany(asList(constant("sleeping"), constant("dogs")))
                            .add("")
                            .build());
        }
    }

    private void assertExpectedString(String expected, Generator<String> gen) {
        ImmutableFiniteIterable<String> results = gen.run().take(10);
        assertTrue(all(s -> s.equals(expected), results));
    }
}
