package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice8;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

/**
 * A builder to facilitate the construction of {@link Generator}s that yield {@link Choice8} values.
 * <p>
 * Call {@link ChoiceBuilder8#toGenerator} to build the final {@link Generator}.
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
 * @param <H> the type of the eighth choice
 */
public final class ChoiceBuilder8<A, B, C, D, E, F, G, H> implements ToGenerator<Choice8<A, B, C, D, E, F, G, H>> {
    private final FrequencyMap<Choice8<A, B, C, D, E, F, G, H>> frequencyMap;

    private ChoiceBuilder8(FrequencyMap<Choice8<A, B, C, D, E, F, G, H>> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    /**
     * Builds the final {@link Generator}.
     */
    @Override
    public Generator<Choice8<A, B, C, D, E, F, G, H>> toGenerator() {
        return frequencyMap.toGenerator();
    }

    static <A, B, C, D, E, F, G, H> ChoiceBuilder8<A, B, C, D, E, F, G, H> choiceBuilder8(FrequencyMap<Choice8<A, B, C, D, E, F, G, H>> frequencyMap) {
        return new ChoiceBuilder8<>(frequencyMap);
    }
}
