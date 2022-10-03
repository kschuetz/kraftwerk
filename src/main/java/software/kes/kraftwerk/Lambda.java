package software.kes.kraftwerk;

import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static software.kes.kraftwerk.aggregator.Aggregator.aggregator;
import static software.kes.kraftwerk.aggregator.Aggregators.monoidAggregator;

final class Lambda {
    private Lambda() {
    }

    static <A> Generator<A> generateFromSemigroup(Semigroup<A> semigroup, Generator<A> gen) {
        return Generators.sized(size -> generateNFromSemigroup(semigroup, gen, Math.max(1, size)));
    }

    static <A> Generator<A> generateNFromSemigroup(Semigroup<A> semigroup, Generator<A> gen, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be >= 1");
        } else if (count == 1) {
            return gen;
        } else {
            return gen.flatMap(initial ->
                    Generators.sized(size ->
                            Generators.aggregate(aggregator(() -> initial,
                                    semigroup::apply,
                                    id()),
                                    count - 1,
                                    gen)));
        }

    }

    static <A> Generator<A> generateFromMonoid(Monoid<A> monoid, Generator<A> gen) {
        return Generators.sized(size -> generateNFromMonoid(monoid, gen, size));
    }

    static <A> Generator<A> generateNFromMonoid(Monoid<A> monoid, Generator<A> gen, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        } else if (count == 1) {
            return Generators.constant(monoid.identity());
        } else {
            return Generators.aggregate(monoidAggregator(monoid), count, gen);
        }
    }
}
