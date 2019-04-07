package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product4Impl<A, B, C, D> implements CompiledGenerator<Tuple4<A, B, C, D>> {
    private final CompiledGenerator<A> a;
    private final CompiledGenerator<B> b;
    private final CompiledGenerator<C> c;
    private final CompiledGenerator<D> d;

    @Override
    public Result<? extends RandomState, Tuple4<A, B, C, D>> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.run(r3.getNextState());
        Tuple4<A, B, C, D> result = tuple(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue());
        return result(r4.getNextState(), result);
    }

    public static <A, B, C, D> Product4Impl<A, B, C, D> product4Impl(CompiledGenerator<A> a,
                                                                     CompiledGenerator<B> b,
                                                                     CompiledGenerator<C> c,
                                                                     CompiledGenerator<D> d) {
        return new Product4Impl<>(a, b, c, d);
    }

}
