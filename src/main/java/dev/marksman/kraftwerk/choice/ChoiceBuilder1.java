package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.kraftwerk.FrequencyEntry;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Generator.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder2.choiceBuilder2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder1<A> implements ToGenerator<A> {
    private final FrequencyMap<A> frequencyMap;

    @Override
    public Generator<A> toGenerator() {
        return frequencyMap.toGenerator();
    }

    public <B> ChoiceBuilder2<A, B> or(int weight, Generator<B> gen) {
        FrequencyMap<Choice2<A, B>> newFrequencyMap = frequencyMap
                .<Choice2<A, B>>fmap(Choice2::a)
                .add(weight, gen.fmap(Choice2::b));
        return choiceBuilder2(newFrequencyMap);
    }

    public <B> ChoiceBuilder2<A, B> or(Generator<B> gen) {
        return or(1, gen);
    }

    public <B> ChoiceBuilder2<A, B> or(FrequencyEntry<B> frequencyEntry) {
        return or(frequencyEntry.getWeight(), frequencyEntry.getGenerate());
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
        return choiceBuilder(firstChoice.getWeight(), firstChoice.getGenerate());
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(int weight, A firstChoice) {
        return new ChoiceBuilder1<>(FrequencyMap.frequencyMap(weight, firstChoice));
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(A firstChoice) {
        return choiceBuilderValue(1, firstChoice);
    }
}
