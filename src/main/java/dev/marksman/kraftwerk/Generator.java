package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.monad.Monad;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.weights.MaybeWeights;
import dev.marksman.kraftwerk.weights.NullWeights;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.kraftwerk.Generators.generateArrayList;
import static dev.marksman.kraftwerk.Generators.generateArrayListOfSize;
import static dev.marksman.kraftwerk.Generators.generateJust;
import static dev.marksman.kraftwerk.Generators.generateMaybe;
import static dev.marksman.kraftwerk.Generators.generateNonEmptyArrayList;
import static dev.marksman.kraftwerk.Generators.generateNonEmptyVector;
import static dev.marksman.kraftwerk.Generators.generateNonEmptyVectorOfSize;
import static dev.marksman.kraftwerk.Generators.generateProduct;
import static dev.marksman.kraftwerk.Generators.generateTuple;
import static dev.marksman.kraftwerk.Generators.generateVector;
import static dev.marksman.kraftwerk.Generators.generateVectorOfSize;
import static dev.marksman.kraftwerk.Generators.generateWithNulls;
import static dev.marksman.kraftwerk.StandardGeneratorParameters.defaultGeneratorParameters;
import static dev.marksman.kraftwerk.ValueSupply.valueSupply;

/**
 * A strategy for generating random values of type {@code A}.
 * <p>
 * To use a {@code Generator}, call one of its {@code run} methods ({@link Generator#run()}, {@link Generator#run(Seed)},
 * {@link Generator#run(GeneratorParameters)}, or {@link Generator#run(GeneratorParameters, Seed)}).  These will
 * give you a {@link ValueSupply}, which is an infinite {@link Iterable} with some additional convenience methods.
 * <p>
 * Several built-in {@code Generator}s are provided in {@link Generators}.  These can be used as is or composed with others
 * to build {@code Generator}s that yield more complex types.
 * <p>
 * All {@code Generator}s are immutable and contain no state. Any method that appears to mutate a {@code Generator} actually
 * creates a new {@code Generator}, while leaving the original unchanged.
 * <p>
 * Since a {@code Generator} is a {@link Functor}, its output can be mapped using {@link Generator#fmap}.
 * It is a {@link Monad} as well, so it can be composed with other {@code Generator}s using {@link Generator#flatMap}.
 * {@code Generator}s, if properly designed, obey the functor and monad laws.
 * <p>
 * {@code Generator}s can <i>not</i>, however, be filtered. A {@code Generator} provides the guarantee that when invoked
 * for any {@link Seed}, it will eventually yield a value. Filtering would remove that guarantee. If you need to filter
 * the values you are interested in, either change the design of your generator to only produce those values, or filter the
 * resulting {@code ValueSupply} instead.
 *
 * @param <A> the output type
 */
public interface Generator<A> extends Monad<A, Generator<?>>, ToGenerator<A> {
    /**
     * Creates a {@link GenerateFn} using the given {@code GeneratorParameters}.
     * This is the only method that a {@code Generator} needs to implement.
     * Since this is a low level method, users may wish to call once of the
     * higher level {@code run} methods instead.
     * <p>
     * Implementers must follow these rules:
     * - createGenerateFn must be referentially transparent; it must yield the equivalent {@code GenerateFn} for all calls for a given {@link Seed}
     * - The {@code GenerateFn} must pure and referentially transparent
     * - The {@code GenerateFn} must be guaranteed to yield a value
     * - The {@code GenerateFn} must not mutate the input {@code Seed}; it must return a new {@code Seed}
     *
     * @param generatorParameters the {@code GeneratorParameters}
     * @return a {@code GenerateFn<A>}
     * @see Generator#run()
     * @see Generator#run(Seed)
     * @see Generator#run(GeneratorParameters)
     * @see Generator#run(GeneratorParameters, Seed)
     */
    GenerateFn<A> createGenerateFn(GeneratorParameters generatorParameters);

    /**
     * Creates a {@link ValueSupply} from custom {@code GeneratorParameters} and a specific initial {@link Seed}.
     *
     * @param generatorParameters the {@code GeneratorParameters}
     * @param initialSeed         the initial {@code Seed}
     * @return a {@code ValueSupply<A>}
     * @see Generator#run()
     * @see Generator#run(Seed)
     * @see Generator#run(GeneratorParameters)
     */
    default ValueSupply<A> run(GeneratorParameters generatorParameters, Seed initialSeed) {
        return valueSupply(createGenerateFn(generatorParameters), initialSeed);
    }

