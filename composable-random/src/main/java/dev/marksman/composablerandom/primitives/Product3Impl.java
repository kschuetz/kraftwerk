package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product3Impl<A, B, C> implements CompiledGenerator<Tuple3<A, B, C>> {
    private final CompiledGenerator<A> a;
    private final CompiledGenerator<B> b;
    private final CompiledGenerator<C> c;

    @Override
    public Result<? extends RandomState, Tuple3<A, B, C>> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Tuple3<A, B, C> result = tuple(r1.getValue(), r2.getValue(), r3.getValue());
        return result(r3.getNextState(), result);
    }

    public static <A, B, C> Product3Impl<A, B, C> product3Impl(CompiledGenerator<A> a,
                                                               CompiledGenerator<B> b,
                                                               CompiledGenerator<C> c) {
        return new Product3Impl<>(a, b, c);
    }

}
