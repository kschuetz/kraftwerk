package software.kes.kraftwerk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class WeightedTest {
    @Test
    void throwsIfWeightIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> Weighted.weighted(-1, "foo"));
    }

    @Test
    void defaultWeightIsOne() {
        Assertions.assertEquals(1, Weighted.weighted("foo").getWeight());
    }

    @Test
    void throwsIfMultiplyByZero() {
        assertThrows(IllegalArgumentException.class, () -> Weighted.weighted(2, "foo").multiplyBy(0));
    }

    @Test
    void throwsIfMultiplyByNegative() {
        assertThrows(IllegalArgumentException.class, () -> Weighted.weighted(2, "foo").multiplyBy(0));
    }

    @Test
    void multiplication() {
        Assertions.assertEquals(4, Weighted.weighted(2, "foo").multiplyBy(2).getWeight());
        Assertions.assertEquals(2, Weighted.weighted(2, "foo").multiplyBy(1).getWeight());
    }

    @Test
    void testEquality() {
        Assertions.assertEquals(Weighted.weighted(1, "foo"), Weighted.weighted(1, "foo"));
        Assertions.assertNotEquals(Weighted.weighted(2, "foo"), Weighted.weighted(1, "foo"));
    }

    @Test
    void fmapRetainsWeightValue() {
        Assertions.assertEquals(123, Weighted.weighted(123, "foo").fmap(String::length).getWeight());
    }
}