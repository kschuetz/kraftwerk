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

public abstract class Generator<A> implements Monad<A, Generator<?>>, ToGenerator<A> {
    public Generator() {
    }

//    public abstract Result<? extends Seed, A> run(GeneratorContext context, Seed input);

    public abstract Generate<A> prepare(Parameters parameters);

    @Override
    public final <B> Generator<B> fmap(Fn1<? super A, ? extends B> fn) {
        return Generators.mapped(fn, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <B> Generator<B> flatMap(Fn1<? super A, ? extends Monad<B, Generator<?>>> f) {
        return Generators.flatMapped((Fn1<? super A, ? extends Generator<B>>) f, this);
    }

    @Override
    public final <B> Generator<B> pure(B b) {
        return Generators.constant(b);
    }

    @Override
    public final Generator<A> toGenerator() {
        return this;
    }

    public Maybe<String> getLabel() {
        return nothing();
    }

    public Maybe<Object> getApplicationData() {
        return nothing();
    }

    public boolean isPrimitive() {
        return true;
    }

    public final Generator<A> labeled(String label) {
        return Generators.withMetadata(Maybe.maybe(label), this.getApplicationData(), this);
    }

    public final Generator<A> attachApplicationData(Object applicationData) {
        return Generators.withMetadata(getLabel(), Maybe.maybe(applicationData), this);
    }

    public final Generator<Tuple2<A, A>> pair() {
        return Generators.tupled(this, this);
    }

    public final Generator<Tuple3<A, A, A>> triple() {
        return Generators.tupled(this, this, this);
    }

    public final Generator<Maybe<A>> just() {
        return Generators.generateJust(this);
    }

    public final Generator<Maybe<A>> maybe() {
        return Generators.generateMaybe(this);
    }

    public final Generator<Maybe<A>> maybe(MaybeWeights weights) {
        return Generators.generateMaybe(weights, this);
    }

    public final <R> Generator<Either<A, R>> left() {
        return CoProducts.generateLeft(this);
    }

    public final <L> Generator<Either<L, A>> right() {
        return CoProducts.generateRight(this);
    }

    public final Generator<A> withNulls() {
        return Generators.generateWithNulls(this);
    }

    public final Generator<A> withNulls(NullWeights weights) {
        return Generators.generateWithNulls(weights, this);
    }

    public final Generator<ArrayList<A>> arrayList() {
        return Generators.generateArrayList(this);
    }

    public final Generator<ArrayList<A>> nonEmptyArrayList() {
        return Generators.generateNonEmptyArrayList(this);
    }

    public final Generator<ArrayList<A>> arrayListOfN(int count) {
        return Generators.generateArrayListOfN(count, this);
    }

    public final Generator<ImmutableVector<A>> vector() {
        return Generators.generateVector(this);
    }

    public final Generator<ImmutableVector<A>> vectorOfN(int count) {
        return Generators.generateVectorOfN(count, this);
    }

    public final Generator<ImmutableNonEmptyVector<A>> nonEmptyVector() {
        return Generators.generateNonEmptyVector(this);
    }

    public final Generator<ImmutableNonEmptyVector<A>> nonEmptyVectorOfN(int count) {
        return Generators.generateNonEmptyVectorOfN(count, this);
    }

    // **********
    // mixing in edge cases

    public final Generator<A> injectSpecialValues(NonEmptyFiniteIterable<A> values) {
        return Generators.injectSpecialValues(values, this);
    }



    /*

    private final ImmutableNonEmptyVector<Elem> elements;
    private final int specialWeight;
    private final long totalWeight;
    private final GeneratorImpl<Elem> inner;

    private InjectSpecialValuesImpl(ImmutableNonEmptyVector<Elem> elements, long nonSpecialWeight, GeneratorImpl<Elem> inner) {
        this.elements = elements;
        this.specialWeight = elements.size();
        this.totalWeight = Math.max(0, nonSpecialWeight) + specialWeight;
        this.inner = inner;
    }

    @Override
    public Result<? extends LegacySeed, Elem> run(LegacySeed input) {
        // TODO: InjectSpecialValuesImpl
        long n = input.getSeedValue() % totalWeight;
        if (n < specialWeight) {
            Result<? extends LegacySeed, Integer> nextSeed = input.nextInt();
            return result(nextSeed.getNextState(), elements.unsafeGet((int) n));
        } else {
            return inner.run(input);
        }
    }



    if (gen instanceof Generator.InjectSpecialValues) {
            Generator.InjectSpecialValues<A> g1 = (Generator.InjectSpecialValues<A>) gen;
            NonEmptyFiniteIterable<A> acc = g1.getSpecialValues();
            while (g1.getInner() instanceof Generator.InjectSpecialValues) {
                g1 = (Generator.InjectSpecialValues<A>) g1.getInner();
                acc = acc.concat(g1.getSpecialValues());
            }
            ImmutableNonEmptyVector<A> specialValues = NonEmptyVector.copyFromOrThrow(acc);
            return mixInSpecialValuesImpl(specialValues, 20 + 3 * specialValues.size(),
                    context.recurse(g1.getInner()));
        }
     */


    // TODO:  organize these


}
