package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.frequency.FrequencyMapBuilder;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.composablerandom.EitherWeights.rightWeight;
import static dev.marksman.composablerandom.Generate.constant;
import static dev.marksman.composablerandom.MaybeWeights.justWeight;

class CoProducts {

    private static final Generate<Unit> GENERATE_UNIT = constant(Unit.UNIT);
    private static final Generate<Boolean> GENERATE_TRUE = constant(true);
    private static final Generate<Boolean> GENERATE_FALSE = constant(false);

    private static final MaybeWeights DEFAULT_MAYBE_WEIGHTS = justWeight(9).toNothing(1);
    private static final EitherWeights DEFAULT_EITHER_WEIGHTS = rightWeight(9).toLeft(1);

    static Generate<Unit> generateUnit() {
        return GENERATE_UNIT;
    }

    static Generate<Boolean> generateBoolean(BooleanWeights weights) {
        int trueWeight = weights.getTrueWeight();
        int falseWeight = weights.getFalseWeight();
        if (trueWeight == falseWeight) {
            return Generate.generateBoolean();
        } else {
            return FrequencyMapBuilder.<Boolean>frequencyMapBuilder()
                    .add(falseWeight, generateFalse())
                    .add(trueWeight, generateTrue())
                    .build()
                    .toGenerate();
        }
    }

    static Generate<Boolean> generateTrue() {
        return GENERATE_TRUE;
    }

    static Generate<Boolean> generateFalse() {
        return GENERATE_FALSE;
    }

    static <A> Generate<Maybe<A>> generateMaybe(MaybeWeights weights, Generate<A> g) {

        return FrequencyMapBuilder.<Maybe<A>>frequencyMapBuilder()
                .add(weights.getJustWeight(), generateJust(g))
                .add(weights.getNothingWeight(), generateNothing())
                .build()
                .toGenerate();
    }

    static <A> Generate<Maybe<A>> generateMaybe(Generate<A> g) {
        return generateMaybe(DEFAULT_MAYBE_WEIGHTS, g);
    }

    static <A> Generate<Maybe<A>> generateJust(Generate<A> g) {
        return g.fmap(Maybe::just);
    }

    static <A> Generate<Maybe<A>> generateNothing() {
        return constant(nothing());
    }

    static <L, R> Generate<Either<L, R>> generateEither(EitherWeights weights, Generate<L> leftGenerator, Generate<R> rightGenerator) {
        return FrequencyMapBuilder.<Either<L, R>>frequencyMapBuilder()
                .add(weights.getLeftWeight(), CoProducts.<L, R>generateLeft(leftGenerator))
                .add(weights.getRightWeight(), generateRight(rightGenerator))
                .build()
                .toGenerate();
    }

    static <L, R> Generate<Either<L, R>> generateEither(Generate<L> leftGenerator, Generate<R> rightGenerator) {
        return generateEither(DEFAULT_EITHER_WEIGHTS, leftGenerator, rightGenerator);
    }

    static <L, R> Generate<Either<L, R>> generateLeft(Generate<L> g) {
        return g.fmap(Either::left);
    }

    static <L, R> Generate<Either<L, R>> generateRight(Generate<R> g) {
        return g.fmap(Either::right);
    }

}
