package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product2Impl<A, B> implements CompiledGenerator<Tuple2<A, B>> {
    private final CompiledGenerator<A> a;
    private final CompiledGenerator<B> b;

    @Override
    public Result<? extends RandomState, Tuple2<A, B>> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Tuple2<A, B> result = tuple(r1.getValue(), r2.getValue());
        return result(r2.getNextState(), result);
    }

    public static <A, B> Product2Impl<A, B> product2Impl(CompiledGenerator<A> a,
                                                         CompiledGenerator<B> b) {
        return new Product2Impl<>(a, b);
    }

}
