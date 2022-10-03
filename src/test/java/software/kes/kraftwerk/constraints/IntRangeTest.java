package software.kes.kraftwerk.constraints;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntRangeTest {
    @Test
    void shouldThrowWithIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> IntRange.inclusive(1, 0));
        assertThrows(IllegalArgumentException.class, () -> IntRange.exclusive(1, 1));
    }

    @Test
    void inclusiveIteratesCorrectly() {
        assertThat(IntRange.inclusive(-5, 5), contains(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5));
        assertThat(IntRange.inclusive(Integer.MAX_VALUE - 4, Integer.MAX_VALUE), contains(Integer.MAX_VALUE - 4,
                Integer.MAX_VALUE - 3, Integer.MAX_VALUE - 2, Integer.MAX_VALUE - 1, Integer.MAX_VALUE));
        assertThat(IntRange.inclusive(Integer.MIN_VALUE, Integer.MIN_VALUE + 4), contains(Integer.MIN_VALUE,
                Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, Integer.MIN_VALUE + 3, Integer.MIN_VALUE + 4));
    }

    @Test
    void exclusiveIteratesCorrectly() {
        assertThat(IntRange.exclusive(-5, 5), contains(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4));
        assertThat(IntRange.exclusive(Integer.MAX_VALUE - 4, Integer.MAX_VALUE), contains(Integer.MAX_VALUE - 4,
                Integer.MAX_VALUE - 3, Integer.MAX_VALUE - 2, Integer.MAX_VALUE - 1));
        assertThat(IntRange.exclusive(Integer.MIN_VALUE, Integer.MIN_VALUE + 4), contains(Integer.MIN_VALUE,
                Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2, Integer.MIN_VALUE + 3));
    }

    @Test
    void equality() {
        assertEquals(IntRange.inclusive(-10, 10), IntRange.exclusive(-10, 11));
        assertEquals(IntRange.inclusive(0, 10), IntRange.exclusive(11));
        assertEquals(IntRange.from(0).to(10), IntRange.inclusive(0, 10));
        assertEquals(IntRange.from(0).until(10), IntRange.exclusive(0, 10));
    }
}
