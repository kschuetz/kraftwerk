package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.monad.Monad;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.weights.MaybeWeights;
import dev.marksman.kraftwerk.weights.NullWeights;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.kraftwerk.StandardGeneratorParameters.defaultGeneratorParameters;
import static dev.marksman.kraftwerk.ValueSupply.valueSupply;

public interface Generator<A> extends Monad<A, Generator<?>>, ToGenerator<A> {
    /**
     * Creates a {@link GenerateFn} using the given {@link GeneratorParameters}.
     * This is the only method that a {@code Generator} needs to implement.
     * Since this is a low level method, users may wish to call once of the
     * higher level {@code run} methods instead.
     * <p>
     * Implementers must follow these rules:
     * - createGenerateFn must be referentially transparent; it must yield the equivalent {@code GenerateFn} for all calls for a given {@link Seed}
     * - The {@code GenerateFn} must pure and referentially transparent
     * - The {@code GenerateFn} must be guaranteed to yield a value
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
     * Creates a {@link ValueSupply} from custom {@link GeneratorParameters} and a specific initial {@link Seed}.
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
     * Creates a {@link ValueSupply} from a specific initial {@link Seed}, using the built-in default {@link GeneratorParameters}.
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
     * Creates a {@link ValueSupply} from custom {@link GeneratorParameters}, and a random initial seed.
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
     * Creates a {@link ValueSupply} from a random initial seed, using the built-in default {@link GeneratorParameters}.
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
     * Creates a new {@link Generator} by mapping the output of this {@code Generator}.
     *
     * @param fn  the mapping function
     * @param <B> the new output type
     * @return a {@code Generator<B>}
     */
    @Override
    default <B> Generator<B> fmap(Fn1<? super A, ? extends B> fn) {
        return Mapping.mapped(fn, this);
    }

    // TODO: flatMap docs
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
     * Returns the application-specific opaque object associated with this {@link Generator}, if any.
     *
     * @return {@code a Maybe<Object>}
     * @see Generator#attachApplicationData(Object)
     */
    default Maybe<Object> getApplicationData() {
        return nothing();
    }

    /**
     * Creates a new {@link Generator} that is the same as this one, with the label changed to the one provided.
     *
     * @param label the label
     * @return a {@code Generator<A>}
     * @see Generator#getLabel()
     */
    default Generator<A> labeled(String label) {
        return Meta.withMetadata(Maybe.maybe(label), this.getApplicationData(), this);
    }

    /**
     * Creates a new {@link Generator} that is the same as this one, with the attached application changed to the object provided.
     *
     * @param applicationData an opaque application-defined object of any type to be used as metadata
     * @return a {@code Generator<A>}
     * @see Generator#getApplicationData()
     */
    default Generator<A> attachApplicationData(Object applicationData) {
        return Meta.withMetadata(getLabel(), Maybe.maybe(applicationData), this);
    }

    /**
     * Creates a new {@link Generator} that yields pairs of values generated by this one.
     *
     * @return a {@code Generator<Tuple2<A, A>>}
     */
    default Generator<Tuple2<A, A>> pair() {
        return Generators.tupled(this, this);
    }

    /**
     * Creates a new {@link Generator} that yields triples of values generated by this one.
     *
     * @return a {@code Generator<Tuple3<A, A, A>>}
     */
    default Generator<Tuple3<A, A, A>> triple() {
        return Generators.tupled(this, this, this);
    }

    /**
     * Creates a {@link Weighted} instance of this {@link Generator} with a weight of 1.
     *
     * @return a {@code Weighted<Generator<A>>}
     */
    default Weighted<Generator<A>> weighted() {
        return Weighted.weighted(1, this);
    }

    /**
     * Creates a {@link Weighted} instance of this {@link Generator} with a custom weight.
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
        return Generators.generateJust(this);
    }

    /**
     * Converts this {@code Generator<A>} into a {@code Generator<Maybe<A>>} that usually yields {@code just},
     * but occasionally yields {@code nothing}.
     *
     * @return a {@code Generator<Maybe<A>>}
     */
    default Generator<Maybe<A>> maybe() {
        return Generators.generateMaybe(this);
    }

    /**
     * Converts this {@code Generator<A>} into a {@code Generator<Maybe<A>>} with custom probabilities for
     * {@code just} vs. {@code nothing}.
     *
     * @return a {@code Generator<Maybe<A>>}
     */
    default Generator<Maybe<A>> maybe(MaybeWeights weights) {
        return Generators.generateMaybe(weights, this);
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
        return Generators.generateWithNulls(this);
    }

    default Generator<A> withNulls(NullWeights weights) {
        return Generators.generateWithNulls(weights, this);
    }

    default Generator<ArrayList<A>> arrayList() {
        return Generators.generateArrayList(this);
    }

    default Generator<ArrayList<A>> nonEmptyArrayList() {
        return Generators.generateNonEmptyArrayList(this);
    }

    default Generator<ArrayList<A>> arrayListOfSize(int size) {
        return Generators.generateArrayListOfSize(size, this);
    }

    default Generator<ArrayList<A>> arrayListOfSize(IntRange sizeRange) {
        return Generators.generateArrayListOfSize(sizeRange, this);
    }

    default Generator<ImmutableVector<A>> vector() {
        return Generators.generateVector(this);
    }

    default Generator<ImmutableVector<A>> vectorOfSize(int size) {
        return Generators.generateVectorOfSize(size, this);
    }

    default Generator<ImmutableVector<A>> vectorOfSize(IntRange sizeRange) {
        return Generators.generateVectorOfSize(sizeRange, this);
    }

    default Generator<ImmutableNonEmptyVector<A>> nonEmptyVector() {
        return Generators.generateNonEmptyVector(this);
    }

    default Generator<ImmutableNonEmptyVector<A>> nonEmptyVectorOfSize(int size) {
        return Generators.generateNonEmptyVectorOfSize(size, this);
    }

    default Generator<ImmutableNonEmptyVector<A>> nonEmptyVectorOfSize(IntRange sizeRange) {
        return Generators.generateNonEmptyVectorOfSize(sizeRange, this);
    }

    default <B, C> Generator<C> zipWith(Fn2<A, B, C> fn, Generator<B> other) {
        return Generators.product(this, other, fn);
    }

    // **********
    // mixing in edge cases

    default Generator<A> injectSpecialValues(NonEmptyFiniteIterable<A> values) {
        return Bias.injectsSpecialValues(values, this);
    }

    default Generator<A> injectSpecialValue(A specialValue) {
        return Bias.injectsSpecialValue(specialValue, this);
    }

}
