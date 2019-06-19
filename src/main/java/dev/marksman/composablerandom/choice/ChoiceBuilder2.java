package dev.marksman.composablerandom.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.ToGenerate;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Generate.constant;
import static dev.marksman.composablerandom.choice.ChoiceBuilder3.choiceBuilder3;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceBuilder2<A, B> implements ToGenerate<Choice2<A, B>> {
    private final FrequencyMap<Choice2<A, B>> frequencyMap;

    @Override
    public Generate<Choice2<A, B>> toGenerate() {
        return frequencyMap.toGenerate();
    }

    public <C> ChoiceBuilder3<A, B, C> or(int weight, Generate<C> gen) {
        FrequencyMap<Choice3<A, B, C>> newFrequencyMap = frequencyMap
                .<Choice3<A, B, C>>fmap(c2 ->
                        c2.match(Choice3::a, Choice3::b))
                .add(weight, gen.fmap(Choice3::c));
        return choiceBuilder3(newFrequencyMap);
    }

    public <C> ChoiceBuilder3<A, B, C> or(Generate<C> gen) {
        return or(1, gen);
    }

    public <C> ChoiceBuilder3<A, B, C> or(FrequencyEntry<C> frequencyEntry) {
        return or(frequencyEntry.getWeight(), frequencyEntry.getGenerate());
    }

    public <C> ChoiceBuilder3<A, B, C> orValue(int weight, C value) {
        return or(weight, constant(value));
    }

    public <C> ChoiceBuilder3<A, B, C> orValue(C value) {
        return or(1, constant(value));
    }

    public static <A, B> ChoiceBuilder2<A, B> choiceBuilder2(FrequencyMap<Choice2<A, B>> frequencyMap) {
        return new ChoiceBuilder2<>(frequencyMap);
    }
}
