package software.kes.kraftwerk.aggregator;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.Vector;
import software.kes.collectionviews.VectorBuilder;

import java.util.ArrayList;
import java.util.Collections;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static software.kes.kraftwerk.aggregator.Aggregators.charAggregator;
import static software.kes.kraftwerk.aggregator.Aggregators.collectionAggregator;
import static software.kes.kraftwerk.aggregator.Aggregators.maybeStringAggregator;
import static software.kes.kraftwerk.aggregator.Aggregators.monoidAggregator;
import static software.kes.kraftwerk.aggregator.Aggregators.stringAggregator;
import static software.kes.kraftwerk.aggregator.Aggregators.vectorAggregator;

class AggregatorsTest {
    @Nested
    @DisplayName("vectorAggregator")
    class VectorAggregator {
        @Test
        void empty() {
            Aggregator<Integer, VectorBuilder<Integer>, ImmutableVector<Integer>> aggregator = vectorAggregator();
            VectorBuilder<Integer> builder = aggregator.builder();
            assertEquals(Vector.empty(), aggregator.build(builder));
        }

        @Test
        void oneElements() {
            Aggregator<Integer, VectorBuilder<Integer>, ImmutableVector<Integer>> aggregator = vectorAggregator();
            VectorBuilder<Integer> builder = aggregator.builder();
            builder = aggregator.add(builder, 1);
            assertEquals(Vector.of(1), aggregator.build(builder));
        }

        @Test
        void fiveElements() {
            Aggregator<Integer, VectorBuilder<Integer>, ImmutableVector<Integer>> aggregator = vectorAggregator();
            VectorBuilder<Integer> builder = aggregator.builder();
            builder = aggregator.add(builder, 1);
            builder = aggregator.add(builder, 2);
            builder = aggregator.add(builder, 3);
            builder = aggregator.add(builder, 4);
            builder = aggregator.add(builder, 5);
            assertEquals(Vector.of(1, 2, 3, 4, 5), aggregator.build(builder));
        }

        @Test
        void withInitialCapacity() {
            Aggregator<Integer, VectorBuilder<Integer>, ImmutableVector<Integer>> aggregator = vectorAggregator(2);
            VectorBuilder<Integer> builder = aggregator.builder();
            builder = aggregator.add(builder, 1);
            builder = aggregator.add(builder, 2);
            builder = aggregator.add(builder, 3);
            assertEquals(Vector.of(1, 2, 3), aggregator.build(builder));
        }
    }

    @Nested
    @DisplayName("stringAggregator")
    class StringAggregator {
        @Test
        void empty() {
            Aggregator<String, StringBuilder, String> aggregator = stringAggregator();
            StringBuilder builder = aggregator.builder();
            assertEquals("", aggregator.build(builder));
        }

        @Test
        void oneElement() {
            Aggregator<String, StringBuilder, String> aggregator = stringAggregator();
            StringBuilder builder = aggregator.builder();
            aggregator.add(builder, "foo");
            assertEquals("foo", aggregator.build(builder));
        }

        @Test
        void fiveElements() {
            Aggregator<String, StringBuilder, String> aggregator = stringAggregator();
            StringBuilder builder = aggregator.builder();
            aggregator.add(builder, "foo");
            aggregator.add(builder, "bar");
            aggregator.add(builder, "baz");
            aggregator.add(builder, "qux");
            aggregator.add(builder, "quux");
            assertEquals("foobarbazquxquux", aggregator.build(builder));
        }

        @Test
        void withInitialCapacity() {
            Aggregator<String, StringBuilder, String> aggregator = stringAggregator(5);
            StringBuilder builder = aggregator.builder();
            builder = aggregator.add(builder, "foo");
            builder = aggregator.add(builder, "bar");
            assertEquals("foobar", aggregator.build(builder));
        }
    }

    @Nested
    @DisplayName("charAggregator")
    class CharAggregator {
        @Test
        void empty() {
            Aggregator<Character, StringBuilder, String> aggregator = charAggregator();
            StringBuilder builder = aggregator.builder();
            assertEquals("", aggregator.build(builder));
        }

        @Test
        void oneElement() {
            Aggregator<Character, StringBuilder, String> aggregator = charAggregator();
            StringBuilder builder = aggregator.builder();
            aggregator.add(builder, 'a');
            assertEquals("a", aggregator.build(builder));
        }

        @Test
        void fiveElements() {
            Aggregator<Character, StringBuilder, String> aggregator = charAggregator();
            StringBuilder builder = aggregator.builder();
            aggregator.add(builder, 'a');
            aggregator.add(builder, 'b');
            aggregator.add(builder, 'c');
            aggregator.add(builder, 'd');
            aggregator.add(builder, 'e');
            assertEquals("abcde", aggregator.build(builder));
        }

