package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class State {
    private final RandomState randomState;
    private final Context context;

    public final State withRandomState(RandomState newState) {
        return state(newState, context);
    }

    public final State withContext(StandardContext newContext) {
        return state(randomState, newContext);
    }

    public final State modifyContext(Function<? super Context, ? extends Context> f) {
        return state(randomState, f.apply(context));
    }

    public static State state(RandomState randomState, Context context) {
        return new State(randomState, context);
    }

    public static State state(RandomState randomState) {
        return new State(randomState, StandardContext.defaultContext());
    }
}
