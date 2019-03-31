package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Function;

import static dev.marksman.composablerandom.frequency.FrequencyMap2.frequencyMap2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap1<A> implements FrequencyMap<A> {
    private final int weight;
    private final Generator<A> generator;

    @Override
    public Generator<A> generator() {
        return generator;
    }

    @Override
    public FrequencyMap<A> add(int weight, Generator<? extends A> generator) {
        if (weight < 1) return this;
        else {
            @SuppressWarnings("unchecked")
            Generator<A> generatorB = (Generator<A>) generator;
            return frequencyMap2(this.weight, this.generator, weight, generatorB);
        }
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return other.add(weight, generator);
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        checkMultiplier(positiveFactor);
        if (positiveFactor == 1) return this;
        else return frequencyMap1(positiveFactor * weight, generator);
    }

    @Override
    public <B> FrequencyMap<B> fmap(Function<? super A, ? extends B> fn) {
        return frequencyMap1(weight, generator.fmap(fn));
    }

    static <A> FrequencyMap1<A> frequencyMap1(int weight, Generator<A> generator) {
        if (weight < 1) throw new IllegalArgumentException("initial weight must be >= 1");
        return new FrequencyMap1<>(weight, generator);
    }

    static void checkMultiplier(int factor) {
        if (factor < 1) throw new IllegalArgumentException("factor must be positive");
    }
}
