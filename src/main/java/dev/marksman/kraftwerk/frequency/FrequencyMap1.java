package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Weighted;

import static dev.marksman.kraftwerk.frequency.FrequencyMap2.frequencyMap2;

final class FrequencyMap1<A> implements FrequencyMap<A> {
    private final Weighted<Generator<A>> weightedGenerator;

    @SuppressWarnings("unchecked")
    private FrequencyMap1(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        this.weightedGenerator = (Weighted<Generator<A>>) weightedGenerator;
    }

    static <A> FrequencyMap1<A> frequencyMap1(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        if (weightedGenerator.getWeight() < 1) throw new IllegalArgumentException("initial weight must be >= 1");
        return new FrequencyMap1<>(weightedGenerator);
    }

    static void checkMultiplier(int factor) {
        if (factor < 1) throw new IllegalArgumentException("factor must be positive");
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Generator<A> toGenerator() {
        return weightedGenerator.getValue();
    }

    @Override
    public FrequencyMap<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        if (weightedGenerator.getWeight() < 1) {
            return this;
        } else {
            return frequencyMap2(this.weightedGenerator, weightedGenerator);
        }
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return other.add(weightedGenerator);
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        if (positiveFactor == 1) {
            return this;
        } else {
            return frequencyMap1(weightedGenerator.multiplyBy(positiveFactor));
        }
    }

    @Override
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        return frequencyMap1(weightedGenerator.fmap(gen -> gen.fmap(fn)));
    }
}
