package software.kes.kraftwerk.constraints;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShortRangeTest {
    @Test
    void shouldThrowWithIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> ShortRange.inclusive((short) 1, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> ShortRange.exclusive((short) 1, (short) 1));
    }

    @Test
    void inclusiveIteratesCorrectly() {
        assertThat(ShortRange.inclusive((short) -5, (short) 5), contains((short) -5, (short) -4, (short) -3, (short) -2,
                (short) -1, (short) 0, (short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
        assertThat(ShortRange.inclusive((short) (Short.MAX_VALUE - 4), Short.MAX_VALUE), contains((short) (Short.MAX_VALUE - 4),
                (short) (Short.MAX_VALUE - 3), (short) (Short.MAX_VALUE - 2), (short) (Short.MAX_VALUE - 1), Short.MAX_VALUE));
        assertThat(ShortRange.inclusive(Short.MIN_VALUE, (short) (Short.MIN_VALUE + 4)), contains(Short.MIN_VALUE,
                (short) (Short.MIN_VALUE + 1), (short) (Short.MIN_VALUE + 2), (short) (Short.MIN_VALUE + 3), (short) (Short.MIN_VALUE + 4)));
    }

    @Test
    void exclusiveIteratesCorrectly() {
        assertThat(ShortRange.exclusive((short) -5, (short) 5), contains((short) -5, (short) -4, (short) -3, (short) -2,
                (short) -1, (short) 0, (short) 1, (short) 2, (short) 3, (short) 4));
        assertThat(ShortRange.exclusive((short) (Short.MAX_VALUE - 4), Short.MAX_VALUE), contains((short) (Short.MAX_VALUE - 4),
                (short) (Short.MAX_VALUE - 3), (short) (Short.MAX_VALUE - 2), (short) (Short.MAX_VALUE - 1)));
        assertThat(ShortRange.exclusive(Short.MIN_VALUE, (short) (Short.MIN_VALUE + 4)), contains(Short.MIN_VALUE,
                (short) (Short.MIN_VALUE + 1), (short) (Short.MIN_VALUE + 2), (short) (Short.MIN_VALUE + 3)));
    }

    @Test
    void equality() {
        assertEquals(ShortRange.inclusive((short) -10, (short) 10), ShortRange.exclusive((short) -10, (short) 11));
        assertEquals(ShortRange.inclusive((short) 0, (short) 10), ShortRange.exclusive((short) 11));
        assertEquals(ShortRange.from((short) 0).to((short) 10), ShortRange.inclusive((short) 0, (short) 10));
        assertEquals(ShortRange.from((short) 0).until((short) 10), ShortRange.exclusive((short) 0, (short) 10));
    }
}
