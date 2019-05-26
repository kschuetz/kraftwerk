package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.ToGenerator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.choice.ChoiceBuilder2.choiceBuilder2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder1<A> implements ToGenerator<A> {
    private final FrequencyMap<A> frequencyMap;

    @Override
    public Generator<A> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <B> ChoiceBuilder2<A, B> or(int weight, Generator<B> generator) {
        FrequencyMap<Choice2<A, B>> newFrequencyMap = frequencyMap
                .<Choice2<A, B>>fmap(Choice2::a)
                .add(weight, generator.fmap(Choice2::b));
        return choiceBuilder2(newFrequencyMap);
    }

    public <B> ChoiceBuilder2<A, B> or(Generator<B> generator) {
        return or(1, generator);
    }

    public <B> ChoiceBuilder2<A, B> or(FrequencyEntry<B> frequencyEntry) {
        return or(frequencyEntry.getWeight(), frequencyEntry.getGenerator());
    }

    public <B> ChoiceBuilder2<A, B> orValue(int weight, B value) {
        return or(weight, constant(value));
    }

    public <B> ChoiceBuilder2<A, B> orValue(B value) {
        return or(1, constant(value));
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(int weight, Generator<A> firstChoice) {
        return new ChoiceBuilder1<A>(FrequencyMap.frequencyMap(weight, firstChoice));
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(Generator<A> firstChoice) {
        return choiceBuilder(1, firstChoice);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(FrequencyEntry<A> firstChoice) {
        return choiceBuilder(firstChoice.getWeight(), firstChoice.getGenerator());
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(int weight, A firstChoice) {
        return new ChoiceBuilder1<>(FrequencyMap.frequencyMap(weight, firstChoice));
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(A firstChoice) {
        return choiceBuilderValue(1, firstChoice);
    }
}
