package dev.marksman.kraftwerk.choice;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.choice.ChoiceBuilder2.choiceBuilder2;

/**
 * A builder to facilitate the construction of {@link Generator}s that yield {@code ChoiceN} values.
 * <p>
 * Use one of the {@link ChoiceBuilder1#or} methods to add more choices, and call {@link ChoiceBuilder1#toGenerator} to
 * build the final {@link Generator}.
 * <p>
 * All instances of {@code ChoiceBuilder} are immutable and can be reused, even after calling {@code toGenerator}.
 *
 * @param <A> the type of the first choice
 */
public final class ChoiceBuilder1<A> implements ToGenerator<A> {
    private final FrequencyMap<A> frequencyMap;

    private ChoiceBuilder1(FrequencyMap<A> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    /**
     * Creates a {@code ChoiceBuilder1}.
     *
     * @param firstChoice a weighted {@code Generator} for the first choice
     * @param <A>         the type of the first choice
     * @return a {@code ChoiceBuilder1<A>}
     */
    public static <A> ChoiceBuilder1<A> choiceBuilder(Weighted<? extends Generator<? extends A>> firstChoice) {
        return new ChoiceBuilder1<>(FrequencyMap.frequencyMap(firstChoice));
    }

    /**
     * Creates a {@code ChoiceBuilder1}.
     *
     * @param firstChoice a {@code Generator} for the first choice
     * @param <A>         the type of the first choice
     * @return a {@code ChoiceBuilder1<A>}
     */
    public static <A> ChoiceBuilder1<A> choiceBuilder(Generator<? extends A> firstChoice) {
        return choiceBuilder(firstChoice.weighted());
    }

    /**
     * Creates a {@code ChoiceBuilder1}.
     *
     * @param firstChoice a weighted value for the first choice
     * @param <A>         the type of the first choice
     * @return a {@code ChoiceBuilder1<A>}
     */
    public static <A> ChoiceBuilder1<A> choiceBuilderValue(Weighted<? extends A> firstChoice) {
        return new ChoiceBuilder1<>(FrequencyMap.frequencyMapFirstValue(firstChoice));
    }

    /**
     * Creates a {@code ChoiceBuilder1}.
     *
     * @param firstChoice a value for the first choice
     * @param <A>         the type of the first choice
     * @return a {@code ChoiceBuilder1<A>}
     */
    public static <A> ChoiceBuilder1<A> choiceBuilderValue(A firstChoice) {
        return choiceBuilderValue(Weighted.weighted(firstChoice));
    }

    @Override
    public Generator<A> toGenerator() {
        return frequencyMap.toGenerator();
    }

    /**
     * Adds another choice.
     *
     * @param weightedGenerator a weighted {@code Generator} for the next choice
     * @param <B>               the type of the next choice
     * @return a {@code ChoiceBuilder2<A, B>}
     */
    public <B> ChoiceBuilder2<A, B> or(Weighted<? extends Generator<? extends B>> weightedGenerator) {
        FrequencyMap<Choice2<A, B>> newFrequencyMap = frequencyMap
                .<Choice2<A, B>>fmap(Choice2::a)
                .add(weightedGenerator.fmap(gen -> gen.fmap(Choice2::b)));
        return choiceBuilder2(newFrequencyMap);
    }

    /**
     * Adds another choice.
     *
     * @param gen a  {@code Generator} for the next choice
     * @param <B> the type of the next choice
     * @return a {@code ChoiceBuilder2<A, B>}
     */
    public <B> ChoiceBuilder2<A, B> or(Generator<B> gen) {
        return or(gen.weighted());
    }

    /**
     * Adds another choice.
     *
     * @param weightedValue a weighted value for the next choice
     * @param <B>           the type of the next choice
     * @return a {@code ChoiceBuilder2<A, B>}
     */
    public <B> ChoiceBuilder2<A, B> orValue(Weighted<? extends B> weightedValue) {
        return or(weightedValue.fmap(Generators::constant));
    }

    /**
     * Adds another choice.
     *
     * @param value a weighted value for the next choice
     * @param <B>   the type of the next choice
     * @return a {@code ChoiceBuilder2<A, B>}
     */
    public <B> ChoiceBuilder2<A, B> orValue(B value) {
        return or(constant(value).weighted());
    }
}
