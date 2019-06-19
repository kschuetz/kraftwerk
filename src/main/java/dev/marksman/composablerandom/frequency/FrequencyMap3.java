package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.FrequencyEntry.entry;
import static dev.marksman.composablerandom.Generate.generateLongExclusive;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.checkMultiplier;
import static dev.marksman.composablerandom.frequency.FrequencyMapN.addLabel;
import static dev.marksman.composablerandom.frequency.FrequencyMapN.frequencyMapN;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap3<A> implements FrequencyMap<A> {
    private final int weightA;
    private final Generate<A> generatorA;
    private final int weightB;
    private final Generate<A> generatorB;
    private final int weightC;
    private final Generate<A> generatorC;

    @Override
    public Generate<A> toGenerate() {
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
    public FrequencyMap<A> add(int weight, Generate<? extends A> gen) {
        if (weight < 1) {
            return this;
        } else {
            //noinspection unchecked
            return frequencyMapN((FrequencyEntry<A>) entry(weight, gen),
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
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        return frequencyMap3(weightA, generatorA.fmap(fn),
                weightB, generatorB.fmap(fn),
                weightC, generatorC.fmap(fn));
    }

    static <A> FrequencyMap3<A> frequencyMap3(int weightA, Generate<A> generatorA,
                                              int weightB, Generate<A> generatorB,
                                              int weightC, Generate<A> generatorC) {
        return new FrequencyMap3<>(weightA, generatorA, weightB, generatorB, weightC, generatorC);
    }
}
