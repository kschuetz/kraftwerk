package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder3.choiceBuilder3;

/**
 * A builder to facilitate the construction of {@link Generator}s that yield {@link Choice2} values.
 * <p>
 * Use one of the {@link ChoiceBuilder2#or} methods to add more choices, and call {@link ChoiceBuilder2#toGenerator} to
 * build the final {@link Generator}.
 * <p>
 * All instances of {@code ChoiceBuilder} are immutable and can be reused, even after calling {@code toGenerator}.
 *
 * @param <A> the type of the first choice
 * @param <B> the type of the second choice
 */
public final class ChoiceBuilder2<A, B> implements ToGenerator<Choice2<A, B>> {
    private final FrequencyMap<Choice2<A, B>> frequencyMap;

    private ChoiceBuilder2(FrequencyMap<Choice2<A, B>> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    static <A, B> ChoiceBuilder2<A, B> choiceBuilder2(FrequencyMap<Choice2<A, B>> frequencyMap) {
        return new ChoiceBuilder2<>(frequencyMap);
    }

    /**
     * Builds the final {@link Generator}.
     */
    @Override
    public Generator<Choice2<A, B>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    /**
     * Adds another choice.
     *
     * @param weightedGenerator a weighted {@code Generator} for the next choice
     * @param <C>               the type of the next choice
     * @return a {@code ChoiceBuilder3<A, B, C>}
     */
    public <C> ChoiceBuilder3<A, B, C> or(Weighted<? extends Generator<? extends C>> weightedGenerator) {
        FrequencyMap<Choice3<A, B, C>> newFrequencyMap = frequencyMap
                .<Choice3<A, B, C>>fmap(c2 ->
                        c2.match(Choice3::a, Choice3::b))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice3::c)));
        return choiceBuilder3(newFrequencyMap);
    }

    /**
     * Adds another choice.
     *
     * @param gen a  {@code Generator} for the next choice
     * @param <C> the type of the next choice
     * @return a {@code ChoiceBuilder3<A, B, C>}
     */
    public <C> ChoiceBuilder3<A, B, C> or(Generator<C> gen) {
        return or(gen.weighted());
    }

    /**
     * Adds another choice.
     *
     * @param weightedValue a weighted value for the next choice
     * @param <C>           the type of the next choice
     * @return a {@code ChoiceBuilder3<A, B, C>}
     */
    public <C> ChoiceBuilder3<A, B, C> orValue(Weighted<? extends C> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    /**
     * Adds another choice.
     *
     * @param value a weighted value for the next choice
     * @param <C>   the type of the next choice
     * @return a {@code ChoiceBuilder3<A, B, C>}
     */
    public <C> ChoiceBuilder3<A, B, C> orValue(C value) {
        return or(constant(value).weighted());
    }
}
