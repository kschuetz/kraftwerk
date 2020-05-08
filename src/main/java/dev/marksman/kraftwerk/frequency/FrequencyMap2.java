package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;

import static dev.marksman.kraftwerk.Generators.generateLongIndex;
import static dev.marksman.kraftwerk.frequency.FrequencyMap1.checkMultiplier;
import static dev.marksman.kraftwerk.frequency.FrequencyMap3.frequencyMap3;
import static dev.marksman.kraftwerk.frequency.FrequencyMapN.addLabel;

final class FrequencyMap2<A> implements FrequencyMap<A> {
    private final int weightA;
    private final Generator<A> generatorA;
    private final int weightB;
    private final Generator<A> generatorB;

    @SuppressWarnings("unchecked")
    private FrequencyMap2(int weightA, Generator<? extends A> generatorA, int weightB, Generator<? extends A> generatorB) {
        this.weightA = weightA;
        this.generatorA = (Generator<A>) generatorA;
        this.weightB = weightB;
        this.generatorB = (Generator<A>) generatorB;
    }

    @Override
    public Generator<A> toGenerator() {
        long total = weightA + weightB;

        return addLabel(generateLongIndex(total)
                .flatMap(n -> n < weightA
                        ? generatorA
                        : generatorB));
    }

    @Override
    public FrequencyMap<A> add(int weight, Generator<? extends A> gen) {
        if (weight < 1) {
            return this;
        } else {
            return frequencyMap3(weightA, generatorA, weightB, generatorB, weight, gen);
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
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        return frequencyMap2(weightA, generatorA.fmap(fn),
                weightB, generatorB.fmap(fn));
    }

    static <A> FrequencyMap2<A> frequencyMap2(int weightA, Generator<? extends A> generatorA,
                                              int weightB, Generator<? extends A> generatorB) {
        return new FrequencyMap2<>(weightA, generatorA, weightB, generatorB);
    }
}
