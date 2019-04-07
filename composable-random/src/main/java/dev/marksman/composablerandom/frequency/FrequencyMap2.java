package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Function;

import static dev.marksman.composablerandom.builtin.Generators.generateLongExclusive;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.checkMultiplier;
import static dev.marksman.composablerandom.frequency.FrequencyMap3.frequencyMap3;
import static dev.marksman.composablerandom.frequency.FrequencyMapN.addLabel;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap2<A> implements FrequencyMap<A> {
    private final int weightA;
    private final Generator<A> generatorA;
    private final int weightB;
    private final Generator<A> generatorB;

    @Override
    public Generator<A> toGenerator() {
        long total = weightA + weightB;

        return addLabel(generateLongExclusive(total)
                .flatMap(n -> n < weightA
                        ? generatorA
                        : generatorB));
    }

    @Override
    public FrequencyMap<A> add(int weight, Generator<? extends A> generator) {
        if (weight < 1) return this;
        else {
            @SuppressWarnings("unchecked")
            Generator<A> generatorC = (Generator<A>) generator;
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

    static <A> FrequencyMap2<A> frequencyMap2(int weightA, Generator<A> generatorA,
                                              int weightB, Generator<A> generatorB) {
        return new FrequencyMap2<>(weightA, generatorA, weightB, generatorB);
    }
}
