package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

/**
 * Guidelines for choosing sizes.
 * <p>
 * {@link Generator}s will consult these parameters when tasked with creating objects that have sizes (e.g. collections).
 */
public final class SizeParameters {
    private static final SizeParameters NO_SIZE_LIMITS = new SizeParameters(nothing(), nothing(), nothing());

    private final Maybe<Integer> minSize;
    private final Maybe<Integer> maxSize;
    private final Maybe<Integer> preferredSize;

    private SizeParameters(Maybe<Integer> minSize, Maybe<Integer> maxSize, Maybe<Integer> preferredSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.preferredSize = preferredSize;
    }

    /**
     * Creates a {@code SizeParameters}.
     *
     * @param minSize       an optional minimum size
     * @param maxSize       an optional maximum size
     * @param preferredSize an optional "preferred size" (a sensible default that should occur more frequently than other sizes)
     * @return a {@code SizeParameters}
     */
    public static SizeParameters sizeParameters(Maybe<Integer> minSize, Maybe<Integer> maxSize, Maybe<Integer> preferredSize) {
        return new SizeParameters(minSize, maxSize, preferredSize);
    }

    /**
     * Creates a {@code SizeParameters}.
     *
     * @param minSize       the minimum size
     * @param maxSize       the maximum size
     * @param preferredSize the "preferred size" (a sensible default that should occur more frequently than other sizes)
     * @return a {@code SizeParameters}
     */
    public static SizeParameters sizeParameters(int minSize, int maxSize, int preferredSize) {
        if (minSize == maxSize) return exactSize(minSize);
        else return sizeParameters(just(minSize), just(maxSize), just(preferredSize));
    }

    /**
     * Creates a {@code SizeParameters} that limits the size to a single value.
     *
     * @return a {@code SizeParameters}
     */
    public static SizeParameters exactSize(int size) {
        Maybe<Integer> n = just(size);
        return sizeParameters(n, n, n);
    }

    /**
     * Creates a {@code SizeParameters} that has no size limits.
     *
     * @return a {@code SizeParameters}
     */
    public static SizeParameters noSizeLimits() {
        return NO_SIZE_LIMITS;
    }

    private static Fn1<Integer, Integer> clampToCeiling(int max) {
        return n -> Math.min(n, max);
    }

    private static Fn1<Integer, Integer> clampToFloor(int min) {
        return n -> Math.max(n, min);
    }

    /**
     * Creates a new {@code SizeParameters} that as the same as this one, with a new value for "minimum size".
     *
     * @return a {@code SizeParameters}
     */

    public SizeParameters withMinSize(int size) {
        int min = Math.max(size, 0);
        Fn1<Integer, Integer> clamp = clampToFloor(min);
        return sizeParameters(just(min), maxSize.fmap(clamp), preferredSize.fmap(clampToFloor(min)));
    }

    /**
     * Creates a new {@code SizeParameters} that as the same as this one, with a new value for "maximum size".
     *
     * @return a {@code SizeParameters}
     */
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

    /**
     * Creates a new {@code SizeParameters} that as the same as this one, with the value for "minimum size" removed.
     *
     * @return a {@code SizeParameters}
     */
    public SizeParameters withNoMinSize() {
        return minSize.match(__ -> this,
                s -> sizeParameters(nothing(), maxSize, preferredSize));
    }

    /**
     * Creates a new {@code SizeParameters} that as the same as this one, with the value for "maximum size" removed.
     *
     * @return a {@code SizeParameters}
     */
    public SizeParameters withNoMaxSize() {
        return maxSize.match(__ -> this,
                s -> sizeParameters(minSize, nothing(), preferredSize));
    }

    /**
     * Creates a new {@code SizeParameters} that as the same as this one, with the value for "preferred size" removed.
     *
     * @return a {@code SizeParameters}
     */
    public SizeParameters withNoPreferredSize() {
        return maxSize.match(__ -> this,
                s -> sizeParameters(minSize, maxSize, nothing()));
    }

    public Maybe<Integer> getMinSize() {
        return this.minSize;
    }

    public Maybe<Integer> getMaxSize() {
        return this.maxSize;
    }

    public Maybe<Integer> getPreferredSize() {
        return this.preferredSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SizeParameters that = (SizeParameters) o;

        if (!minSize.equals(that.minSize)) return false;
        if (!maxSize.equals(that.maxSize)) return false;
        return preferredSize.equals(that.preferredSize);
    }

    @Override
    public int hashCode() {
        int result = minSize.hashCode();
        result = 31 * result + maxSize.hashCode();
        result = 31 * result + preferredSize.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SizeParameters{" +
                "minSize=" + minSize +
                ", maxSize=" + maxSize +
                ", preferredSize=" + preferredSize +
                '}';
    }
}
