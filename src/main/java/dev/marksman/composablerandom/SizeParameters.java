package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SizeParameters {
    private static final SizeParameters NO_SIZE_LIMITS = new SizeParameters(nothing(), nothing());

    private final Maybe<Integer> minSize;
    private final Maybe<Integer> maxSize;

    public SizeParameters withMinSize(int size) {
        return sizeParameters(just(size), maxSize);
    }

    public SizeParameters withMaxSize(int size) {
        return sizeParameters(minSize, just(size));
    }

    public SizeParameters withNoMinSize() {
        return minSize.match(__ -> this,
                s -> sizeParameters(nothing(), maxSize));
    }

    public SizeParameters withNoMaxSize() {
        return maxSize.match(__ -> this,
                s -> sizeParameters(minSize, nothing()));
    }

    public static SizeParameters sizeParameters(Maybe<Integer> minSize, Maybe<Integer> maxSize) {
        return new SizeParameters(minSize, maxSize);
    }

    public static SizeParameters sizeParameters(int minSize, int maxSize) {
        if (minSize == maxSize) return exactSize(minSize);
        else return sizeParameters(just(minSize), just(maxSize));
    }

    public static SizeParameters exactSize(int size) {
        Maybe<Integer> n = just(size);
        return sizeParameters(n, n);
    }

    public static SizeParameters noSizeLimits() {
        return NO_SIZE_LIMITS;
    }

}
