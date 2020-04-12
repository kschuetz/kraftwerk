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

    static <A> Generator<A> injectsSpecialValues(NonEmptyFiniteIterable<A> specialValues, Generator<A> underlying) {
        if (underlying instanceof InjectsSpecialValues<?>) {
            return ((InjectsSpecialValues<A>) underlying).add(specialValues);
        } else {
            return new InjectsSpecialValues<>(NonEmptyVector.nonEmptyCopyFrom(specialValues), underlying);
        }
    }

    static <A> Generator<A> injectsSpecialValue(A specialValue, Generator<A> underlying) {
        return injectsSpecialValues(Vector.of(specialValue), underlying);
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class InjectsSpecialValues<A> implements Generator<A> {
        ImmutableNonEmptyVector<A> specialValues;
        Generator<A> underlying;

        InjectsSpecialValues<A> add(NonEmptyFiniteIterable<A> newValues) {
            return new InjectsSpecialValues<>(NonEmptyVector.nonEmptyCopyFrom(specialValues.concat(newValues)),
                    underlying);
        }

        @Override
        public Generate<A> prepare(GeneratorParameters generatorParameters) {
            return injectSpecial(specialValues, underlying.prepare(generatorParameters));
        }

        @Override
        public Maybe<String> getLabel() {
            return underlying.getLabel();
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
