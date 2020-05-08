package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder6.choiceBuilder6;

@AllArgsConstructor
public class ChoiceBuilder5<A, B, C, D, E> implements ToGenerator<Choice5<A, B, C, D, E>> {
    private final FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap;

    public static <A, B, C, D, E> ChoiceBuilder5<A, B, C, D, E> choiceBuilder5(FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap) {
        return new ChoiceBuilder5<>(frequencyMap);
    }

    @Override
    public Generator<Choice5<A, B, C, D, E>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <F> ChoiceBuilder6<A, B, C, D, E, F> or(Weighted<? extends Generator<? extends F>> weightedGenerator) {
        FrequencyMap<Choice6<A, B, C, D, E, F>> newFrequencyMap = frequencyMap
                .<Choice6<A, B, C, D, E, F>>fmap(c5 ->
                        c5.match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, Choice6::e))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice6::f)));
        return choiceBuilder6(newFrequencyMap);
    }

    public <F> ChoiceBuilder6<A, B, C, D, E, F> or(Generator<F> gen) {
        return or(gen.weighted());
    }

    public <F> ChoiceBuilder6<A, B, C, D, E, F> orValue(Weighted<? extends F> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    public <F> ChoiceBuilder6<A, B, C, D, E, F> orValue(F value) {
        return or(constant(value).weighted());
    }
}
