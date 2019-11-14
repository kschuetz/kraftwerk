package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.Monad;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;

public interface Generator<A> extends Monad<A, Generator<?>>, ToGenerator<A> {
    Generate<A> prepare(Parameters parameters);

    @Override
    default <B> Generator<B> fmap(Fn1<? super A, ? extends B> fn) {
        return Mapping.mapped(fn, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    default <B> Generator<B> flatMap(Fn1<? super A, ? extends Monad<B, Generator<?>>> f) {
        return Composition.flatMapped((Fn1<? super A, ? extends Generator<B>>) f, this);
    }

    @Override
    default <B> Generator<B> pure(B b) {
        return Primitives.constant(b);
    }

    @Override
    default Generator<A> toGenerator() {
        return this;
    }

    default Maybe<String> getLabel() {
        return nothing();
    }

    default Maybe<Object> getApplicationData() {
        return nothing();
    }

    default boolean isPrimitive() {
        return true;
    }

    default Generator<A> labeled(String label) {
        return Primitives.withMetadata(Maybe.maybe(label), this.getApplicationData(), this);
    }

    default Generator<A> attachApplicationData(Object applicationData) {
        return Primitives.withMetadata(getLabel(), Maybe.maybe(applicationData), this);
    }

    default Generator<Tuple2<A, A>> pair() {
        return Generators.tupled(this, this);
    }

    default Generator<Tuple3<A, A, A>> triple() {
        return Generators.tupled(this, this, this);
    }

    default Generator<Maybe<A>> just() {
        return Generators.generateJust(this);
    }

    default Generator<Maybe<A>> maybe() {
        return Generators.generateMaybe(this);
    }

    default Generator<Maybe<A>> maybe(MaybeWeights weights) {
        return Generators.generateMaybe(weights, this);
    }

    default <R> Generator<Either<A, R>> left() {
        return CoProducts.generateLeft(this);
    }

    default <L> Generator<Either<L, A>> right() {
        return CoProducts.generateRight(this);
    }

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

    default Generator<ArrayList<A>> arrayListOfN(int count) {
        return Generators.generateArrayListOfN(count, this);
    }

    default Generator<ImmutableVector<A>> vector() {
        return Generators.generateVector(this);
    }

    default Generator<ImmutableVector<A>> vectorOfN(int count) {
        return Generators.generateVectorOfN(count, this);
    }

    default Generator<ImmutableNonEmptyVector<A>> nonEmptyVector() {
        return Generators.generateNonEmptyVector(this);
    }

    default Generator<ImmutableNonEmptyVector<A>> nonEmptyVectorOfN(int count) {
        return Generators.generateNonEmptyVectorOfN(count, this);
    }

    // **********
    // mixing in edge cases

    default Generator<A> injectSpecialValues(NonEmptyFiniteIterable<A> values) {
        return Bias.injectSpecialValues(values, this);
    }

    default Generator<A> injectSpecialValue(A specialValue) {
        return Bias.injectSpecialValue(specialValue, this);
    }

}
