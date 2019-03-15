package dev.marksman.random;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.product.Product2.product;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Random<A> implements Monad<A, Random>, Traversable<A, Random> {

    private static final Random<Boolean> RANDOM_BOOLEAN = random(RandomGen::nextBoolean);
    private static final Random<Byte> RANDOM_BYTE = random(RandomGen::nextByte);
    private static final Random<Double> RANDOM_DOUBLE = random(RandomGen::nextDouble);
    private static final Random<Float> RANDOM_FLOAT = random(RandomGen::nextFloat);
    private static final Random<Integer> RANDOM_INTEGER = random(RandomGen::nextInt);
    private static final Random<Long> RANDOM_LONG = random(RandomGen::nextLong);
    private static final Random<Short> RANDOM_SHORT = random(RandomGen::nextShort);

    private final Function<RandomGen, Product2<A, ? extends RandomGen>> run;

    public final Product2<A, ? extends RandomGen> run(RandomGen randomGen) {
        return run.apply(randomGen);
    }

    @Override
    public final <B> Random<B> fmap(Function<? super A, ? extends B> fn) {
        return new Random<>(rg0 -> {
            Product2<A, ? extends RandomGen> x = run.apply(rg0);
            return product(fn.apply(x._1()), x._2());
        });
    }

    @Override
    public final <B> Random<B> flatMap(Function<? super A, ? extends Monad<B, Random>> fn) {
        return random(rg0 -> {
            Product2<A, ? extends RandomGen> x = run.apply(rg0);
            return ((Random<B>) fn.apply(x._1())).run.apply(x._2());
        });
    }

    @Override
    public final <B> Monad<B, Random> pure(B b) {
        return constant(b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <B, App extends Applicative, TravB extends Traversable<B, Random>, AppB extends Applicative<B, App>, AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Function<? super A, ? extends AppB> fn,
            Function<? super TravB, ? extends AppTrav> pure) {
        // TODO:  Traversable
        return null;
    }

    public final Random<Tuple2<A, A>> pair() {
        return (Random<Tuple2<A, A>>) zip(fmap(tupler()));
    }

    public static <A> Random<A> random(Function<RandomGen, Product2<A, ? extends RandomGen>> op) {
        return new Random<>(op);
    }

    public static <A> Random<A> constant(A a) {
        return random(rg -> product(a, rg));
    }

    public static Random<Boolean> randomBoolean() {
        return RANDOM_BOOLEAN;
    }

    public static Random<Byte> randomByte() {
        return RANDOM_BYTE;
    }

    public static Random<Double> randomDouble() {
        return RANDOM_DOUBLE;
    }

    public static Random<Integer> randomInt() {
        return RANDOM_INTEGER;
    }

    public static Random<Float> randomFloat() {
        return RANDOM_FLOAT;
    }

    public static Random<Integer> randomInt(int bound) {
        return random(s -> s.nextInt(bound));
    }

    public static Random<Long> randomLong() {
        return RANDOM_LONG;
    }

    public static Random<Short> randomShort() {
        return RANDOM_SHORT;
    }

    public static Random<Byte[]> randomBytes(int count) {
        return random(s -> s.nextBytes(new byte[count]));
    }

}
