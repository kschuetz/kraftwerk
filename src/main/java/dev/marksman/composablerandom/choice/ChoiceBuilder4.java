package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.choice.ChoiceBuilder5.choiceBuilder5;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder4<A, B, C, D> implements ToGenerator<Choice4<A, B, C, D>> {
    private final FrequencyMap<Choice4<A, B, C, D>> frequencyMap;

    @Override
    public Generator<Choice4<A, B, C, D>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <E> ChoiceBuilder5<A, B, C, D, E> or(int weight, Generator<E> generator) {
        FrequencyMap<Choice5<A, B, C, D, E>> newFrequencyMap = frequencyMap
                .<Choice5<A, B, C, D, E>>fmap(c4 ->
                        c4.match(Choice5::a, Choice5::b, Choice5::c, Choice5::d))
                .add(weight, generator.fmap(Choice5::e));
        return choiceBuilder5(newFrequencyMap);
    }

    public <E> ChoiceBuilder5<A, B, C, D, E> or(Generator<E> generator) {
        return or(1, generator);
    }

    public <E> ChoiceBuilder5<A, B, C, D, E> or(FrequencyEntry<E> frequencyEntry) {
        return or(frequencyEntry.getWeight(), frequencyEntry.getGenerator());
    }

    public <E> ChoiceBuilder5<A, B, C, D, E> orValue(int weight, E value) {
        return or(weight, constant(value));
    }

    public <E> ChoiceBuilder5<A, B, C, D, E> orValue(E value) {
        return or(1, constant(value));
    }

    public static <A, B, C, D> ChoiceBuilder4<A, B, C, D> choiceBuilder4(FrequencyMap<Choice4<A, B, C, D>> frequencyMap) {
        return new ChoiceBuilder4<>(frequencyMap);
    }
}
