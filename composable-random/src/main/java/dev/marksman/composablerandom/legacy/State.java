package dev.marksman.composablerandom.legacy;

import dev.marksman.composablerandom.RandomState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class State {
    private final RandomState randomState;
    private final OldContext context;

    public final State withRandomState(RandomState newState) {
        return state(newState, context);
    }

    public final State withContext(OldContext newContext) {
        return state(randomState, newContext);
    }

    public final State modifyContext(Function<? super OldContext, ? extends OldContext> f) {
        return state(randomState, f.apply(context));
    }

    public static State state(RandomState randomState, OldContext context) {
        return new State(randomState, context);
    }

    public static State state(RandomState randomState) {
        return new State(randomState, StandardContext.defaultContext());
    }
}
