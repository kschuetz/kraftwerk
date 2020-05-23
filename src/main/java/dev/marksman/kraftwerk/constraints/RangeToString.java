package dev.marksman.kraftwerk.constraints;

import java.util.Objects;

final class RangeToString {
    static <A> String rangeToString(String label, A min, boolean minIncluded, A max, boolean maxIncluded) {
        String minPart = minIncluded
                ? Objects.toString(min)
                : min + " (exclusive)";
        String maxPart = maxIncluded
                ? Objects.toString(max)
                : max + " (exclusive)";
        return label + "{" + minPart + " .. " + maxPart + "}";
    }
}
