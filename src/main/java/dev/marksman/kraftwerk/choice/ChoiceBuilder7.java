package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder8.choiceBuilder8;

/**
 * A builder to facilitate the construction of {@link Generator}s that yield {@link Choice7} values.
 * <p>
 * Use one of the {@link ChoiceBuilder7#or} methods to add more choices, and call {@link ChoiceBuilder7#toGenerator} to
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
 * @param <G> the type of the seventh choice
 */
public final class ChoiceBuilder7<A, B, C, D, E, F, G> implements ToGenerator<Choice7<A, B, C, D, E, F, G>> {
    private final FrequencyMap<Choice7<A, B, C, D, E, F, G>> frequencyMap;

    private ChoiceBuilder7(FrequencyMap<Choice7<A, B, C, D, E, F, G>> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    static <A, B, C, D, E, F, G> ChoiceBuilder7<A, B, C, D, E, F, G> choiceBuilder7(FrequencyMap<Choice7<A, B, C, D, E, F, G>> frequencyMap) {
        return new ChoiceBuilder7<>(frequencyMap);
    }

    /**
     * Builds the final {@link Generator}.
     */
    @Override
    public Generator<Choice7<A, B, C, D, E, F, G>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    /**
     * Adds another choice.
     *
     * @param weightedGenerator a weighted {@code Generator} for the next choice
     * @param <H>               the type of the next choice
     * @return a {@code ChoiceBuilder8<A, B, C, D, E, F, G, H>}
     */
    public <H> ChoiceBuilder8<A, B, C, D, E, F, G, H> or(Weighted<? extends Generator<? extends H>> weightedGenerator) {
        FrequencyMap<Choice8<A, B, C, D, E, F, G, H>> newFrequencyMap = frequencyMap
                .<Choice8<A, B, C, D, E, F, G, H>>fmap(c7 ->
                        c7.match(Choice8::a, Choice8::b, Choice8::c, Choice8::d,
                                Choice8::e, Choice8::f, Choice8::g))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice8::h)));
        return choiceBuilder8(newFrequencyMap);
    }

    /**
     * Adds another choice.
     *
     * @param gen    a  {@code Generator} for the next choice
     * @param <H>the type of the next choice
     * @return a {@code ChoiceBuilder8<A, B, C, D, E, F, G, H>}
     */
    public <H> ChoiceBuilder8<A, B, C, D, E, F, G, H> or(Generator<H> gen) {
        return or(gen.weighted());
    }

    /**
     * Adds another choice.
     *
     * @param weightedValue a weighted value for the next choice
     * @param <H>           the type of the next choice
     * @return a {@code ChoiceBuilder8<A, B, C, D, E, F, G, H>}
     */
    public <H> ChoiceBuilder8<A, B, C, D, E, F, G, H> orValue(Weighted<? extends H> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    /**
     * Adds another choice.
     *
     * @param value a value for the next choice
     * @param <H>   the type of the next choice
     * @return a {@code ChoiceBuilder8<A, B, C, D, E, F, G, H>}
     */
    public <H> ChoiceBuilder8<A, B, C, D, E, F, G, H> orValue(H value) {
        return or(constant(value).weighted());
    }
}
