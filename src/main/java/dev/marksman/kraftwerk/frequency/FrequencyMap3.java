package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Weighted;

import static dev.marksman.kraftwerk.Generators.generateLongIndex;
import static dev.marksman.kraftwerk.frequency.FrequencyMapN.addLabel;
import static dev.marksman.kraftwerk.frequency.FrequencyMapN.frequencyMapN;
import static java.util.Arrays.asList;

final class FrequencyMap3<A> implements FrequencyMap<A> {
    private final Weighted<Generator<A>> weightedGeneratorA;
    private final Weighted<Generator<A>> weightedGeneratorB;
    private final Weighted<Generator<A>> weightedGeneratorC;

    @SuppressWarnings("unchecked")
    private FrequencyMap3(Weighted<? extends Generator<? extends A>> weightedGeneratorA,
                          Weighted<? extends Generator<? extends A>> weightedGeneratorB,
                          Weighted<? extends Generator<? extends A>> weightedGeneratorC) {
        this.weightedGeneratorA = (Weighted<Generator<A>>) weightedGeneratorA;
        this.weightedGeneratorB = (Weighted<Generator<A>>) weightedGeneratorB;
        this.weightedGeneratorC = (Weighted<Generator<A>>) weightedGeneratorC;
    }

    static <A> FrequencyMap3<A> frequencyMap3(Weighted<? extends Generator<? extends A>> weightedGeneratorA,
                                              Weighted<? extends Generator<? extends A>> weightedGeneratorB,
                                              Weighted<? extends Generator<? extends A>> weightedGeneratorC) {
        return new FrequencyMap3<>(weightedGeneratorA, weightedGeneratorB, weightedGeneratorC);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Generator<A> toGenerator() {
        int weightA = weightedGeneratorA.getWeight();
        int weightB = weightedGeneratorB.getWeight();
        int weightC = weightedGeneratorC.getWeight();
        Generator<A> generatorA = weightedGeneratorA.getValue();
        Generator<A> generatorB = weightedGeneratorB.getValue();
        Generator<A> generatorC = weightedGeneratorC.getValue();

        long thresholdB = weightA + weightB;
        return addLabel(generateLongIndex(weightA + weightB + weightC)
                .flatMap(n -> n < weightA
                        ? generatorA
                        : n < thresholdB
                        ? generatorB
                        : generatorC));
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return other.add(weightedGeneratorA)
                .add(weightedGeneratorB)
                .add(weightedGeneratorC);
    }

    @Override
    public FrequencyMap<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        if (weightedGenerator.getWeight() < 1) {
            return this;
        } else {
            return frequencyMapN(weightedGenerator, asList(weightedGeneratorA, weightedGeneratorB, weightedGeneratorC));
        }
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        if (positiveFactor == 1) {
            return this;
        } else {
            return frequencyMap3(weightedGeneratorA.multiplyBy(positiveFactor),
                    weightedGeneratorB.multiplyBy(positiveFactor),
                    weightedGeneratorC.multiplyBy(positiveFactor));
        }
    }

    @Override
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        Fn1<Generator<A>, ? extends Generator<? extends B>> mapGenerator = gen -> gen.fmap(fn);
        return frequencyMap3(weightedGeneratorA.fmap(mapGenerator),
                weightedGeneratorB.fmap(mapGenerator),
                weightedGeneratorC.fmap(mapGenerator));
    }
}
