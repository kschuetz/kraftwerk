package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

class Bias {

    static <A> Generate<A> applyBiasSetting(BiasSetting<A> biasSetting,
                                            Generate<A> underlying) {
        return biasSetting.match(__ -> underlying,
                isv -> injectSpecial(isv.getSpecialValues(), underlying));
    }

    static <A> Generate<A> injectSpecial(ImmutableNonEmptyVector<A> specialValues,
                                         Generate<A> underlying) {
        return underlying;
    }

    static <A> Generator<A> injectSpecialValues(NonEmptyFiniteIterable<A> specialValues, Generator<A> inner) {
        if (inner instanceof InjectSpecialValues<?>) {
            return ((InjectSpecialValues<A>) inner).add(specialValues);
        } else {
            // TODO:  NonEmptyVector.nonEmptyCopyFrom()
            return new InjectSpecialValues<>(Vector.copyFrom(specialValues).toNonEmptyOrThrow(), inner);
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

    /*

    private final ImmutableNonEmptyVector<Elem> elements;
    private final int specialWeight;
    private final long totalWeight;
    private final GeneratorImpl<Elem> inner;

    private InjectSpecialValuesImpl(ImmutableNonEmptyVector<Elem> elements, long nonSpecialWeight, GeneratorImpl<Elem> inner) {
        this.elements = elements;
        this.specialWeight = elements.size();
        this.totalWeight = Math.max(0, nonSpecialWeight) + specialWeight;
        this.inner = inner;
    }

    @Override
    public Result<? extends LegacySeed, Elem> run(LegacySeed input) {
        // TODO: InjectSpecialValuesImpl
        long n = input.getSeedValue() % totalWeight;
        if (n < specialWeight) {
            Result<? extends LegacySeed, Integer> nextSeed = input.nextInt();
            return result(nextSeed.getNextState(), elements.unsafeGet((int) n));
        } else {
            return inner.run(input);
        }
    }

    if (gen instanceof Generator.InjectSpecialValues) {
            Generator.InjectSpecialValues<A> g1 = (Generator.InjectSpecialValues<A>) gen;
            NonEmptyFiniteIterable<A> acc = g1.getSpecialValues();
            while (g1.getInner() instanceof Generator.InjectSpecialValues) {
                g1 = (Generator.InjectSpecialValues<A>) g1.getInner();
                acc = acc.concat(g1.getSpecialValues());
            }
            ImmutableNonEmptyVector<A> specialValues = NonEmptyVector.copyFromOrThrow(acc);
            return mixInSpecialValuesImpl(specialValues, 20 + 3 * specialValues.size(),
                    context.recurse(g1.getInner()));
        }
     */

}
