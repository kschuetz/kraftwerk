package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.choice.ChoiceBuilder6.choiceBuilder6;

@AllArgsConstructor
public class ChoiceBuilder5<A, B, C, D, E> implements ToGenerator<Choice5<A, B, C, D, E>> {
    private final FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap;

    @Override
    public Generator<Choice5<A, B, C, D, E>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <F> ChoiceBuilder6<A, B, C, D, E, F> or(int weight, Generator<F> gen) {
        FrequencyMap<Choice6<A, B, C, D, E, F>> newFrequencyMap = frequencyMap
                .<Choice6<A, B, C, D, E, F>>fmap(c5 ->
                        c5.match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, Choice6::e))
                .add(weight, gen.fmap(Choice6::f));
        return choiceBuilder6(newFrequencyMap);
    }

    public <F> ChoiceBuilder6<A, B, C, D, E, F> or(Generator<F> gen) {
        return or(1, gen);
    }

    public <F> ChoiceBuilder6<A, B, C, D, E, F> or(FrequencyEntry<F> frequencyEntry) {
        return or(frequencyEntry.getWeight(), frequencyEntry.getGenerate());
    }

    public <F> ChoiceBuilder6<A, B, C, D, E, F> orValue(int weight, F value) {
        return or(weight, constant(value));
    }

    public <F> ChoiceBuilder6<A, B, C, D, E, F> orValue(F value) {
        return or(1, constant(value));
    }

    public static <A, B, C, D, E> ChoiceBuilder5<A, B, C, D, E> choiceBuilder5(FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap) {
        return new ChoiceBuilder5<>(frequencyMap);
    }
}
