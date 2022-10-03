package software.kes.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Generators;
import software.kes.kraftwerk.ToGenerator;
import software.kes.kraftwerk.Weighted;
import software.kes.kraftwerk.frequency.FrequencyMap;

import static software.kes.kraftwerk.Generators.constant;
import static software.kes.kraftwerk.choice.ChoiceBuilder6.choiceBuilder6;

/**
 * A builder to facilitate the construction of {@link Generator}s that yield {@link Choice5} values.
 * <p>
 * Use one of the {@link ChoiceBuilder5#or} methods to add more choices, and call {@link ChoiceBuilder5#toGenerator} to
 * build the final {@link Generator}.
 * <p>
 * All instances of {@code ChoiceBuilder} are immutable and can be reused, even after calling {@code toGenerator}.
 *
 * @param <A> the type of the first choice
 * @param <B> the type of the second choice
 * @param <C> the type of the third choice
 * @param <D> the type of the fourth choice
 * @param <E> the type of the fifth choice
 */
public final class ChoiceBuilder5<A, B, C, D, E> implements ToGenerator<Choice5<A, B, C, D, E>> {
    private final FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap;

    public ChoiceBuilder5(FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    static <A, B, C, D, E> ChoiceBuilder5<A, B, C, D, E> choiceBuilder5(FrequencyMap<Choice5<A, B, C, D, E>> frequencyMap) {
        return new ChoiceBuilder5<>(frequencyMap);
    }

    /**
     * Builds the final {@link Generator}.
     */
    @Override
    public Generator<Choice5<A, B, C, D, E>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    /**
     * Adds another choice.
     *
     * @param weightedGenerator a weighted {@code Generator} for the next choice
     * @param <F>               the type of the next choice
     * @return a {@code ChoiceBuilder6<A, B, C, D, E, F>}
     */
    public <F> ChoiceBuilder6<A, B, C, D, E, F> or(Weighted<? extends Generator<? extends F>> weightedGenerator) {
        FrequencyMap<Choice6<A, B, C, D, E, F>> newFrequencyMap = frequencyMap
                .<Choice6<A, B, C, D, E, F>>fmap(c5 ->
                        c5.match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, Choice6::e))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice6::f)));
        return choiceBuilder6(newFrequencyMap);
    }

    /**
     * Adds another choice.
     *
     * @param gen a  {@code Generator} for the next choice
     * @param <F> the type of the next choice
     * @return a {@code ChoiceBuilder6<A, B, C, D, E, F>}
     */
    public <F> ChoiceBuilder6<A, B, C, D, E, F> or(Generator<F> gen) {
        return or(gen.weighted());
    }

    /**
     * Adds another choice.
     *
     * @param weightedValue a weighted value for the next choice
     * @param <F>           the type of the next choice
     * @return a {@code ChoiceBuilder6<A, B, C, D, E, F>}
     */
    public <F> ChoiceBuilder6<A, B, C, D, E, F> orValue(Weighted<? extends F> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    /**
     * Adds another choice.
     *
     * @param value a value for the next choice
     * @param <F>   the type of the next choice
     * @return a {@code ChoiceBuilder6<A, B, C, D, E, F>}
     */
    public <F> ChoiceBuilder6<A, B, C, D, E, F> orValue(F value) {
        return or(constant(value).weighted());
    }
}
