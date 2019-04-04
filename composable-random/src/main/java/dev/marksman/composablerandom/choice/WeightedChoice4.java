package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.choice.WeightedChoice5.weightedChoice5;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeightedChoice4<A, B, C, D> implements ToGenerator<Choice4<A, B, C, D>> {
    private final FrequencyMap<Choice4<A, B, C, D>> frequencyMap;

    @Override
    public Generator<Choice4<A, B, C, D>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <E> WeightedChoice5<A, B, C, D, E> or(int weight, Generator<E> generator) {
        FrequencyMap<Choice5<A, B, C, D, E>> newFrequencyMap = frequencyMap
                .<Choice5<A, B, C, D, E>>fmap(c4 ->
                        c4.match(Choice5::a, Choice5::b, Choice5::c, Choice5::d))
                .multiply(4)
                .add(4 * weight, generator.fmap(Choice5::e));
        return weightedChoice5(newFrequencyMap);
    }

    public static <A, B, C, D> WeightedChoice4<A, B, C, D> weightedChoice4(FrequencyMap<Choice4<A, B, C, D>> frequencyMap) {
        return new WeightedChoice4<>(frequencyMap);
    }
}
