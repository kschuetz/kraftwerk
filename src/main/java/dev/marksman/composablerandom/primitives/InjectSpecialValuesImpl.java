package dev.marksman.composablerandom.primitives;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;

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
    public Result<? extends Seed, Elem> run(Seed input) {
        // TODO: InjectSpecialValuesImpl
        long n = input.getSeedValue() % totalWeight;
        if (n < specialWeight) {
            Result<? extends Seed, Integer> nextSeed = input.nextInt();
            return result(nextSeed.getNextState(), elements.unsafeGet((int) n));
        } else {
            return inner.run(input);
        }
    }

    public static <Elem> InjectSpecialValuesImpl<Elem> mixInSpecialValuesImpl(ImmutableNonEmptyVector<Elem> elements, long nonSpecialWeight, GeneratorImpl<Elem> inner) {
        return new InjectSpecialValuesImpl<>(elements, nonSpecialWeight, inner);
    }

}
