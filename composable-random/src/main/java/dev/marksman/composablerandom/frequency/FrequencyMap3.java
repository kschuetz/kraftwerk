package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.builtin.Generators.generateLongExclusive;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap3<A> implements FrequencyMap<A> {
    private final int weightA;
    private final Generator<A> generatorA;
    private final int weightB;
    private final Generator<A> generatorB;
    private final int weightC;
    private final Generator<A> generatorC;

    @Override
    public Generator<A> generator() {
        long thresholdB = weightA + weightB;
        return generateLongExclusive(weightA + weightB + weightC)
                .flatMap(n -> n < weightA
                        ? generatorA
                        : n < thresholdB
                        ? generatorB
                        : generatorC);
    }

    @Override
    public FrequencyMap<A> add(int weight, Generator<? extends A> generator) {
        return null;
    }

    static <A> FrequencyMap3<A> frequencyMap3(int weightA, Generator<A> generatorA,
                                              int weightB, Generator<A> generatorB,
                                              int weightC, Generator<A> generatorC) {
        return new FrequencyMap3<>(weightA, generatorA, weightB, generatorB, weightC, generatorC);
    }
}
