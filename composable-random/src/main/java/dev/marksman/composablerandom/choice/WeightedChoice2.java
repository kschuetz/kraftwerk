package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.choice.WeightedChoice3.weightedChoice3;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeightedChoice2<A, B> implements ToGenerator<Choice2<A, B>> {
    private final FrequencyMap<Choice2<A, B>> frequencyMap;

    @Override
    public Generator<Choice2<A, B>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <C> WeightedChoice3<A, B, C> or(int weight, Generator<C> generator) {
        FrequencyMap<Choice3<A, B, C>> newFrequencyMap = frequencyMap
                .<Choice3<A, B, C>>fmap(c2 ->
                        c2.match(Choice3::a, Choice3::b))
                .multiply(2)
                .add(2 * weight, generator.fmap(Choice3::c));
        return weightedChoice3(newFrequencyMap);
    }

    public static <A, B> WeightedChoice2<A, B> weightedChoice2(FrequencyMap<Choice2<A, B>> frequencyMap) {
        return new WeightedChoice2<>(frequencyMap);
    }
}
