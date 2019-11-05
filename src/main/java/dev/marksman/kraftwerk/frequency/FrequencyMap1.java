package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.frequency.FrequencyMap2.frequencyMap2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap1<A> implements FrequencyMap<A> {
    private final int weight;
    private final Generator<A> generate;

    @Override
    public Generator<A> toGenerator() {
        return generate;
    }

    @Override
    public FrequencyMap<A> add(int weight, Generator<? extends A> gen) {
        if (weight < 1) return this;
        else {
            @SuppressWarnings("unchecked")
            Generator<A> generatorB = (Generator<A>) gen;
            return frequencyMap2(this.weight, this.generate, weight, generatorB);
        }
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return other.add(weight, generate);
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        checkMultiplier(positiveFactor);
        if (positiveFactor == 1) return this;
        else return frequencyMap1(positiveFactor * weight, generate);
    }

    @Override
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        return frequencyMap1(weight, generate.fmap(fn));
    }

    static <A> FrequencyMap1<A> frequencyMap1(int weight, Generator<A> gen) {
        if (weight < 1) throw new IllegalArgumentException("initial weight must be >= 1");
        return new FrequencyMap1<>(weight, gen);
    }

    static void checkMultiplier(int factor) {
        if (factor < 1) throw new IllegalArgumentException("factor must be positive");
    }
}
