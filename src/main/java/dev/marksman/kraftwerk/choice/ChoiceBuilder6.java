package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import dev.marksman.kraftwerk.FrequencyEntry;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Generator.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder7.choiceBuilder7;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder6<A, B, C, D, E, F> implements ToGenerator<Choice6<A, B, C, D, E, F>> {
    private final FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap;

    @Override
    public Generator<Choice6<A, B, C, D, E, F>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> or(int weight, Generator<G> gen) {
        FrequencyMap<Choice7<A, B, C, D, E, F, G>> newFrequencyMap = frequencyMap
                .<Choice7<A, B, C, D, E, F, G>>fmap(c6 ->
                        c6.match(Choice7::a, Choice7::b, Choice7::c, Choice7::d, Choice7::e, Choice7::f))
                .add(weight, gen.fmap(Choice7::g));
        return choiceBuilder7(newFrequencyMap);
    }

    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> or(Generator<G> gen) {
        return or(1, gen);
    }

    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> or(FrequencyEntry<G> frequencyEntry) {
        return or(frequencyEntry.getWeight(), frequencyEntry.getGenerate());
    }

    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> orValue(int weight, G value) {
        return or(weight, constant(value));
    }

    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> orValue(G value) {
        return or(1, constant(value));
    }

    public static <A, B, C, D, E, F> ChoiceBuilder6<A, B, C, D, E, F> choiceBuilder6(FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap) {
        return new ChoiceBuilder6<>(frequencyMap);
    }
}
