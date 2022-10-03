package software.kes.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Weighted;

final class IncompleteFrequencyMap<A> implements FrequencyMap<A> {
    private static final IncompleteFrequencyMap<?> INSTANCE = new IncompleteFrequencyMap<>();

    @SuppressWarnings("unchecked")
    static <A> IncompleteFrequencyMap<A> incompleteFrequencyMap() {
        return (IncompleteFrequencyMap<A>) INSTANCE;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Generator<A> toGenerator() {
        throw new IllegalStateException("Cannot build Generator from this FrequencyMap: no positive weights provided");
    }

    @Override
    public FrequencyMap<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        if (weightedGenerator.getWeight() < 1) {
            return this;
        } else {
            return FrequencyMap1.frequencyMap1(weightedGenerator);
        }
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        if (other.isEmpty()) {
            return this;
        } else {
            return other;
        }
    }

    @Override
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        return new IncompleteFrequencyMap<>();
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        FrequencyMap1.checkMultiplier(positiveFactor);
        return this;
    }
}
