package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMapBuilderN<A> implements FrequencyMapBuilder<A> {
    private final FrequencyMap<A> result;

    @Override
    public FrequencyMapBuilder<A> add(int weight, Generate<? extends A> gen) {
        if (weight > 0) {
            return frequencyMapBuilderN(result.add(weight, gen));
        } else {
            return this;
        }
    }

    @Override
    public FrequencyMapBuilder<A> combine(FrequencyMap<A> other) {
        return frequencyMapBuilderN(result.combine(other));
    }

    @Override
    public FrequencyMap<A> build() {
        return result;
    }

    @Override
    public <B> FrequencyMapBuilder<B> fmap(Fn1<? super A, ? extends B> fn) {
        return frequencyMapBuilderN(result.fmap(fn));
    }

    @Override
    public FrequencyMapBuilder<A> multiply(int positiveFactor) {
        return frequencyMapBuilderN(result.multiply(positiveFactor));
    }

    @SuppressWarnings("unchecked")
    static <A> FrequencyMapBuilderN<A> frequencyMapBuilderN(FrequencyMap<? extends A> frequencyMap) {
        return new FrequencyMapBuilderN<>((FrequencyMap<A>) frequencyMap);
    }
}
