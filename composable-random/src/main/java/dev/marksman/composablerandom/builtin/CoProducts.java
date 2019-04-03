package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.frequency.FrequencyMapBuilder;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.composablerandom.Generator.constant;

class CoProducts {

    private static final Generator<Unit> GENERATE_UNIT = constant(Unit.UNIT);

    static Generator<Unit> generateUnit() {
        return GENERATE_UNIT;
    }

    static <A> Generator<Maybe<A>> generateMaybe(int nothingWeight, int justWeight, Generator<A> g) {
        checkWeights("nothingWeight", nothingWeight,
                "justWeight", justWeight);
        return FrequencyMapBuilder.<Maybe<A>>frequencyMapBuilder()
                .add(justWeight, generateJust(g))
                .add(nothingWeight, generateNothing())
                .build()
                .generator();
    }

    static <A> Generator<Maybe<A>> generateMaybe(int justWeight, Generator<A> g) {
        return generateMaybe(justWeight, 1, g);
    }

    static <A> Generator<Maybe<A>> generateMaybe(Generator<A> g) {
        return generateMaybe(9, g);
    }

    static <A> Generator<Maybe<A>> generateJust(Generator<A> g) {
        return g.fmap(Maybe::just);
    }

    static <A> Generator<Maybe<A>> generateNothing() {
        return constant(nothing());
    }

    static <L, R> Generator<Either<L, R>> generateEither(int leftWeight, int rightWeight, Generator<L> leftGenerator, Generator<R> rightGenerator) {
        checkWeights("leftWeight", leftWeight,
                "rightWeight", rightWeight);

        return FrequencyMapBuilder.<Either<L, R>>frequencyMapBuilder()
                .add(leftWeight, CoProducts.<L, R>generateLeft(leftGenerator))
                .add(rightWeight, generateRight(rightGenerator))
                .build()
                .generator();
    }

    static <L, R> Generator<Either<L, R>> generateEither(int rightWeight, Generator<L> leftGenerator, Generator<R> rightGenerator) {
        return generateEither(1, rightWeight, leftGenerator, rightGenerator);
    }

    static <L, R> Generator<Either<L, R>> generateEither(Generator<L> leftGenerator, Generator<R> rightGenerator) {
        return generateEither(9, leftGenerator, rightGenerator);
    }

    static <L, R> Generator<Either<L, R>> generateLeft(Generator<L> g) {
        return g.fmap(Either::left);
    }

    static <L, R> Generator<Either<L, R>> generateRight(Generator<R> g) {
        return g.fmap(Either::right);
    }

    private static void checkWeights(String leftName, int leftWeight,
                                     String rightName, int rightWeight) {
        if (leftWeight < 0) {
            throw new IllegalArgumentException(leftName + " must be >= 0");
        }
        if (rightWeight < 0) {
            throw new IllegalArgumentException(rightName + " must be >= 0");
        }
        int total = leftWeight + rightWeight;
        if (total < 1) {
            throw new IllegalArgumentException("sum of weights must be >= 1");
        }
    }

}