    /**
     * Creates a {@link ValueSupply} from a specific initial {@link Seed}, using the built-in default {@code GeneratorParameters}.
     *
     * @param initialSeed the initial {@code Seed}
     * @return a {@code ValueSupply<A>}
     * @see Generator#run()
     * @see Generator#run(GeneratorParameters)
     * @see Generator#run(GeneratorParameters, Seed)
     */
    default ValueSupply<A> run(Seed initialSeed) {
        return run(defaultGeneratorParameters(), initialSeed);
    }

    /**
     * Creates a {@link ValueSupply} from custom {@code GeneratorParameters}, and a random initial seed.
     * Calling this will result in a different {@code ValueSupply} each time.
     *
     * @return a {@code ValueSupply<A>}
     * @see Generator#run()
     * @see Generator#run(Seed)
     * @see Generator#run(GeneratorParameters, Seed)
     */
    default ValueSupply<A> run(GeneratorParameters generatorParameters) {
        return run(generatorParameters, Seed.random());
    }

    /**
     * Creates a {@link ValueSupply} from a random initial seed, using the built-in default {@code GeneratorParameters}.
     * Calling this will result in a different {@code ValueSupply} each time.
     *
     * @return a {@code ValueSupply<A>}
     * @see Generator#run(Seed)
     * @see Generator#run(GeneratorParameters)
     * @see Generator#run(GeneratorParameters, Seed)
     */
    default ValueSupply<A> run() {
        return run(defaultGeneratorParameters(), Seed.random());
    }

    /**
     * Creates a new {@code Generator} by mapping the output of this {@code Generator}.
     *
     * @param fn  the mapping function
     * @param <B> the new output type
     * @return a {@code Generator<B>}
     */
    @Override
    default <B> Generator<B> fmap(Fn1<? super A, ? extends B> fn) {
        return Mapping.mapped(fn, this);
    }

    /**
     * Creates a new {@code Generator} that, when invoked, feeds the output of this {@code Generator} to a function that
     * returns another {@code Generator}, and invokes that.
     *
     * @param f   the mapping function
     * @param <B> the new output type
     * @return a {@code Generator<B>}
     */
    @SuppressWarnings("unchecked")
    @Override
    default <B> Generator<B> flatMap(Fn1<? super A, ? extends Monad<B, Generator<?>>> f) {
        return Composition.flatMapped((Fn1<? super A, ? extends Generator<B>>) f, this);
    }

    /**
     * The {@code pure} method to satisfy the {@link Monad} interface.
     *
     * @see Generators#constant
     */
    @Override
    default <B> Generator<B> pure(B b) {
        return Constant.constant(b);
    }

    @Override
    default Generator<A> toGenerator() {
        return this;
    }

    /**
     * Returns an optional label used in diagnostics.
     *
     * @return {@code a Maybe<String>}
     * @see Generator#labeled(String)
     */
    default Maybe<String> getLabel() {
        return nothing();
    }

    /**
     * Returns the application-specific opaque object associated with this {@code Generator}, if any.
     *
     * @return {@code a Maybe<Object>}
     * @see Generator#attachApplicationData(Object)
     */
    default Maybe<Object> getApplicationData() {
        return nothing();
    }

    /**
     * Creates a new {@code Generator} that is the same as this one, with the label changed to the one provided.
     *
     * @param label the label
     * @return a {@code Generator<A>}
     * @see Generator#getLabel()
     */
    default Generator<A> labeled(String label) {
        return Meta.withMetadata(Maybe.maybe(label), this.getApplicationData(), this);
    }

    /**
     * Creates a new {@code Generator} that is the same as this one, with the attached application changed to the object provided.
     *
     * @param applicationData an opaque application-defined object of any type to be used as metadata
     * @return a {@code Generator<A>}
     * @see Generator#getApplicationData()
     */
    default Generator<A> attachApplicationData(Object applicationData) {
        return Meta.withMetadata(getLabel(), Maybe.maybe(applicationData), this);
    }

