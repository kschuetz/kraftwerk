package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.choice.WeightedChoice7.weightedChoice7;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeightedChoice6<A, B, C, D, E, F> implements ToGenerator<Choice6<A, B, C, D, E, F>> {
    private final FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap;

    public <G> WeightedChoice7<A, B, C, D, E, F, G> or(int weight, Generator<G> generator) {
        FrequencyMap<Choice7<A, B, C, D, E, F, G>> newFrequencyMap = frequencyMap
                .<Choice7<A, B, C, D, E, F, G>>fmap(c6 ->
                        c6.match(Choice7::a, Choice7::b, Choice7::c, Choice7::d, Choice7::e, Choice7::f))
                .multiply(6)
                .add(6 * weight, generator.fmap(Choice7::g));
        return weightedChoice7(newFrequencyMap);
    }

    @Override
    public Generator<Choice6<A, B, C, D, E, F>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public static <A, B, C, D, E, F> WeightedChoice6<A, B, C, D, E, F> weightedChoice6(FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap) {
        return new WeightedChoice6<>(frequencyMap);
    }
}
