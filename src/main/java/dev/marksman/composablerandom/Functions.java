package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import static dev.marksman.composablerandom.Generator.tap;

class Functions {

    static <R> Generator<Fn0<R>> generateFn0(Generator<R> result) {
        return tap(result, (output, rs) -> () -> output.run(rs).getValue());
    }

    static <A, R> Generator<Fn1<A, R>> generateFn1(Cogenerator<A> param1, Generator<R> result) {
        return tap(result, (output, rs) -> a -> output.run(param1.apply(rs, a)).getValue());
    }

    static <A, B, R> Generator<Fn2<A, B, R>> generateFn2(Cogenerator<A> param1, Cogenerator<B> param2, Generator<R> result) {
        return Generator.tap(result,
                (output, rs) -> (a, b) -> output.run(param2.apply(param1.apply(rs, a), b)).getValue());
    }

}
