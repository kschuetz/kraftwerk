package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.frequency.FrequencyMapBuilder;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.composablerandom.EitherWeights.rightWeight;
import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.MaybeWeights.justWeight;

class CoProducts {

    private static final Generator<Unit> GENERATE_UNIT = constant(Unit.UNIT);
    private static final Generator<Boolean> GENERATE_TRUE = constant(true);
    private static final Generator<Boolean> GENERATE_FALSE = constant(false);

    private static final MaybeWeights DEFAULT_MAYBE_WEIGHTS = justWeight(9).toNothing(1);
    private static final EitherWeights DEFAULT_EITHER_WEIGHTS = rightWeight(9).toLeft(1);

    static Generator<Unit> generateUnit() {
        return GENERATE_UNIT;
    }

    static Generator<Boolean> generateBoolean(BooleanWeights weights) {
        int trueWeight = weights.getTrueWeight();
        int falseWeight = weights.getFalseWeight();
        if (trueWeight == falseWeight) {
            return Generator.generateBoolean();
        } else {
            return FrequencyMapBuilder.<Boolean>frequencyMapBuilder()
                    .add(falseWeight, generateFalse())
                    .add(trueWeight, generateTrue())
                    .build()
                    .toGenerator();
        }
    }

    static Generator<Boolean> generateTrue() {
        return GENERATE_TRUE;
    }

    static Generator<Boolean> generateFalse() {
        return GENERATE_FALSE;
    }

    static <A> Generator<Maybe<A>> generateMaybe(MaybeWeights weights, Generator<A> g) {

        return FrequencyMapBuilder.<Maybe<A>>frequencyMapBuilder()
                .add(weights.getJustWeight(), generateJust(g))
                .add(weights.getNothingWeight(), generateNothing())
                .build()
                .toGenerator();
    }

    static <A> Generator<Maybe<A>> generateMaybe(Generator<A> g) {
        return generateMaybe(DEFAULT_MAYBE_WEIGHTS, g);
    }

    static <A> Generator<Maybe<A>> generateJust(Generator<A> g) {
        return g.fmap(Maybe::just);
    }

    static <A> Generator<Maybe<A>> generateNothing() {
        return constant(nothing());
    }

    static <L, R> Generator<Either<L, R>> generateEither(EitherWeights weights, Generator<L> leftGenerator, Generator<R> rightGenerator) {
        return FrequencyMapBuilder.<Either<L, R>>frequencyMapBuilder()
                .add(weights.getLeftWeight(), CoProducts.<L, R>generateLeft(leftGenerator))
                .add(weights.getRightWeight(), generateRight(rightGenerator))
                .build()
                .toGenerator();
    }

    static <L, R> Generator<Either<L, R>> generateEither(Generator<L> leftGenerator, Generator<R> rightGenerator) {
        return generateEither(DEFAULT_EITHER_WEIGHTS, leftGenerator, rightGenerator);
    }

    static <L, R> Generator<Either<L, R>> generateLeft(Generator<L> g) {
        return g.fmap(Either::left);
    }

    static <L, R> Generator<Either<L, R>> generateRight(Generator<R> g) {
        return g.fmap(Either::right);
    }

}
