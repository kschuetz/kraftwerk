package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Function;

import static dev.marksman.composablerandom.FrequencyEntry.entry;
import static dev.marksman.composablerandom.Generator.generateLongExclusive;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.checkMultiplier;
import static dev.marksman.composablerandom.frequency.FrequencyMapN.addLabel;
import static dev.marksman.composablerandom.frequency.FrequencyMapN.frequencyMapN;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap3<A> implements FrequencyMap<A> {
    private final int weightA;
    private final Generator<A> generatorA;
    private final int weightB;
    private final Generator<A> generatorB;
    private final int weightC;
    private final Generator<A> generatorC;

    @Override
    public Generator<A> toGenerator() {
        long thresholdB = weightA + weightB;
        return addLabel(generateLongExclusive(weightA + weightB + weightC)
                .flatMap(n -> n < weightA
                        ? generatorA
                        : n < thresholdB
                        ? generatorB
                        : generatorC));
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return other.add(weightA, generatorA)
                .add(weightB, generatorB)
                .add(weightC, generatorC);
    }

    @Override
    public FrequencyMap<A> add(int weight, Generator<? extends A> generator) {
        if (weight < 1) {
            return this;
        } else {
            //noinspection unchecked
            return frequencyMapN((FrequencyEntry<A>) entry(weight, generator),
                    asList(entry(weightA, generatorA), entry(weightB, generatorB), entry(weightC, generatorC)));
        }
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

    static <A> FrequencyMap3<A> frequencyMap3(int weightA, Generator<A> generatorA,
                                              int weightB, Generator<A> generatorB,
                                              int weightC, Generator<A> generatorC) {
        return new FrequencyMap3<>(weightA, generatorA, weightB, generatorB, weightC, generatorC);
    }
}
