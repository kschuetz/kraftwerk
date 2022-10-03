package software.kes.kraftwerk;

import software.kes.kraftwerk.constraints.IntRange;

final class Preconditions {
    private Preconditions() {
    }

    static void requirePositiveSize(IntRange sizeRange) {
        if (sizeRange.minInclusive() < 1) {
            throw new IllegalArgumentException("minimum size must be >= 1");
        }
    }

    static void requirePositiveSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("minimum size must be >= 1");
        }
    }

    static void requireNaturalSize(IntRange sizeRange) {
        if (sizeRange.minInclusive() < 0) {
            throw new IllegalArgumentException("minimum size must be >= 0");
        }
    }

    static void requireNaturalSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("minimum size must be >= 0");
        }
    }
}
