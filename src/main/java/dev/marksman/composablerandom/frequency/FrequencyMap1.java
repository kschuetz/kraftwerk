package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.frequency.FrequencyMap2.frequencyMap2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap1<A> implements FrequencyMap<A> {
    private final int weight;
    private final Generate<A> generate;

    @Override
    public Generate<A> toGenerate() {
        return generate;
    }

    @Override
    public FrequencyMap<A> add(int weight, Generate<? extends A> gen) {
        if (weight < 1) return this;
        else {
            @SuppressWarnings("unchecked")
            Generate<A> generatorB = (Generate<A>) gen;
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

    static <A> FrequencyMap1<A> frequencyMap1(int weight, Generate<A> gen) {
        if (weight < 1) throw new IllegalArgumentException("initial weight must be >= 1");
        return new FrequencyMap1<>(weight, gen);
    }

    static void checkMultiplier(int factor) {
        if (factor < 1) throw new IllegalArgumentException("factor must be positive");
    }
}
