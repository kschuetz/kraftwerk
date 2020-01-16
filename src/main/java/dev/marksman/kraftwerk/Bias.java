package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;
import dev.marksman.kraftwerk.bias.BiasSetting;
import dev.marksman.kraftwerk.core.BuildingBlocks;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.Result.result;

class Bias {

    static <A> Generate<A> applyBiasSetting(BiasSetting<A> biasSetting,
                                            Generate<A> underlying) {
        return biasSetting.match(__ -> underlying,
                isv -> injectSpecial(isv.getSpecialValues(), underlying));
    }

    static <A> Generator<A> injectSpecialValues(NonEmptyFiniteIterable<A> specialValues, Generator<A> inner) {
        if (inner instanceof InjectSpecialValues<?>) {
            return ((InjectSpecialValues<A>) inner).add(specialValues);
        } else {
            return new InjectSpecialValues<>(NonEmptyVector.nonEmptyCopyFrom(specialValues), inner);
        }
    }

    static <A> Generator<A> injectSpecialValue(A specialValue, Generator<A> inner) {
        return injectSpecialValues(Vector.of(specialValue), inner);
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class InjectSpecialValues<A> implements Generator<A> {
        private final ImmutableNonEmptyVector<A> specialValues;
        private final Generator<A> inner;

        InjectSpecialValues<A> add(NonEmptyFiniteIterable<A> newValues) {
            return new InjectSpecialValues<>(Vector.copyFrom(specialValues.concat(newValues)).toNonEmptyOrThrow(),
                    inner);
        }

        @Override
        public Generate<A> prepare(Parameters parameters) {
            return injectSpecial(specialValues, inner.prepare(parameters));
        }

        @Override
        public Maybe<String> getLabel() {
            return inner.getLabel();
        }
    }

    private static <A> Generate<A> injectSpecial(ImmutableNonEmptyVector<A> specialValues,
                                                 Generate<A> underlying) {
        final int specialWeight = specialValues.size();
        final int nonSpecialWeight = 20 + 3 * specialWeight;
        final int totalWeight = specialWeight + nonSpecialWeight;

        return input -> {
            long n = input.getSeedValue() % totalWeight;
            if (n < specialWeight) {
                Result<Seed, Integer> r0 = BuildingBlocks.nextIntExclusive(0, specialWeight, input);
                return result(r0.getNextState(), specialValues.unsafeGet(r0.getValue()));
            } else {
                return underlying.apply(input);
            }
        };
    }

}
