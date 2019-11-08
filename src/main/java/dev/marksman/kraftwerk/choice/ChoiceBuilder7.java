package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import dev.marksman.kraftwerk.FrequencyEntry;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder8.choiceBuilder8;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder7<A, B, C, D, E, F, G> implements ToGenerator<Choice7<A, B, C, D, E, F, G>> {
    private final FrequencyMap<Choice7<A, B, C, D, E, F, G>> frequencyMap;

    @Override
    public Generator<Choice7<A, B, C, D, E, F, G>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <H> ChoiceBuilder8<A, B, C, D, E, F, G, H> or(int weight, Generator<H> gen) {
        FrequencyMap<Choice8<A, B, C, D, E, F, G, H>> newFrequencyMap = frequencyMap
                .<Choice8<A, B, C, D, E, F, G, H>>fmap(c7 ->
                        c7.match(Choice8::a, Choice8::b, Choice8::c, Choice8::d,
                                Choice8::e, Choice8::f, Choice8::g))
                .add(weight, gen.fmap(Choice8::h));
        return choiceBuilder8(newFrequencyMap);
    }

    public <H> ChoiceBuilder8<A, B, C, D, E, F, G, H> or(Generator<H> gen) {
        return or(1, gen);
    }

    public <H> ChoiceBuilder8<A, B, C, D, E, F, G, H> or(FrequencyEntry<H> frequencyEntry) {
        return or(frequencyEntry.getWeight(), frequencyEntry.getGenerate());
    }

    public <H> ChoiceBuilder8<A, B, C, D, E, F, G, H> orValue(int weight, H value) {
        return or(weight, constant(value));
    }

    public <H> ChoiceBuilder8<A, B, C, D, E, F, G, H> orValue(H value) {
        return or(1, constant(value));
    }

    public static <A, B, C, D, E, F, G> ChoiceBuilder7<A, B, C, D, E, F, G> choiceBuilder7(FrequencyMap<Choice7<A, B, C, D, E, F, G>> frequencyMap) {
        return new ChoiceBuilder7<>(frequencyMap);
    }
}
