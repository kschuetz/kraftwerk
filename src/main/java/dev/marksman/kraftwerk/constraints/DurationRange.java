package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;

import java.time.Duration;

import static dev.marksman.kraftwerk.constraints.ConcreteDurationRange.concreteDurationRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteDurationRange.concreteDurationRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteDurationRange.concreteDurationRangeInclusive;

public interface DurationRange {
    Duration minInclusive();

    Duration maxInclusive();

    default boolean contains(Duration duration) {
        return GTE.gte(minInclusive(), duration) && LTE.lte(maxInclusive(), duration);
    }

    default DurationRange withMin(Duration min) {
        return concreteDurationRangeInclusive(min, maxInclusive());
    }

    default DurationRange withMaxInclusive(Duration max) {
        return concreteDurationRangeInclusive(minInclusive(), max);
    }

    default DurationRange withMaxExclusive(Duration max) {
        return concreteDurationRangeExclusive(minInclusive(), max);
    }

    static DurationFrom from(Duration min) {
        return concreteDurationRangeFrom(min);
    }

    static DurationRange inclusive(Duration min, Duration max) {
        return concreteDurationRangeInclusive(min, max);
    }

    static DurationRange inclusive(Duration max) {
        return concreteDurationRangeInclusive(Duration.ZERO, max);
    }

    static DurationRange exclusive(Duration min, Duration maxExclusive) {
        return concreteDurationRangeExclusive(min, maxExclusive);
    }

    static DurationRange exclusive(Duration maxExclusive) {
        return concreteDurationRangeExclusive(Duration.ZERO, maxExclusive);
    }

    interface DurationFrom {
        DurationRange to(Duration max);

        DurationRange until(Duration maxExclusive);
    }

}
