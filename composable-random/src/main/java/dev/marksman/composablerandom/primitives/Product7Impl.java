package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product7Impl<A, B, C, D, E, F, G> implements CompiledGenerator<Tuple7<A, B, C, D, E, F, G>> {
    private final CompiledGenerator<A> a;
    private final CompiledGenerator<B> b;
    private final CompiledGenerator<C> c;
    private final CompiledGenerator<D> d;
    private final CompiledGenerator<E> e;
    private final CompiledGenerator<F> f;
    private final CompiledGenerator<G> g;

    @Override
    public Result<? extends RandomState, Tuple7<A, B, C, D, E, F, G>> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.run(r3.getNextState());
        Result<? extends RandomState, E> r5 = e.run(r4.getNextState());
        Result<? extends RandomState, F> r6 = f.run(r5.getNextState());
        Result<? extends RandomState, G> r7 = g.run(r6.getNextState());
        Tuple7<A, B, C, D, E, F, G> result = tuple(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue(), r7.getValue());
        return result(r7.getNextState(), result);
    }

    public static <A, B, C, D, E, F, G> Product7Impl<A, B, C, D, E, F, G> product7Impl(CompiledGenerator<A> a,
                                                                                       CompiledGenerator<B> b,
                                                                                       CompiledGenerator<C> c,
                                                                                       CompiledGenerator<D> d,
                                                                                       CompiledGenerator<E> e,
                                                                                       CompiledGenerator<F> f,
                                                                                       CompiledGenerator<G> g) {
        return new Product7Impl<>(a, b, c, d, e, f, g);
    }

}
