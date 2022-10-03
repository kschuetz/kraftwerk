package software.kes.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Generators;
import software.kes.kraftwerk.ToGenerator;
import software.kes.kraftwerk.Weighted;
import software.kes.kraftwerk.frequency.FrequencyMap;

import static software.kes.kraftwerk.Generators.constant;

/**
 * A builder to facilitate the construction of {@link Generator}s that yield {@link Choice4} values.
 * <p>
 * Use one of the {@link ChoiceBuilder4#or} methods to add more choices, and call {@link ChoiceBuilder4#toGenerator} to
 * build the final {@link Generator}.
 * <p>
 * All instances of {@code ChoiceBuilder} are immutable and can be reused, even after calling {@code toGenerator}.
 *
 * @param <A> the type of the first choice
 * @param <B> the type of the second choice
 * @param <C> the type of the third choice
 * @param <D> the type of the fourth choice
 */
public final class ChoiceBuilder4<A, B, C, D> implements ToGenerator<Choice4<A, B, C, D>> {
    private final FrequencyMap<Choice4<A, B, C, D>> frequencyMap;

    private ChoiceBuilder4(FrequencyMap<Choice4<A, B, C, D>> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    static <A, B, C, D> ChoiceBuilder4<A, B, C, D> choiceBuilder4(FrequencyMap<Choice4<A, B, C, D>> frequencyMap) {
        return new ChoiceBuilder4<>(frequencyMap);
    }

    /**
     * Builds the final {@link Generator}.
     */
    @Override
    public Generator<Choice4<A, B, C, D>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    /**
     * Adds another choice.
     *
     * @param weightedGenerator a weighted {@code Generator} for the next choice
     * @param <E>               the type of the next choice
     * @return a {@code ChoiceBuilder5<A, B, C, D, E>}
     */
    public <E> ChoiceBuilder5<A, B, C, D, E> or(Weighted<? extends Generator<? extends E>> weightedGenerator) {
        FrequencyMap<Choice5<A, B, C, D, E>> newFrequencyMap = frequencyMap
                .<Choice5<A, B, C, D, E>>fmap(c4 ->
                        c4.match(Choice5::a, Choice5::b, Choice5::c, Choice5::d))
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice5::e)));
        return ChoiceBuilder5.choiceBuilder5(newFrequencyMap);
    }

    /**
     * Adds another choice.
     *
     * @param gen a  {@code Generator} for the next choice
     * @param <E> the type of the next choice
     * @return a {@code ChoiceBuilder5<A, B, C, D, E>}
     */
    public <E> ChoiceBuilder5<A, B, C, D, E> or(Generator<E> gen) {
        return or(gen.weighted());
    }

    /**
     * Adds another choice.
     *
     * @param weightedValue a weighted value for the next choice
     * @param <E>           the type of the next choice
     * @return a {@code ChoiceBuilder5<A, B, C, D, E>}
     */
    public <E> ChoiceBuilder5<A, B, C, D, E> orValue(Weighted<? extends E> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    /**
     * Adds another choice.
     *
     * @param value a value for the next choice
     * @param <E>   the type of the next choice
     * @return a {@code ChoiceBuilder5<A, B, C, D, E>}
     */
    public <E> ChoiceBuilder5<A, B, C, D, E> orValue(E value) {
        return or(constant(value).weighted());
    }
}
