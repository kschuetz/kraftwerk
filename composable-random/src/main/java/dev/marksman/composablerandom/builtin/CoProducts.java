package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.composablerandom.Generator;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.builtin.Generators.generateIntExclusive;

class CoProducts {

    static <A> Generator<Maybe<A>> maybe(int justFrequency, Generator<A> g) {
        if (justFrequency < 0) {
            throw new IllegalArgumentException("justFrequency must be >= 0");
        } else if (justFrequency == 0) {
            return constant(nothing());
        }
        Generator<Maybe<A>> just = g.fmap(Maybe::just);
        Generator<Maybe<A>> nothing = constant(nothing());
        return generateIntExclusive(1 + justFrequency)
                .flatMap(n -> n == 0 ? nothing : just);
    }

    static <A> Generator<Maybe<A>> maybe(Generator<A> g) {
        return maybe(9, g);
    }

    static <A> Generator<Maybe<A>> just(Generator<A> g) {
        return g.fmap(Maybe::just);
    }

}
