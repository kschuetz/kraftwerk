package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteIntRange.*;

public interface IntRange {
    int min();

    int max();

    static IntRangeFrom from(int min) {
        return concreteIntRangeFrom(min);
    }

    static IntRange inclusive(int min, int max) {
        return concreteIntRangeInclusive(min, max);
    }

    static IntRange exclusive(int min, int maxExclusive) {
        return concreteIntRangeExclusive(min, maxExclusive);
    }

    static IntRange exclusive(int maxExclusive) {
        return concreteIntRangeExclusive(0, maxExclusive);
    }

    interface IntRangeFrom {
        IntRange to(int max);

        IntRange until(int maxExclusive);
    }
}
