package dev.marksman.composablerandom.legacy.primitives;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;

import static dev.marksman.composablerandom.Result.result;

public class InjectSpecialValuesImpl<Elem> implements GeneratorImpl<Elem> {
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

    /*
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

    public static <Elem> InjectSpecialValuesImpl<Elem> mixInSpecialValuesImpl(ImmutableNonEmptyVector<Elem> elements, long nonSpecialWeight, GeneratorImpl<Elem> inner) {
        return new InjectSpecialValuesImpl<>(elements, nonSpecialWeight, inner);
    }

}
