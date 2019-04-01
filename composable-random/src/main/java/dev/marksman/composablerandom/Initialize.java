package dev.marksman.composablerandom;

import dev.marksman.composablerandom.legacy.OldContext;
import dev.marksman.composablerandom.legacy.State;

import java.util.Random;

import static dev.marksman.composablerandom.legacy.StandardContext.defaultContext;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

public class Initialize {
    public static State randomInitialState() {
        return randomInitialState(defaultContext());
    }

    public static State randomInitialState(OldContext context) {
        return createInitialState(new Random().nextLong(), context);
    }

    public static State createInitialState(long initialSeedValue) {
        return State.state(initStandardGen(initialSeedValue));
    }

    public static State createInitialState(RandomState randomState) {
        return State.state(randomState);
    }

    public static State createInitialState(long initialSeedValue, OldContext context) {
        return State.state(initStandardGen(initialSeedValue), context);
    }

    public static State createInitialState(RandomState randomState, OldContext context) {
        return State.state(randomState, context);
    }

    public static RandomState createInitialRandomState(long initialSeedValue) {
        return initStandardGen(initialSeedValue);
    }

    public static RandomState randomInitialRandomState() {
        return createInitialRandomState(new Random().nextLong());
    }
}
