package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

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

    static <A> FrequencyMap1<A> frequencyMap1(int weight, Generator<A> generator) {
        if (weight < 1) throw new IllegalArgumentException("initial weight must be >= 1");
        return new FrequencyMap1<>(weight, generator);
    }
}
