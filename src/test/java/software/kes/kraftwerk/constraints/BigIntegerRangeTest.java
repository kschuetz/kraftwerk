package software.kes.kraftwerk.constraints;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BigIntegerRangeTest {
    @Test
    void shouldThrowWithIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> BigIntegerRange.inclusive(BigInteger.ONE, BigInteger.ZERO));
        assertThrows(IllegalArgumentException.class, () -> BigIntegerRange.exclusive(BigInteger.ONE, BigInteger.ONE));
    }

    @Test
    void inclusiveIteratesCorrectly() {
        assertThat(BigIntegerRange.inclusive(BigInteger.ONE, BigInteger.ONE), contains(BigInteger.ONE));
        assertThat(BigIntegerRange.inclusive(BigInteger.valueOf(-5), BigInteger.valueOf(5)),
                contains(BigInteger.valueOf(-5), BigInteger.valueOf(-4), BigInteger.valueOf(-3), BigInteger.valueOf(-2),
                        BigInteger.valueOf(-1), BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(3),
                        BigInteger.valueOf(4), BigInteger.valueOf(5)));
    }

    @Test
    void exclusiveIteratesCorrectly() {
        assertThat(BigIntegerRange.exclusive(BigInteger.ONE, BigInteger.valueOf(2)), contains(BigInteger.ONE));
        assertThat(BigIntegerRange.exclusive(BigInteger.valueOf(-5), BigInteger.valueOf(6)),
                contains(BigInteger.valueOf(-5), BigInteger.valueOf(-4), BigInteger.valueOf(-3), BigInteger.valueOf(-2),
                        BigInteger.valueOf(-1), BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(3),
                        BigInteger.valueOf(4), BigInteger.valueOf(5)));
    }

    @Test
    void equality() {
        assertEquals(BigIntegerRange.inclusive(BigInteger.valueOf(-10), BigInteger.valueOf(10)),
                BigIntegerRange.exclusive(BigInteger.valueOf(-10), BigInteger.valueOf(11)));
        assertEquals(BigIntegerRange.inclusive(BigInteger.ZERO, BigInteger.valueOf(10)), BigIntegerRange.exclusive(BigInteger.valueOf(11)));
        assertEquals(BigIntegerRange.from(BigInteger.ONE).to(BigInteger.TEN), BigIntegerRange.inclusive(BigInteger.ONE, BigInteger.TEN));
        assertEquals(BigIntegerRange.from(BigInteger.ONE).until(BigInteger.TEN), BigIntegerRange.exclusive(BigInteger.ONE, BigInteger.TEN));
    }
}