        @Test
        void withInitialCapacity() {
            Aggregator<Character, StringBuilder, String> aggregator = charAggregator(2);
            StringBuilder builder = aggregator.builder();
            builder = aggregator.add(builder, 'a');
            builder = aggregator.add(builder, 'b');
            builder = aggregator.add(builder, 'c');
            assertEquals("abc", aggregator.build(builder));
        }
    }

    @Nested
    @DisplayName("maybeStringAggregator")
    class MaybeStringAggregator {
        @Test
        void empty() {
            Aggregator<Maybe<String>, StringBuilder, String> aggregator = maybeStringAggregator();
            StringBuilder builder = aggregator.builder();
            aggregator.add(builder, nothing());
            assertEquals("", aggregator.build(builder));
        }

        @Test
        void oneElement() {
            Aggregator<Maybe<String>, StringBuilder, String> aggregator = maybeStringAggregator();
            StringBuilder builder = aggregator.builder();
            aggregator.add(builder, just("foo"));
            assertEquals("foo", aggregator.build(builder));
        }

        @Test
        void fiveElements() {
            Aggregator<Maybe<String>, StringBuilder, String> aggregator = maybeStringAggregator();
            StringBuilder builder = aggregator.builder();
            aggregator.add(builder, just("foo"));
            aggregator.add(builder, nothing());
            aggregator.add(builder, just("baz"));
            aggregator.add(builder, nothing());
            aggregator.add(builder, just("quux"));
            assertEquals("foobazquux", aggregator.build(builder));
        }

        @Test
        void withInitialCapacity() {
            Aggregator<Maybe<String>, StringBuilder, String> aggregator = maybeStringAggregator(5);
            StringBuilder builder = aggregator.builder();
            builder = aggregator.add(builder, just("foo"));
            builder = aggregator.add(builder, nothing());
            builder = aggregator.add(builder, just("bar"));
            assertEquals("foobar", aggregator.build(builder));
        }
    }

    @Nested
    @DisplayName("collectionAggregator")
    class CollectionAggregator {
        @Test
        void empty() {
            Aggregator<Integer, ArrayList<Integer>, ArrayList<Integer>> aggregator = collectionAggregator(ArrayList::new);
            ArrayList<Integer> builder = aggregator.builder();
            assertEquals(Collections.emptyList(), aggregator.build(builder));
        }

        @Test
        void oneElements() {
            Aggregator<Integer, ArrayList<Integer>, ArrayList<Integer>> aggregator = collectionAggregator(ArrayList::new);
            ArrayList<Integer> builder = aggregator.builder();
            builder = aggregator.add(builder, 1);
            assertEquals(asList(1), aggregator.build(builder));
        }

        @Test
        void fiveElements() {
            Aggregator<Integer, ArrayList<Integer>, ArrayList<Integer>> aggregator = collectionAggregator(ArrayList::new);
            ArrayList<Integer> builder = aggregator.builder();
            builder = aggregator.add(builder, 1);
            builder = aggregator.add(builder, 2);
            builder = aggregator.add(builder, 3);
            builder = aggregator.add(builder, 4);
            builder = aggregator.add(builder, 5);
            assertEquals(asList(1, 2, 3, 4, 5), aggregator.build(builder));
        }
    }

    @Nested
    @DisplayName("monoidAggregator")
    class MonoidAggregator {
        private final Monoid<Integer> integerAdd = new Monoid<Integer>() {
            @Override
            public Integer identity() {
                return 0;
            }

            @Override
            public Integer checkedApply(Integer a, Integer b) {
                return a + b;
            }
        };

        @Test
        void empty() {
            Aggregator<Integer, Integer, Integer> aggregator = monoidAggregator(integerAdd);
            Integer builder = aggregator.builder();
            assertEquals(0, aggregator.build(builder));
        }

        @Test
        void oneElement() {
            Aggregator<Integer, Integer, Integer> aggregator = monoidAggregator(integerAdd);
            Integer builder = aggregator.builder();
            builder = aggregator.add(builder, 1);
            assertEquals(1, aggregator.build(builder));
        }

        @Test
        void fiveElements() {
            Aggregator<Integer, Integer, Integer> aggregator = monoidAggregator(integerAdd);
            Integer builder = aggregator.builder();
            builder = aggregator.add(builder, 1);
            builder = aggregator.add(builder, 2);
            builder = aggregator.add(builder, 3);
            builder = aggregator.add(builder, 4);
            builder = aggregator.add(builder, 5);
            assertEquals(15, aggregator.build(builder));
        }
    }
}
