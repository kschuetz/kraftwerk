package dev.marksman.composablerandom;

import java.util.Random;

import static dev.marksman.composablerandom.StandardContext.defaultContext;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

public class Initialize {
    public static State randomInitialState() {
        return randomInitialState(defaultContext());
    }

    public static State randomInitialState(Context context) {
        return createInitialState(new Random().nextLong(), context);
    }

    public static State createInitialState(long initialSeedValue) {
        return State.state(initStandardGen(initialSeedValue));
    }

    public static State createInitialState(RandomState randomState) {
        return State.state(randomState);
    }

    public static State createInitialState(long initialSeedValue, Context context) {
        return State.state(initStandardGen(initialSeedValue), context);
    }

    public static State createInitialState(RandomState randomState, Context context) {
        return State.state(randomState, context);
    }
}
