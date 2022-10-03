package software.kes.kraftwerk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.enhancediterables.ImmutableFiniteIterable;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringGeneratorBuilderTest {
    @Nested
    @DisplayName("add")
    class Add {
        @Test
        void testCase1() {
            assertExpectedString("foo", StringGeneratorBuilder.builder()
                    .add("foo")
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("foobar", StringGeneratorBuilder.builder()
                    .add("foo")
                    .add("bar")
                    .build());
        }

        @Test
        void testCase3() {
            assertExpectedString("foobar", StringGeneratorBuilder.builder()
                    .add(Constant.constant("foo"))
                    .add(Constant.constant("bar"))
                    .build());
        }
    }

    @Nested
    @DisplayName("addMaybe")
    class AddMaybe {
        @Test
        void testCase1() {
            assertExpectedString("", StringGeneratorBuilder.builder()
                    .addMaybe(nothing())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("foobar", StringGeneratorBuilder.builder()
                    .addMaybe(just("foo"))
                    .addMaybe(nothing())
                    .addMaybe(just("bar"))
                    .build());
        }

        @Test
        void testCase3() {
            assertExpectedString("foobar", StringGeneratorBuilder.builder()
                    .addMaybe(Constant.constant("foo").just())
                    .addMaybe(Constant.constant(nothing()))
                    .addMaybe(Constant.constant("bar").just())
                    .build());
        }
    }

    @Nested
    @DisplayName("addMany")
    class AddMany {
        @Test
        void testCase1() {
            assertExpectedString("", StringGeneratorBuilder.builder()
                    .addMany(emptyList())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
                    .addMany(asList(Constant.constant("foo"), Constant.constant("bar"), Constant.constant("baz")))
                    .build());
        }
    }

    @Nested
    @DisplayName("addManyValues")
    class AddManyValues {
        @Test
        void testCase1() {
            assertExpectedString("", StringGeneratorBuilder.builder()
                    .addManyValues(emptyList())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
                    .addManyValues(asList("foo", "bar", "baz"))
                    .build());
        }
    }

    @Nested
    @DisplayName("addManyMaybe")
    class AddManyMaybe {
        @Test
        void testCase1() {
            assertExpectedString("", StringGeneratorBuilder.builder()
                    .addManyMaybe(emptyList())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("", StringGeneratorBuilder.builder()
                    .addManyMaybe(asList(Constant.constant(nothing()), Constant.constant(nothing()), Constant.constant(nothing())))
                    .build());
        }


        @Test
        void testCase3() {
            assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
                    .addManyMaybe(asList(Constant.constant("foo").just(), Constant.constant(nothing()), Constant.constant("bar").just(),
                            Constant.constant(nothing()), Constant.constant("baz").just()))
                    .build());
        }
    }

    @Nested
    @DisplayName("addManyMaybeValues")
    class AddManyMaybeValues {
        @Test
        void testCase1() {
            assertExpectedString("", StringGeneratorBuilder.builder()
                    .addManyMaybeValues(emptyList())
                    .build());
        }

        @Test
        void testCase2() {
            assertExpectedString("", StringGeneratorBuilder.builder()
                    .addManyMaybeValues(asList(nothing(), nothing(), nothing()))
                    .build());
        }


        @Test
        void testCase3() {
            assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
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
                assertExpectedString("", StringGeneratorBuilder.builder()
                        .withSeparator(",")
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("foo", StringGeneratorBuilder.builder()
                        .withSeparator(",")
                        .add("foo")
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
                        .withSeparator("")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foo,bar,baz", StringGeneratorBuilder.builder()
                        .withSeparator(",")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("foo,bar", StringGeneratorBuilder.builder()
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
                assertExpectedString("", StringGeneratorBuilder.builder()
                        .withSeparator(Constant.constant(","))
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("foo", StringGeneratorBuilder.builder()
                        .withSeparator(Constant.constant(","))
                        .add("foo")
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
                        .withSeparator("")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foo,bar,baz", StringGeneratorBuilder.builder()
                        .withSeparator(Constant.constant(","))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("foo,bar", StringGeneratorBuilder.builder()
                        .withSeparator(Constant.constant(","))
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
                assertExpectedString("", StringGeneratorBuilder.builder()
                        .withStartDelimiter("")
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("[", StringGeneratorBuilder.builder()
                        .withStartDelimiter("[")
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("[foo", StringGeneratorBuilder.builder()
                        .withStartDelimiter("[")
                        .add("foo")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
                        .withStartDelimiter("")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("[foobarbaz", StringGeneratorBuilder.builder()
                        .withStartDelimiter("[")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase6() {
                assertExpectedString("[foobar", StringGeneratorBuilder.builder()
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
                assertExpectedString("", StringGeneratorBuilder.builder()
                        .withStartDelimiter(Constant.constant(""))
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("[", StringGeneratorBuilder.builder()
                        .withStartDelimiter(Constant.constant("["))
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("[foo", StringGeneratorBuilder.builder()
                        .withStartDelimiter(Constant.constant("["))
                        .add("foo")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
                        .withStartDelimiter(Constant.constant(""))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("[foobarbaz", StringGeneratorBuilder.builder()
                        .withStartDelimiter(Constant.constant("["))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase6() {
                assertExpectedString("[foobar", StringGeneratorBuilder.builder()
                        .withStartDelimiter(Constant.constant("["))
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
                assertExpectedString("", StringGeneratorBuilder.builder()
                        .withEndDelimiter("")
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("]", StringGeneratorBuilder.builder()
                        .withEndDelimiter("]")
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("foo]", StringGeneratorBuilder.builder()
                        .withEndDelimiter("]")
                        .add("foo")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
                        .withEndDelimiter("")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("foobarbaz]", StringGeneratorBuilder.builder()
                        .withEndDelimiter("]")
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase6() {
                assertExpectedString("foobar]", StringGeneratorBuilder.builder()
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
                assertExpectedString("", StringGeneratorBuilder.builder()
                        .withEndDelimiter(Constant.constant(""))
                        .build());
            }

            @Test
            void testCase2() {
                assertExpectedString("]", StringGeneratorBuilder.builder()
                        .withEndDelimiter(Constant.constant("]"))
                        .build());
            }

            @Test
            void testCase3() {
                assertExpectedString("foo]", StringGeneratorBuilder.builder()
                        .withEndDelimiter(Constant.constant("]"))
                        .add("foo")
                        .build());
            }

            @Test
            void testCase4() {
                assertExpectedString("foobarbaz", StringGeneratorBuilder.builder()
                        .withEndDelimiter(Constant.constant(""))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase5() {
                assertExpectedString("foobarbaz]", StringGeneratorBuilder.builder()
                        .withEndDelimiter(Constant.constant("]"))
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build());
            }

            @Test
            void testCase6() {
                assertExpectedString("foobar]", StringGeneratorBuilder.builder()
                        .withEndDelimiter(Constant.constant("]"))
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
                    StringGeneratorBuilder.builder()
                            .withStartDelimiter("[")
                            .withSeparator(",")
                            .withEndDelimiter("]")
                            .add("")
                            .add("The")
                            .addManyValues(asList("quick", "brown", "fox"))
                            .add(Constant.constant("jumped"))
                            .addManyMaybe(asList(Constant.constant("over").just(), Constant.constant(nothing()), Constant.constant("the").just()))
                            .addMaybe(Constant.constant("lazy").just())
                            .addMaybe(nothing())
                            .addMany(asList(Constant.constant("sleeping"), Constant.constant("dogs")))
                            .add("")
                            .build());
        }
    }

    private void assertExpectedString(String expected, Generator<String> gen) {
        ImmutableFiniteIterable<String> results = gen.run().take(10);
        assertTrue(all(s -> s.equals(expected), results));
    }
}