    /**
     * Creates a new {@code Generator} that yields pairs of values generated by this one.
     *
     * @return a {@code Generator<Tuple2<A, A>>}
     */
    default Generator<Tuple2<A, A>> pair() {
        return generateTuple(this, this);
    }

    /**
     * Creates a new {@code Generator} that yields triples of values generated by this one.
     *
     * @return a {@code Generator<Tuple3<A, A, A>>}
     */
    default Generator<Tuple3<A, A, A>> triple() {
        return generateTuple(this, this, this);
    }

    /**
     * Creates a {@link Weighted} instance of this {@code Generator} with a weight of 1.
     *
     * @return a {@code Weighted<Generator<A>>}
     */
    default Weighted<Generator<A>> weighted() {
        return Weighted.weighted(1, this);
    }

    /**
     * Creates a {@link Weighted} instance of this {@code Generator} with a custom weight.
     *
     * @param weight the weight value; must be &gt;= 0
     * @return a {@code Weighted<Generator<A>>}
     */
    default Weighted<Generator<A>> weighted(int weight) {
        return Weighted.weighted(weight, this);
    }

    /**
     * Converts this {@code Generator<A>} into a {@code Generator<Maybe<A>>} that always yields {@code just}.
     *
     * @return a {@code Generator<Maybe<A>>}
     */
    default Generator<Maybe<A>> just() {
        return generateJust(this);
    }

    /**
     * Converts this {@code Generator<A>} into a {@code Generator<Maybe<A>>} that usually yields {@code just},
     * but occasionally yields {@code nothing}.
     *
     * @return a {@code Generator<Maybe<A>>}
     */
    default Generator<Maybe<A>> maybe() {
        return generateMaybe(this);
    }

    /**
     * Converts this {@code Generator<A>} into a {@code Generator<Maybe<A>>} with custom probabilities for
     * {@code just} vs. {@code nothing}.
     *
     * @return a {@code Generator<Maybe<A>>}
     */
    default Generator<Maybe<A>> maybe(MaybeWeights weights) {
        return generateMaybe(weights, this);
    }

    /**
     * Converts this {@code Generator<A>} into a {@code Generator<Either<A, R>>} that always yields {@code left}.
     *
     * @return a {@code Generator<Either<A, R>>}
     */
    default <R> Generator<Either<A, R>> left() {
        return CoProducts.generateLeft(this);
    }

    /**
     * Converts this {@code Generator<A>} into a {@code Generator<Either<L, A>>} that always yields {@code right}.
     *
     * @return a {@code Generator<Either<L, A>>}
     */
    default <L> Generator<Either<L, A>> right() {
        return CoProducts.generateRight(this);
    }

    /**
     * Creates a new {@code Generator} that is the same as this one, but occasionally yields {@code null}.
     *
     * @return a {@code Generator<Maybe<A>>}
     */
    default Generator<A> withNulls() {
        return generateWithNulls(this);
    }

    /**
     * Creates a new {@code Generator} that is the same as this one, but occasionally yields {@code null},
     * with a custom probability.
     *
     * @return a {@code Generator<Maybe<A>>}
     */
    default Generator<A> withNulls(NullWeights weights) {
        return generateWithNulls(weights, this);
    }

    /**
     * Creates a new {@code Generator} that yields {@link ArrayList}s of various sizes, with this {@code Generator}
     * generating the elements.
     *
     * @return a {@code Generator<ArrayList<A>>}
     */
    default Generator<ArrayList<A>> arrayList() {
        return generateArrayList(this);
    }

    /**
     * Creates a new {@code Generator} that yields non-empty {@link ArrayList}s of various sizes, with this {@code Generator}
     * generating the elements.
     *
     * @return a {@code Generator<ArrayList<A>>}
     */
    default Generator<ArrayList<A>> nonEmptyArrayList() {
        return generateNonEmptyArrayList(this);
    }

    /**
     * Creates a new {@code Generator} that yields {@link ArrayList}s of a specific size, with this {@code Generator}
     * generating the elements.
     *
     * @param size the size of the {@code ArrayList}s generated; must be &gt;=0
     * @return a {@code Generator<ArrayList<A>>}
     */
    default Generator<ArrayList<A>> arrayListOfSize(int size) {
        return generateArrayListOfSize(size, this);
    }

