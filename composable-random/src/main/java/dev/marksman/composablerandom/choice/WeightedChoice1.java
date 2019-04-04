package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.choice.WeightedChoice2.weightedChoice2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeightedChoice1<A> implements ToGenerator<A> {
    private final FrequencyMap<A> frequencyMap;

    @Override
    public Generator<A> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <B> WeightedChoice2<A, B> or(int weight, Generator<B> generator) {
        FrequencyMap<Choice2<A, B>> newFrequencyMap = frequencyMap
                .<Choice2<A, B>>fmap(Choice2::a)
                .add(weight, generator.fmap(Choice2::b));
        return weightedChoice2(newFrequencyMap);
    }

    public <B> WeightedChoice2<A, B> or(Generator<B> generator) {
        return or(1, generator);
    }

    public <B> WeightedChoice2<A, B> or(int weight, B value) {
        return or(weight, constant(value));
    }

    public <B> WeightedChoice2<A, B> or(B value) {
        return or(1, constant(value));
    }

    public static <A> WeightedChoice1<A> choiceBuilder(int weight, Generator<A> firstChoice) {
        return new WeightedChoice1<A>(FrequencyMap.frequencyMap(weight, firstChoice));
    }

    public static <A> WeightedChoice1<A> choiceBuilder(Generator<A> firstChoice) {
        return choiceBuilder(1, firstChoice);
    }

    public static <A> WeightedChoice1<A> choiceBuilder(int weight, A firstChoice) {
        return new WeightedChoice1<>(FrequencyMap.frequencyMap(weight, firstChoice));
    }

    public static <A> WeightedChoice1<A> choiceBuilder(A firstChoice) {
        return choiceBuilder(1, firstChoice);
    }
}
