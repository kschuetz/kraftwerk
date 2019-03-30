package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.OldGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Function;

import static dev.marksman.composablerandom.builtin.Generators.generateLongExclusive;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.checkMultiplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap3<A> implements FrequencyMap<A> {
    private final int weightA;
    private final OldGenerator<A> generatorA;
    private final int weightB;
    private final OldGenerator<A> generatorB;
    private final int weightC;
    private final OldGenerator<A> generatorC;

    @Override
    public OldGenerator<A> generator() {
        long thresholdB = weightA + weightB;
        return generateLongExclusive(weightA + weightB + weightC)
                .flatMap(n -> n < weightA
                        ? generatorA
                        : n < thresholdB
                        ? generatorB
                        : generatorC);
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return other.add(weightA, generatorA)
                .add(weightB, generatorB)
                .add(weightC, generatorC);
    }

    @Override
    public FrequencyMap<A> add(int weight, OldGenerator<? extends A> generator) {
        return null;
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        checkMultiplier(positiveFactor);
        if (positiveFactor == 1) return this;
        else return frequencyMap3(positiveFactor * weightA, generatorA,
                positiveFactor * weightB, generatorB,
                positiveFactor * weightC, generatorC);
    }

    @Override
    public <B> FrequencyMap<B> fmap(Function<? super A, ? extends B> fn) {
        return frequencyMap3(weightA, generatorA.fmap(fn),
                weightB, generatorB.fmap(fn),
                weightC, generatorC.fmap(fn));
    }

    static <A> FrequencyMap3<A> frequencyMap3(int weightA, OldGenerator<A> generatorA,
                                              int weightB, OldGenerator<A> generatorB,
                                              int weightC, OldGenerator<A> generatorC) {
        return new FrequencyMap3<>(weightA, generatorA, weightB, generatorB, weightC, generatorC);
    }
}
