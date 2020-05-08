package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder5.choiceBuilder5;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder4<A, B, C, D> implements ToGenerator<Choice4<A, B, C, D>> {
    private final FrequencyMap<Choice4<A, B, C, D>> frequencyMap;

    public static <A, B, C, D> ChoiceBuilder4<A, B, C, D> choiceBuilder4(FrequencyMap<Choice4<A, B, C, D>> frequencyMap) {
        return new ChoiceBuilder4<>(frequencyMap);
    }

    @Override
    public Generator<Choice4<A, B, C, D>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <E> ChoiceBuilder5<A, B, C, D, E> or(Weighted<? extends Generator<? extends E>> weightedGenerator) {
        FrequencyMap<Choice5<A, B, C, D, E>> newFrequencyMap = frequencyMap
                .<Choice5<A, B, C, D, E>>fmap(c4 ->
                        c4.match(Choice5::a, Choice5::b, Choice5::c, Choice5::d))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice5::e)));
        return choiceBuilder5(newFrequencyMap);
    }

    public <E> ChoiceBuilder5<A, B, C, D, E> or(Generator<E> gen) {
        return or(gen.weighted());
    }

    public <E> ChoiceBuilder5<A, B, C, D, E> orValue(Weighted<? extends E> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    public <E> ChoiceBuilder5<A, B, C, D, E> orValue(E value) {
        return or(constant(value).weighted());
    }
}