    /**
     * Creates a new {@code Generator} that yields {@link ArrayList}s of various sizes within a specific range,
     * with this {@code Generator} generating the elements.
     *
     * @param sizeRange the {@link IntRange} of the sizes of {@code ArrayList}s generated
     * @return a {@code Generator<ArrayList<A>>}
     */
    default Generator<ArrayList<A>> arrayListOfSize(IntRange sizeRange) {
        return generateArrayListOfSize(sizeRange, this);
    }

    /**
     * Creates a new {@code Generator} that yields {@link dev.marksman.collectionviews.Vector}s of various sizes,
     * with this {@code Generator} generating the elements.
     *
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    default Generator<ImmutableVector<A>> vector() {
        return generateVector(this);
    }

    /**
     * Creates a new {@code Generator} that yields {@link dev.marksman.collectionviews.Vector}s of a specific size,
     * with this {@code Generator} generating the elements.
     *
     * @param size the size of the {@code Vector}s generated; must be &gt;=0
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    default Generator<ImmutableVector<A>> vectorOfSize(int size) {
        return generateVectorOfSize(size, this);
    }

    /**
     * Creates a new {@code Generator} that yields {@link dev.marksman.collectionviews.Vector}s of various sizes within
     * a specific range, with this {@code Generator} generating the elements.
     *
     * @param sizeRange the {@link IntRange} of the sizes of {@code Vector}s generated
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    default Generator<ImmutableVector<A>> vectorOfSize(IntRange sizeRange) {
        return generateVectorOfSize(sizeRange, this);
    }

    /**
     * Creates a new {@code Generator} that yields {@link dev.marksman.collectionviews.NonEmptyVector}s of various sizes,
     * with this {@code Generator} generating the elements.
     *
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    default Generator<ImmutableNonEmptyVector<A>> nonEmptyVector() {
        return generateNonEmptyVector(this);
    }

    /**
     * Creates a new {@code Generator} that yields {@link dev.marksman.collectionviews.NonEmptyVector}s of a specific size,
     * with this {@code Generator} generating the elements.
     *
     * @param size the size of the {@code NonEmptyVector}s generated; must be &gt;=1
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    default Generator<ImmutableNonEmptyVector<A>> nonEmptyVectorOfSize(int size) {
        return generateNonEmptyVectorOfSize(size, this);
    }

    /**
     * Creates a new {@code Generator} that yields {@link dev.marksman.collectionviews.NonEmptyVector}s of various sizes
     * within a specific range, with this {@code Generator} generating the elements.
     *
     * @param sizeRange the {@link IntRange} of the sizes of {@code NonEmptyVector}s generated
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    default Generator<ImmutableNonEmptyVector<A>> nonEmptyVectorOfSize(IntRange sizeRange) {
        return generateNonEmptyVectorOfSize(sizeRange, this);
    }

    /**
     * Creates a new {@code Generator} that combines the output of another {@code Generator} using a function
     * to yield the final output.
     *
     * @param fn    a function that takes the output of this {@code Generator} and the output of {@code other},
     *              and returns the final output
     * @param other the other {@code Generator}
     * @param <B>   the output type of the other {@code Generator}
     * @param <C>   the new output type
     * @return a {@code Generator<C>}
     */
    default <B, C> Generator<C> zipWith(Fn2<A, B, C> fn, Generator<B> other) {
        return generateProduct(this, other, fn);
    }

    /**
     * Creates a new {@code Generator} with special values mixed into the output of this one.
     * Special values will be represented more frequently than non-special values.
     *
     * @param values the special values to mix in
     * @return a {@code Generator<A>}
     */
    default Generator<A> injectSpecialValues(NonEmptyFiniteIterable<A> values) {
        return Bias.injectsSpecialValues(values, this);
    }

    /**
     * Creates a new {@code Generator} with a special value mixed into the output of this one.
     * Special values will be represented more frequently than non-special values.
     *
     * @param specialValue the special value
     * @return a {@code Generator<A>}
     */
    default Generator<A> injectSpecialValue(A specialValue) {
        return Bias.injectsSpecialValue(specialValue, this);
    }

}
