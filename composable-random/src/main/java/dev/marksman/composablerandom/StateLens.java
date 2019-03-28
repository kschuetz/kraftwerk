package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class StateLens {

    public static Lens.Simple<State, Context> context = simpleLens(State::getContext,
            State::withContext);

    public static Lens.Simple<State, RandomState> randomState = simpleLens(State::getRandomState,
            State::withRandomState);

}
