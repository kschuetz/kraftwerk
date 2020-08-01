package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder7.choiceBuilder7;

public final class ChoiceBuilder6<A, B, C, D, E, F> implements ToGenerator<Choice6<A, B, C, D, E, F>> {
    private final FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap;

    private ChoiceBuilder6(FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    public static <A, B, C, D, E, F> ChoiceBuilder6<A, B, C, D, E, F> choiceBuilder6(FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap) {
        return new ChoiceBuilder6<>(frequencyMap);
    }

    @Override
    public Generator<Choice6<A, B, C, D, E, F>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> or(Weighted<? extends Generator<? extends G>> weightedGenerator) {
        FrequencyMap<Choice7<A, B, C, D, E, F, G>> newFrequencyMap = frequencyMap
                .<Choice7<A, B, C, D, E, F, G>>fmap(c6 ->
                        c6.match(Choice7::a, Choice7::b, Choice7::c, Choice7::d, Choice7::e, Choice7::f))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice7::g)));
        return choiceBuilder7(newFrequencyMap);
    }

    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> or(Generator<G> gen) {
        return or(gen.weighted());
    }

    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> orValue(Weighted<? extends G> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> orValue(G value) {
        return or(constant(value).weighted());
    }
}
