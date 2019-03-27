package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.adt.choice.*;
import dev.marksman.composablerandom.Generator;
import lombok.AllArgsConstructor;

public class WeightedChoice {

    @AllArgsConstructor
    public static class WeightedChoice2<A, B> {
        private final FrequencyMap<Choice2<A, B>> frequencyMap;

        public <C> WeightedChoice3<A, B, C> or(int weight, Generator<C> generator) {
            FrequencyMap<Choice3<A, B, C>> newFrequencyMap = frequencyMap
                    .<Choice3<A, B, C>>fmap(c2 ->
                            c2.match(Choice3::a, Choice3::b))
                    .multiply(2)
                    .add(2, generator.fmap(Choice3::c));
            return new WeightedChoice3<>(newFrequencyMap);
        }
    }

    @AllArgsConstructor
    public static class WeightedChoice3<A, B, C> {
        private final FrequencyMap<Choice3<A, B, C>> frequencyMap;

        public <D> WeightedChoice4<A, B, C, D> or(int weight, Generator<D> generator) {
            FrequencyMap<Choice4<A, B, C, D>> newFrequencyMap = frequencyMap
                    .<Choice4<A, B, C, D>>fmap(c3 ->
                            c3.match(Choice4::a, Choice4::b, Choice4::c))
                    .multiply(3)
                    .add(3, generator.fmap(Choice4::d));
            return new WeightedChoice4<>(newFrequencyMap);
        }
    }

    @AllArgsConstructor
    public static class WeightedChoice4<A, B, C, D> {
        private final FrequencyMap<Choice4<A, B, C, D>> frequencyMap;

        public <E> WeightedChoice5<A, B, C, D, E> or(int weight, Generator<E> generator) {
            FrequencyMap<Choice5<A, B, C, D, E>> newFrequencyMap = frequencyMap
                    .<Choice5<A, B, C, D, E>>fmap(c4 ->
                            c4.match(Choice5::a, Choice5::b, Choice5::c, Choice5::d))
                    .multiply(4)
                    .add(3, generator.fmap(Choice5::e));
            return new WeightedChoice5<>(newFrequencyMap);
        }
    }

    @AllArgsConstructor
    public static class WeightedChoice5<A, B, C, D, E> {
        private final FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap;

        public <F> WeightedChoice6<A, B, C, D, E, F> or(int weight, Generator<F> generator) {
            return null;
        }
    }

    @AllArgsConstructor
    public static class WeightedChoice6<A, B, C, D, E, F> {
        private final FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap;

        public <G> WeightedChoice7<A, B, C, D, E, F, G> or(int weight, Generator<G> generator) {
            return null;
        }
    }

    @AllArgsConstructor
    public static class WeightedChoice7<A, B, C, D, E, F, G> {
        private final FrequencyMap<Choice7<A, B, C, D, E, F, G>> frequencyMap;

        public <H> WeightedChoice8<A, B, C, D, E, F, G, H> or(int weight, Generator<H> generator) {
            return null;
        }
    }

    @AllArgsConstructor
    public static class WeightedChoice8<A, B, C, D, E, F, G, H> {
        private final FrequencyMap<Choice8<A, B, C, D, E, F, G, H>> frequencyMap;
    }

}