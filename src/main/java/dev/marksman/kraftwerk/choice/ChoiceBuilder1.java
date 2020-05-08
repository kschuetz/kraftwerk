package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder2.choiceBuilder2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder1<A> implements ToGenerator<A> {
    private final FrequencyMap<A> frequencyMap;

    public static <A> ChoiceBuilder1<A> choiceBuilder(Weighted<? extends Generator<? extends A>> firstChoice) {
        return new ChoiceBuilder1<>(FrequencyMap.frequencyMap(firstChoice));
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(Generator<? extends A> firstChoice) {
        return choiceBuilder(firstChoice.weighted());
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(Weighted<? extends A> firstChoice) {
        return new ChoiceBuilder1<>(FrequencyMap.frequencyMapValue(firstChoice));
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(A firstChoice) {
        return choiceBuilderValue(Weighted.weighted(firstChoice));
    }

    @Override
    public Generator<A> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <B> ChoiceBuilder2<A, B> or(Weighted<? extends Generator<? extends B>> weightedGenerator) {
        FrequencyMap<Choice2<A, B>> newFrequencyMap = frequencyMap
                .<Choice2<A, B>>fmap(Choice2::a)
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice2::b)));
        return choiceBuilder2(newFrequencyMap);
    }

    public <B> ChoiceBuilder2<A, B> or(Generator<B> gen) {
        return or(gen.weighted());
    }

    public <B> ChoiceBuilder2<A, B> orValue(Weighted<? extends B> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    public <B> ChoiceBuilder2<A, B> orValue(B value) {
        return or(constant(value).weighted());
    }
}
