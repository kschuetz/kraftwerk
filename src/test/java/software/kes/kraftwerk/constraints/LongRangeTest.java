package software.kes.kraftwerk.constraints;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LongRangeTest {
    @Test
    void shouldThrowWithIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> LongRange.inclusive(1, 0));
        assertThrows(IllegalArgumentException.class, () -> LongRange.exclusive(1, 1));
    }

    @Test
    void inclusiveIteratesCorrectly() {
        assertThat(LongRange.inclusive(-5, 5), contains(-5L, -4L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L, 5L));
        assertThat(LongRange.inclusive(Long.MAX_VALUE - 4, Long.MAX_VALUE), contains(Long.MAX_VALUE - 4,
                Long.MAX_VALUE - 3, Long.MAX_VALUE - 2, Long.MAX_VALUE - 1, Long.MAX_VALUE));
        assertThat(LongRange.inclusive(Long.MIN_VALUE, Long.MIN_VALUE + 4), contains(Long.MIN_VALUE,
                Long.MIN_VALUE + 1, Long.MIN_VALUE + 2, Long.MIN_VALUE + 3, Long.MIN_VALUE + 4));
    }

    @Test
    void exclusiveIteratesCorrectly() {
        assertThat(LongRange.exclusive(-5, 5), contains(-5L, -4L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L));
        assertThat(LongRange.exclusive(Long.MAX_VALUE - 4, Long.MAX_VALUE), contains(Long.MAX_VALUE - 4,
                Long.MAX_VALUE - 3, Long.MAX_VALUE - 2, Long.MAX_VALUE - 1));
        assertThat(LongRange.exclusive(Long.MIN_VALUE, Long.MIN_VALUE + 4), contains(Long.MIN_VALUE,
                Long.MIN_VALUE + 1, Long.MIN_VALUE + 2, Long.MIN_VALUE + 3));
    }

    @Test
    void equality() {
        assertEquals(LongRange.inclusive(-10, 10), LongRange.exclusive(-10, 11));
        assertEquals(LongRange.inclusive(0, 10), LongRange.exclusive(11));
        assertEquals(LongRange.from(0).to(10), LongRange.inclusive(0, 10));
        assertEquals(LongRange.from(0).until(10), LongRange.exclusive(0, 10));
    }
}
