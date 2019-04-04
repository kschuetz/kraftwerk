package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.choice.WeightedChoice4.weightedChoice4;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeightedChoice3<A, B, C> implements ToGenerator<Choice3<A, B, C>> {
    private final FrequencyMap<Choice3<A, B, C>> frequencyMap;

    @Override
    public Generator<Choice3<A, B, C>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <D> WeightedChoice4<A, B, C, D> or(int weight, Generator<D> generator) {
        FrequencyMap<Choice4<A, B, C, D>> newFrequencyMap = frequencyMap
                .<Choice4<A, B, C, D>>fmap(c3 ->
                        c3.match(Choice4::a, Choice4::b, Choice4::c))
                .multiply(3)
                .add(3 * weight, generator.fmap(Choice4::d));
        return weightedChoice4(newFrequencyMap);
    }

    public static <A, B, C> WeightedChoice3<A, B, C> weightedChoice3(FrequencyMap<Choice3<A, B, C>> frequencyMap) {
        return new WeightedChoice3<>(frequencyMap);
    }
}
