package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.Weighted;

import static dev.marksman.kraftwerk.frequency.FrequencyMapBuilder0.frequencyMapBuilder0;

public interface FrequencyMapBuilder<A> {
    FrequencyMapBuilder<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator);

    FrequencyMapBuilder<A> combine(FrequencyMap<A> other);

    FrequencyMap<A> build();

    <B> FrequencyMapBuilder<B> fmap(Fn1<? super A, ? extends B> fn);

    FrequencyMapBuilder<A> multiply(int positiveFactor);

    default FrequencyMapBuilder<A> add(Generator<? extends A> gen) {
        return add(gen.weighted(1));
    }

    default FrequencyMapBuilder<A> addValue(Weighted<? extends A> weightedValue) {
        return add(weightedValue.fmap(Generators::constant));
    }

    default FrequencyMapBuilder<A> addValue(A value) {
        return addValue(Weighted.weighted(value));
    }

    static <A> FrequencyMapBuilder<A> frequencyMapBuilder() {
        return frequencyMapBuilder0();
    }
}
