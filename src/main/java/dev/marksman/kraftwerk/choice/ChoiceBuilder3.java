package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import dev.marksman.kraftwerk.FrequencyEntry;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder4.choiceBuilder4;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder3<A, B, C> implements ToGenerator<Choice3<A, B, C>> {
    private final FrequencyMap<Choice3<A, B, C>> frequencyMap;

    @Override
    public Generator<Choice3<A, B, C>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <D> ChoiceBuilder4<A, B, C, D> or(int weight, Generator<D> gen) {
        FrequencyMap<Choice4<A, B, C, D>> newFrequencyMap = frequencyMap
                .<Choice4<A, B, C, D>>fmap(c3 ->
                        c3.match(Choice4::a, Choice4::b, Choice4::c))
                .add(weight, gen.fmap(Choice4::d));
        return choiceBuilder4(newFrequencyMap);
    }

    public <D> ChoiceBuilder4<A, B, C, D> or(Generator<D> gen) {
        return or(1, gen);
    }

    public <D> ChoiceBuilder4<A, B, C, D> or(FrequencyEntry<D> frequencyEntry) {
        return or(frequencyEntry.getWeight(), frequencyEntry.getGenerate());
    }

    public <D> ChoiceBuilder4<A, B, C, D> orValue(int weight, D value) {
        return or(weight, constant(value));
    }

    public <D> ChoiceBuilder4<A, B, C, D> orValue(D value) {
        return or(1, constant(value));
    }

    public static <A, B, C> ChoiceBuilder3<A, B, C> choiceBuilder3(FrequencyMap<Choice3<A, B, C>> frequencyMap) {
        return new ChoiceBuilder3<>(frequencyMap);
    }
}