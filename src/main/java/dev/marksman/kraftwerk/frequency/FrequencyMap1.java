package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;

import static dev.marksman.kraftwerk.frequency.FrequencyMap2.frequencyMap2;

final class FrequencyMap1<A> implements FrequencyMap<A> {
    private final int weight;
    private final Generator<A> generator;

    @SuppressWarnings("unchecked")
    private FrequencyMap1(int weight, Generator<? extends A> generator) {
        this.weight = weight;
        this.generator = (Generator<A>) generator;
    }

    @Override
    public Generator<A> toGenerator() {
        return generator;
    }

    @Override
    public FrequencyMap<A> add(int weight, Generator<? extends A> gen) {
        if (weight < 1) {
            return this;
        } else {
            return frequencyMap2(this.weight, this.generator, weight, gen);
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
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        return frequencyMap1(weight, generator.fmap(fn));
    }

    static <A> FrequencyMap1<A> frequencyMap1(int weight, Generator<? extends A> gen) {
        if (weight < 1) throw new IllegalArgumentException("initial weight must be >= 1");
        return new FrequencyMap1<>(weight, gen);
    }

    static void checkMultiplier(int factor) {
        if (factor < 1) throw new IllegalArgumentException("factor must be positive");
    }
}
