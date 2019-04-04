package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.choice.WeightedChoice6.weightedChoice6;

@AllArgsConstructor
public class WeightedChoice5<A, B, C, D, E> implements ToGenerator<Choice5<A, B, C, D, E>> {
    private final FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap;

    @Override
    public Generator<Choice5<A, B, C, D, E>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <F> WeightedChoice6<A, B, C, D, E, F> or(int weight, Generator<F> generator) {
        FrequencyMap<Choice6<A, B, C, D, E, F>> newFrequencyMap = frequencyMap
                .<Choice6<A, B, C, D, E, F>>fmap(c5 ->
                        c5.match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, Choice6::e))
                .multiply(5)
                .add(5 * weight, generator.fmap(Choice6::f));
        return weightedChoice6(newFrequencyMap);
    }

    public static <A, B, C, D, E> WeightedChoice5<A, B, C, D, E> weightedChoice5(FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap) {
        return new WeightedChoice5<>(frequencyMap);
    }
}
