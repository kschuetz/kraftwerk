package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.kraftwerk.core.BuildingBlocks;

import static dev.marksman.kraftwerk.Result.result;

final class Tap {
    private Tap() {
    }

    static <A, B> Generator<B> tap(Generator<A> gen,
                                   Fn2<GenerateFn<A>, Seed, B> f) {
        return parameters -> {
            GenerateFn<A> runA = gen.createGenerateFn(parameters);
            return input -> {
                Seed nextState = BuildingBlocks.nextInt(input).getNextState();
                return result(nextState,
                        f.apply(runA, input));
            };
        };
    }
}
