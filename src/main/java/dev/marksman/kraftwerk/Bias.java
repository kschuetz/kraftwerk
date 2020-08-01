package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;
import dev.marksman.kraftwerk.bias.BiasSetting;
import dev.marksman.kraftwerk.bias.BiasSettings;
import dev.marksman.kraftwerk.core.BuildingBlocks;

import static dev.marksman.kraftwerk.Result.result;

final class Bias {
    static <A> Generate<A> applyBiasSetting(BiasSetting<A> biasSetting,
                                            Generate<A> underlying) {
        return biasSetting.match(__ -> underlying,
                isv -> injectSpecial(isv.getSpecialValues(), underlying));
    }

    static <A> Generator<A> applyBiasSetting(Fn1<BiasSettings, BiasSetting<A>> getBiasSetting,
                                             Generator<A> underlying) {
        return generatorParameters -> applyBiasSetting(getBiasSetting.apply(generatorParameters.getBiasSettings()),
                underlying.prepare(generatorParameters));
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

    private static final class InjectsSpecialValues<A> implements Generator<A> {
        private final ImmutableNonEmptyVector<A> specialValues;
        private final Generator<A> underlying;

        private InjectsSpecialValues(ImmutableNonEmptyVector<A> specialValues, Generator<A> underlying) {
            this.specialValues = specialValues;
            this.underlying = underlying;
        }

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

        public ImmutableNonEmptyVector<A> getSpecialValues() {
            return this.specialValues;
        }

        public Generator<A> getUnderlying() {
            return this.underlying;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InjectsSpecialValues<?> that = (InjectsSpecialValues<?>) o;

            if (!specialValues.equals(that.specialValues)) return false;
            return underlying.equals(that.underlying);
        }

        @Override
        public int hashCode() {
            int result = specialValues.hashCode();
            result = 31 * result + underlying.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "InjectsSpecialValues{" +
                    "specialValues=" + specialValues +
                    ", underlying=" + underlying +
                    '}';
        }
    }
}
