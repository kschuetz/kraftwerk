package dev.marksman.composablerandom.spike;

import dev.marksman.composablerandom.EntropySource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class State {
    private final EntropySource entropySource;
    private final Context context;

    public static State input(EntropySource entropySource, Context context) {
        return new State(entropySource, context);
    }
}
