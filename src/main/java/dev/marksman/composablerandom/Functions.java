package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;

class Functions {

    static <A, B> Generator<Fn1<A, B>> generateFn1(Cogenerator<A> param1, Generator<B> codomain) {
        return Generator.higherOrder(codomain, (output, rs) -> {
            Fn1<A, B> function = a -> {
                RandomState newInput = param1.apply(rs, a);
                return output.run(newInput).getValue();
            };
            return Result.result(rs.nextInt().getNextState(), function);
        });

    }
}
