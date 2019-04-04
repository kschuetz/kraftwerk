package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.choice.WeightedChoice8.weightedChoice8;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeightedChoice7<A, B, C, D, E, F, G> implements ToGenerator<Choice7<A, B, C, D, E, F, G>> {
    private final FrequencyMap<Choice7<A, B, C, D, E, F, G>> frequencyMap;

    @Override
    public Generator<Choice7<A, B, C, D, E, F, G>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <H> WeightedChoice8<A, B, C, D, E, F, G, H> or(int weight, Generator<H> generator) {
        FrequencyMap<Choice8<A, B, C, D, E, F, G, H>> newFrequencyMap = frequencyMap
                .<Choice8<A, B, C, D, E, F, G, H>>fmap(c7 ->
                        c7.match(Choice8::a, Choice8::b, Choice8::c, Choice8::d,
                                Choice8::e, Choice8::f, Choice8::g))
                .multiply(7)
                .add(7 * weight, generator.fmap(Choice8::h));
        return weightedChoice8(newFrequencyMap);
    }

    public static <A, B, C, D, E, F, G> WeightedChoice7<A, B, C, D, E, F, G> weightedChoice7(FrequencyMap<Choice7<A, B, C, D, E, F, G>> frequencyMap) {
        return new WeightedChoice7<>(frequencyMap);
    }
}
