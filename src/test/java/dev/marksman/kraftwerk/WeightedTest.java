package dev.marksman.kraftwerk;

import org.junit.jupiter.api.Test;

import static dev.marksman.kraftwerk.Weighted.weighted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeightedTest {
    @Test
    void throwsIfWeightIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> weighted(-1, "foo"));
    }

    @Test
    void defaultWeightIsOne() {
        assertEquals(1, weighted("foo").getWeight());
    }

    @Test
    void throwsIfMultiplyByZero() {
        assertThrows(IllegalArgumentException.class, () -> weighted(2, "foo").multiplyBy(0));
    }

    @Test
    void throwsIfMultiplyByNegative() {
        assertThrows(IllegalArgumentException.class, () -> weighted(2, "foo").multiplyBy(0));
    }

    @Test
    void multiplication() {
        assertEquals(4, weighted(2, "foo").multiplyBy(2).getWeight());
        assertEquals(2, weighted(2, "foo").multiplyBy(1).getWeight());
    }

    @Test
    void testEquality() {
        assertEquals(weighted(1, "foo"), weighted(1, "foo"));
        assertNotEquals(weighted(2, "foo"), weighted(1, "foo"));
    }

    @Test
    void fmapRetainsWeightValue() {
        assertEquals(123, weighted(123, "foo").fmap(String::length).getWeight());
    }
}