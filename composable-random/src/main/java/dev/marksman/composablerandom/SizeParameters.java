package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SizeParameters {
    private static final SizeParameters NO_SIZE_LIMITS = new SizeParameters(nothing(), nothing(), nothing());

    private final Maybe<Integer> minSize;
    private final Maybe<Integer> maxSize;
    private final Maybe<Integer> preferredSize;

    public SizeParameters withMinSize(int size) {
        int min = Math.max(size, 0);
        Fn1<Integer, Integer> clamp = clampToFloor(min);
        return sizeParameters(just(min), maxSize.fmap(clamp), preferredSize.fmap(clampToFloor(min)));
    }

    public SizeParameters withMaxSize(int size) {
        int max = Math.max(size, 0);
        Fn1<Integer, Integer> clamp = clampToCeiling(max);
        return sizeParameters(minSize.fmap(clamp), just(max), preferredSize.fmap(clamp));
    }

    public SizeParameters withPreferredSize(int size) {
        int pref = Math.max(size, 0);
        return sizeParameters(minSize.fmap(clampToCeiling(pref)), maxSize.fmap(clampToFloor(pref)),
                just(pref));
    }

    public SizeParameters withNoMinSize() {
        return minSize.match(__ -> this,
                s -> sizeParameters(nothing(), maxSize, preferredSize));
    }

    public SizeParameters withNoMaxSize() {
        return maxSize.match(__ -> this,
                s -> sizeParameters(minSize, nothing(), preferredSize));
    }

    public SizeParameters withNoPreferredSize() {
        return maxSize.match(__ -> this,
                s -> sizeParameters(minSize, maxSize, nothing()));
    }

    // TODO: normalize parameters
    public static SizeParameters sizeParameters(Maybe<Integer> minSize, Maybe<Integer> maxSize, Maybe<Integer> preferredSize) {
        return new SizeParameters(minSize, maxSize, preferredSize);
    }

    public static SizeParameters sizeParameters(int minSize, int maxSize, int preferredSize) {
        if (minSize == maxSize) return exactSize(minSize);
        else return sizeParameters(just(minSize), just(maxSize), just(preferredSize));
    }

    public static SizeParameters exactSize(int size) {
        Maybe<Integer> n = just(size);
        return sizeParameters(n, n, n);
    }

    public static SizeParameters noSizeLimits() {
        return NO_SIZE_LIMITS;
    }

    private static Fn1<Integer, Integer> clampToCeiling(int max) {
        return n -> Math.min(n, max);
    }

    private static Fn1<Integer, Integer> clampToFloor(int min) {
        return n -> Math.max(n, min);
    }

}
