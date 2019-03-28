package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class ResultLens {

    public static <S, A> Lens.Simple<Result<S, A>, S> nextState() {
        return simpleLens(Result::getNextState, Result::withNextState);
    }

    public static <S, A> Lens.Simple<Result<S, A>, A> value() {
        return simpleLens(Result::getValue, Result::withValue);
    }

}
