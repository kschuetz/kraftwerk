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

/**
 * A builder to facilitate the construction of {@link Generator}s that yield {@link Choice6} values.
 * <p>
 * Use one of the {@link ChoiceBuilder6#or} methods to add more choices, and call {@link ChoiceBuilder6#toGenerator} to
 * build the final {@link Generator}.
 * <p>
 * All instances of {@code ChoiceBuilder} are immutable and can be reused, even after calling {@code toGenerator}.
 *
 * @param <A> the type of the first choice
 * @param <B> the type of the second choice
 * @param <C> the type of the third choice
 * @param <D> the type of the fourth choice
 * @param <E> the type of the fifth choice
 * @param <F> the type of the sixth choice
 */
public final class ChoiceBuilder6<A, B, C, D, E, F> implements ToGenerator<Choice6<A, B, C, D, E, F>> {
    private final FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap;

    private ChoiceBuilder6(FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    static <A, B, C, D, E, F> ChoiceBuilder6<A, B, C, D, E, F> choiceBuilder6(FrequencyMap<Choice6<A, B, C, D, E, F>> frequencyMap) {
        return new ChoiceBuilder6<>(frequencyMap);
    }

    /**
     * Builds the final {@link Generator}.
     */
    @Override
    public Generator<Choice6<A, B, C, D, E, F>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    /**
     * Adds another choice.
     *
     * @param weightedGenerator a weighted {@code Generator} for the next choice
     * @param <G>               the type of the next choice
     * @return a {@code ChoiceBuilder7<A, B, C, D, E, F, G>}
     */
    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> or(Weighted<? extends Generator<? extends G>> weightedGenerator) {
        FrequencyMap<Choice7<A, B, C, D, E, F, G>> newFrequencyMap = frequencyMap
                .<Choice7<A, B, C, D, E, F, G>>fmap(c6 ->
                        c6.match(Choice7::a, Choice7::b, Choice7::c, Choice7::d, Choice7::e, Choice7::f))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice7::g)));
        return choiceBuilder7(newFrequencyMap);
    }

    /**
     * Adds another choice.
     *
     * @param gen    a  {@code Generator} for the next choice
     * @param <G>the type of the next choice
     * @return a {@code ChoiceBuilder7<A, B, C, D, E, F, G>}
     */
    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> or(Generator<G> gen) {
        return or(gen.weighted());
    }

    /**
     * Adds another choice.
     *
     * @param weightedValue a weighted value for the next choice
     * @param <G>           the type of the next choice
     * @return a {@code ChoiceBuilder7<A, B, C, D, E, F, G>}
     */
    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> orValue(Weighted<? extends G> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    /**
     * Adds another choice.
     *
     * @param value a value for the next choice
     * @param <G>   the type of the next choice
     * @return a {@code ChoiceBuilder7<A, B, C, D, E, F, G>}
     */
    public <G> ChoiceBuilder7<A, B, C, D, E, F, G> orValue(G value) {
        return or(constant(value).weighted());
    }
}
