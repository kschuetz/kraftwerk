package software.kes.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Weighted;

import static software.kes.kraftwerk.Generators.generateLongIndex;
import static software.kes.kraftwerk.frequency.FrequencyMap3.frequencyMap3;
import static software.kes.kraftwerk.frequency.FrequencyMapN.addLabel;

final class FrequencyMap2<A> implements FrequencyMap<A> {
    private final Weighted<Generator<A>> weightedGeneratorA;
    private final Weighted<Generator<A>> weightedGeneratorB;

    @SuppressWarnings("unchecked")
    private FrequencyMap2(Weighted<? extends Generator<? extends A>> weightedGeneratorA,
                          Weighted<? extends Generator<? extends A>> weightedGeneratorB) {
        this.weightedGeneratorA = (Weighted<Generator<A>>) weightedGeneratorA;
        this.weightedGeneratorB = (Weighted<Generator<A>>) weightedGeneratorB;
    }

    static <A> FrequencyMap2<A> frequencyMap2(Weighted<? extends Generator<? extends A>> weightedGeneratorA,
                                              Weighted<? extends Generator<? extends A>> weightedGeneratorB) {
        return new FrequencyMap2<>(weightedGeneratorA, weightedGeneratorB);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Generator<A> toGenerator() {
        int weightA = weightedGeneratorA.getWeight();
        int weightB = weightedGeneratorB.getWeight();
        Generator<A> generatorA = weightedGeneratorA.getValue();
        Generator<A> generatorB = weightedGeneratorB.getValue();
        long total = weightA + weightB;
        return addLabel(generateLongIndex(total)
                .flatMap(n -> n < weightA
                        ? generatorA
                        : generatorB));
    }

    @Override
    public FrequencyMap<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        if (weightedGenerator.getWeight() < 1) {
            return this;
        } else {
            return frequencyMap3(weightedGeneratorA, weightedGeneratorB, weightedGenerator);
        }
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return other.add(weightedGeneratorA).add(weightedGeneratorB);
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        if (positiveFactor == 1) {
            return this;
        } else {
            return frequencyMap2(weightedGeneratorA.multiplyBy(positiveFactor),
                    weightedGeneratorB.multiplyBy(positiveFactor));
        }
    }

    @Override
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        Fn1<Generator<A>, ? extends Generator<? extends B>> mapGenerator = gen -> gen.fmap(fn);
        return frequencyMap2(weightedGeneratorA.fmap(mapGenerator),
                weightedGeneratorB.fmap(mapGenerator));
    }
}
