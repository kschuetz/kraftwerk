package dev.marksman.kraftwerk.optics;

import com.jnape.palatable.lambda.optics.Lens;
import dev.marksman.kraftwerk.Result;

import static com.jnape.palatable.lambda.optics.Lens.simpleLens;

public class ResultLens {

    public static <S, A> Lens.Simple<Result<S, A>, S> nextState() {
        return simpleLens(Result::getNextState, Result::withNextState);
    }

    public static <S, A> Lens.Simple<Result<S, A>, A> value() {
        return simpleLens(Result::getValue, Result::withValue);
    }

}
