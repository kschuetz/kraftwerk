package dev.marksman.composablerandom.legacy;

import com.jnape.palatable.lambda.lens.Lens;
import dev.marksman.composablerandom.RandomState;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class StateLens {

    public static Lens.Simple<State, OldContext> context = simpleLens(State::getContext,
            State::withContext);

    public static Lens.Simple<State, RandomState> randomState = simpleLens(State::getRandomState,
            State::withRandomState);

}
