package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder4.choiceBuilder4;

public final class ChoiceBuilder3<A, B, C> implements ToGenerator<Choice3<A, B, C>> {
    private final FrequencyMap<Choice3<A, B, C>> frequencyMap;

    private ChoiceBuilder3(FrequencyMap<Choice3<A, B, C>> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    public static <A, B, C> ChoiceBuilder3<A, B, C> choiceBuilder3(FrequencyMap<Choice3<A, B, C>> frequencyMap) {
        return new ChoiceBuilder3<>(frequencyMap);
    }

    @Override
    public Generator<Choice3<A, B, C>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <D> ChoiceBuilder4<A, B, C, D> or(Weighted<? extends Generator<? extends D>> weightedGenerator) {
        FrequencyMap<Choice4<A, B, C, D>> newFrequencyMap = frequencyMap
                .<Choice4<A, B, C, D>>fmap(c3 ->
                        c3.match(Choice4::a, Choice4::b, Choice4::c))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice4::d)));
        return choiceBuilder4(newFrequencyMap);
    }

    public <D> ChoiceBuilder4<A, B, C, D> or(Generator<D> gen) {
        return or(gen.weighted());
    }

    public <D> ChoiceBuilder4<A, B, C, D> orValue(Weighted<? extends D> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    public <D> ChoiceBuilder4<A, B, C, D> orValue(D value) {
        return or(constant(value).weighted());
    }
}
