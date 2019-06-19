package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generate;

import static dev.marksman.composablerandom.Generate.constant;
import static dev.marksman.composablerandom.frequency.FrequencyMapBuilder0.frequencyMapBuilder0;

public interface FrequencyMapBuilder<A> {
    FrequencyMapBuilder<A> add(int weight, Generate<? extends A> gen);

    FrequencyMapBuilder<A> combine(FrequencyMap<A> other);

    FrequencyMap<A> build();

    <B> FrequencyMapBuilder<B> fmap(Fn1<? super A, ? extends B> fn);

    FrequencyMapBuilder<A> multiply(int positiveFactor);

    default FrequencyMapBuilder<A> add(Generate<? extends A> gen) {
        return add(1, gen);
    }

    default FrequencyMapBuilder<A> addValue(int weight, A value) {
        return add(weight, constant(value));
    }

    default FrequencyMapBuilder<A> addValue(A value) {
        return add(1, constant(value));
    }

    default FrequencyMapBuilder<A> add(FrequencyEntry<A> entry) {
        return add(entry.getWeight(), entry.getGenerate());
    }

    static <A> FrequencyMapBuilder<A> frequencyMapBuilder() {
        return frequencyMapBuilder0();
    }
}
