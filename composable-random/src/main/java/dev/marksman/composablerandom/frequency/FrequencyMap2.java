package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.OldGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Function;

import static dev.marksman.composablerandom.builtin.Generators.generateLongExclusive;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.checkMultiplier;
import static dev.marksman.composablerandom.frequency.FrequencyMap3.frequencyMap3;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap2<A> implements FrequencyMap<A> {
    private final int weightA;
    private final OldGenerator<A> generatorA;
    private final int weightB;
    private final OldGenerator<A> generatorB;

    @Override
    public OldGenerator<A> generator() {
        long total = weightA + weightB;

        return generateLongExclusive(total)
                .flatMap(n -> n < weightA
                        ? generatorA
                        : generatorB);
    }

    @Override
    public FrequencyMap<A> add(int weight, OldGenerator<? extends A> generator) {
        if (weight < 1) return this;
        else {
            @SuppressWarnings("unchecked")
            OldGenerator<A> generatorC = (OldGenerator<A>) generator;
            return frequencyMap3(weightA, generatorA, weightB, generatorB, weight, generatorC);
        }
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return other.add(weightA, generatorA).add(weightB, generatorB);
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        checkMultiplier(positiveFactor);
        if (positiveFactor == 1) return this;
        else return frequencyMap2(positiveFactor * weightA, generatorA,
                positiveFactor * weightB, generatorB);
    }

    @Override
    public <B> FrequencyMap<B> fmap(Function<? super A, ? extends B> fn) {
        return frequencyMap2(weightA, generatorA.fmap(fn),
                weightB, generatorB.fmap(fn));
    }

    static <A> FrequencyMap2<A> frequencyMap2(int weightA, OldGenerator<A> generatorA,
                                              int weightB, OldGenerator<A> generatorB) {
        return new FrequencyMap2<>(weightA, generatorA, weightB, generatorB);
    }
}
