package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder4.choiceBuilder4;

/**
 * A builder to facilitate the construction of {@link Generator}s that yield {@link Choice3} values.
 * <p>
 * Use one of the {@link ChoiceBuilder3#or} methods to add more choices, and call {@link ChoiceBuilder3#toGenerator} to
 * build the final {@link Generator}.
 * <p>
 * All instances of {@code ChoiceBuilder} are immutable and can be reused, even after calling {@code toGenerator}.
 *
 * @param <A> the type of the first choice
 * @param <B> the type of the second choice
 * @param <C> the type of the third choice
 */
public final class ChoiceBuilder3<A, B, C> implements ToGenerator<Choice3<A, B, C>> {
    private final FrequencyMap<Choice3<A, B, C>> frequencyMap;

    private ChoiceBuilder3(FrequencyMap<Choice3<A, B, C>> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    static <A, B, C> ChoiceBuilder3<A, B, C> choiceBuilder3(FrequencyMap<Choice3<A, B, C>> frequencyMap) {
        return new ChoiceBuilder3<>(frequencyMap);
    }

    /**
     * Builds the final {@link Generator}.
     */
    @Override
    public Generator<Choice3<A, B, C>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    /**
     * Adds another choice.
     *
     * @param weightedGenerator a weighted {@code Generator} for the next choice
     * @param <D>               the type of the next choice
     * @return a {@code ChoiceBuilder4<A, B, C, D>}
     */
    public <D> ChoiceBuilder4<A, B, C, D> or(Weighted<? extends Generator<? extends D>> weightedGenerator) {
        FrequencyMap<Choice4<A, B, C, D>> newFrequencyMap = frequencyMap
                .<Choice4<A, B, C, D>>fmap(c3 ->
                        c3.match(Choice4::a, Choice4::b, Choice4::c))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice4::d)));
        return choiceBuilder4(newFrequencyMap);
    }

    /**
     * Adds another choice.
     *
     * @param gen a  {@code Generator} for the next choice
     * @param <D> the type of the next choice
     * @return a {@code ChoiceBuilder4<A, B, C, D>}
     */
    public <D> ChoiceBuilder4<A, B, C, D> or(Generator<D> gen) {
        return or(gen.weighted());
    }

    /**
     * Adds another choice.
     *
     * @param weightedValue a weighted value for the next choice
     * @param <D>           the type of the next choice
     * @return a {@code ChoiceBuilder4<A, B, C, D>}
     */
    public <D> ChoiceBuilder4<A, B, C, D> orValue(Weighted<? extends D> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    /**
     * Adds another choice.
     *
     * @param value a value for the next choice
     * @param <D>   the type of the next choice
     * @return a {@code ChoiceBuilder4<A, B, C, D>}
     */
    public <D> ChoiceBuilder4<A, B, C, D> orValue(D value) {
        return or(constant(value).weighted());
    }
}
