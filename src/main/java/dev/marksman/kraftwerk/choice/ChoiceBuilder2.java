package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder3.choiceBuilder3;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder2<A, B> implements ToGenerator<Choice2<A, B>> {
    private final FrequencyMap<Choice2<A, B>> frequencyMap;

    public static <A, B> ChoiceBuilder2<A, B> choiceBuilder2(FrequencyMap<Choice2<A, B>> frequencyMap) {
        return new ChoiceBuilder2<>(frequencyMap);
    }

    @Override
    public Generator<Choice2<A, B>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <C> ChoiceBuilder3<A, B, C> or(Weighted<? extends Generator<? extends C>> weightedGenerator) {
        FrequencyMap<Choice3<A, B, C>> newFrequencyMap = frequencyMap
                .<Choice3<A, B, C>>fmap(c2 ->
                        c2.match(Choice3::a, Choice3::b))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice3::c)));
        return choiceBuilder3(newFrequencyMap);
    }

    public <C> ChoiceBuilder3<A, B, C> or(Generator<C> gen) {
        return or(gen.weighted());
    }

    public <C> ChoiceBuilder3<A, B, C> orValue(Weighted<? extends C> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    public <C> ChoiceBuilder3<A, B, C> orValue(C value) {
        return or(constant(value).weighted());
    }
}
