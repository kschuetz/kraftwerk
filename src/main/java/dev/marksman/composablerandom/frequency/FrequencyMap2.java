package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Generate.generateLongExclusive;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.checkMultiplier;
import static dev.marksman.composablerandom.frequency.FrequencyMap3.frequencyMap3;
import static dev.marksman.composablerandom.frequency.FrequencyMapN.addLabel;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMap2<A> implements FrequencyMap<A> {
    private final int weightA;
    private final Generate<A> generatorA;
    private final int weightB;
    private final Generate<A> generatorB;

    @Override
    public Generate<A> toGenerate() {
        long total = weightA + weightB;

        return addLabel(generateLongExclusive(total)
                .flatMap(n -> n < weightA
                        ? generatorA
                        : generatorB));
    }

    @Override
    public FrequencyMap<A> add(int weight, Generate<? extends A> gen) {
        if (weight < 1) return this;
        else {
            @SuppressWarnings("unchecked")
            Generate<A> generatorC = (Generate<A>) gen;
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
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        return frequencyMap2(weightA, generatorA.fmap(fn),
                weightB, generatorB.fmap(fn));
    }

    static <A> FrequencyMap2<A> frequencyMap2(int weightA, Generate<A> generatorA,
                                              int weightB, Generate<A> generatorB) {
        return new FrequencyMap2<>(weightA, generatorA, weightB, generatorB);
    }
}
