package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.constraints.IntRange;

final class Normalize {
    private Normalize() {
    }

    static IntRange normalizeSizeRange(IntRange input) {
        if (input.minInclusive() < 0) {
            if (input.maxInclusive() < 0) {
                return IntRange.inclusive(0, 0);
            } else {
                return input.withMinInclusive(0);
            }
        } else {
            return input;
        }
    }
}
