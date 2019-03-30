package dev.marksman.composablerandom.legacy.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.OldGenerator;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.composablerandom.OldGenerator.constant;

class CoProducts {

    private static final OldGenerator<Unit> GENERATE_UNIT = constant(Unit.UNIT);

    static OldGenerator<Unit> generateUnit() {
        return GENERATE_UNIT;
    }

    static <A> OldGenerator<Maybe<A>> generateMaybe(int nothingWeight, int justWeight, OldGenerator<A> g) {
        return Weighted.leftRight(justWeight, nothingWeight,
                "justWeight", "nothingWeight",
                () -> generateJust(g), CoProducts::generateNothing);
    }

    static <A> OldGenerator<Maybe<A>> generateMaybe(int justWeight, OldGenerator<A> g) {
        return generateMaybe(justWeight, 1, g);
    }

    static <A> OldGenerator<Maybe<A>> generateMaybe(OldGenerator<A> g) {
        return generateMaybe(9, g);
    }

    static <A> OldGenerator<Maybe<A>> generateJust(OldGenerator<A> g) {
        return g.fmap(Maybe::just);
    }

    static <A> OldGenerator<Maybe<A>> generateNothing() {
        return constant(nothing());
    }

    static <L, R> OldGenerator<Either<L, R>> generateEither(int leftWeight, int rightWeight, OldGenerator<L> leftGenerator, OldGenerator<R> rightGenerator) {
        return Weighted.leftRight(leftWeight, rightWeight,
                "leftWeight", "rightWeight",
                () -> generateLeft(leftGenerator), () -> generateRight(rightGenerator));
    }

    static <L, R> OldGenerator<Either<L, R>> generateEither(int rightWeight, OldGenerator<L> leftGenerator, OldGenerator<R> rightGenerator) {
        return generateEither(1, rightWeight, leftGenerator, rightGenerator);
    }

    static <L, R> OldGenerator<Either<L, R>> generateEither(OldGenerator<L> leftGenerator, OldGenerator<R> rightGenerator) {
        return generateEither(9, leftGenerator, rightGenerator);
    }

    static <L, R> OldGenerator<Either<L, R>> generateLeft(OldGenerator<L> g) {
        return g.fmap(Either::left);
    }

    static <L, R> OldGenerator<Either<L, R>> generateRight(OldGenerator<R> g) {
        return g.fmap(Either::right);
    }

}
