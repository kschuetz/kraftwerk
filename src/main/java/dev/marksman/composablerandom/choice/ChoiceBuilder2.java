package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.choice.ChoiceBuilder3.choiceBuilder3;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder2<A, B> implements ToGenerator<Choice2<A, B>> {
    private final FrequencyMap<Choice2<A, B>> frequencyMap;

    @Override
    public Generator<Choice2<A, B>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <C> ChoiceBuilder3<A, B, C> or(int weight, Generator<C> generator) {
        FrequencyMap<Choice3<A, B, C>> newFrequencyMap = frequencyMap
                .<Choice3<A, B, C>>fmap(c2 ->
                        c2.match(Choice3::a, Choice3::b))
                .add(weight, generator.fmap(Choice3::c));
        return choiceBuilder3(newFrequencyMap);
    }

    public <C> ChoiceBuilder3<A, B, C> or(Generator<C> generator) {
        return or(1, generator);
    }

    public <C> ChoiceBuilder3<A, B, C> or(FrequencyEntry<C> frequencyEntry) {
        return or(frequencyEntry.getWeight(), frequencyEntry.getGenerator());
    }

    public <C> ChoiceBuilder3<A, B, C> orValue(int weight, C value) {
        return or(weight, constant(value));
    }

    public <C> ChoiceBuilder3<A, B, C> orValue(C value) {
        return or(1, constant(value));
    }

    public static <A, B> ChoiceBuilder2<A, B> choiceBuilder2(FrequencyMap<Choice2<A, B>> frequencyMap) {
        return new ChoiceBuilder2<>(frequencyMap);
    }
}
