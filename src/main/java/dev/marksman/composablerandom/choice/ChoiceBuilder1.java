package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.ToGenerate;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Generate.constant;
import static dev.marksman.composablerandom.choice.ChoiceBuilder2.choiceBuilder2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder1<A> implements ToGenerate<A> {
    private final FrequencyMap<A> frequencyMap;

    @Override
    public Generate<A> toGenerate() {
        return frequencyMap.toGenerate();
    }

    public <B> ChoiceBuilder2<A, B> or(int weight, Generate<B> gen) {
        FrequencyMap<Choice2<A, B>> newFrequencyMap = frequencyMap
                .<Choice2<A, B>>fmap(Choice2::a)
                .add(weight, gen.fmap(Choice2::b));
        return choiceBuilder2(newFrequencyMap);
    }

    public <B> ChoiceBuilder2<A, B> or(Generate<B> gen) {
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

    public static <A> ChoiceBuilder1<A> choiceBuilder(int weight, Generate<A> firstChoice) {
        return new ChoiceBuilder1<A>(FrequencyMap.frequencyMap(weight, firstChoice));
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(Generate<A> firstChoice) {
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
