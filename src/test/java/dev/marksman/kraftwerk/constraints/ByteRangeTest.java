package dev.marksman.kraftwerk.constraints;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteRangeTest {
    @Test
    void shouldThrowWithIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> ByteRange.inclusive((byte) 1, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> ByteRange.exclusive((byte) 1, (byte) 1));
    }

    @Test
    void inclusiveIteratesCorrectly() {
        assertThat(ByteRange.inclusive((byte) -5, (byte) 5), contains((byte) -5, (byte) -4, (byte) -3, (byte) -2,
                (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5));
        assertThat(ByteRange.inclusive((byte) (Byte.MAX_VALUE - 4), Byte.MAX_VALUE), contains((byte) (Byte.MAX_VALUE - 4),
                (byte) (Byte.MAX_VALUE - 3), (byte) (Byte.MAX_VALUE - 2), (byte) (Byte.MAX_VALUE - 1), Byte.MAX_VALUE));
        assertThat(ByteRange.inclusive(Byte.MIN_VALUE, (byte) (Byte.MIN_VALUE + 4)), contains(Byte.MIN_VALUE,
                (byte) (Byte.MIN_VALUE + 1), (byte) (Byte.MIN_VALUE + 2), (byte) (Byte.MIN_VALUE + 3), (byte) (Byte.MIN_VALUE + 4)));
    }

    @Test
    void exclusiveIteratesCorrectly() {
        assertThat(ByteRange.exclusive((byte) -5, (byte) 5), contains((byte) -5, (byte) -4, (byte) -3, (byte) -2,
                (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4));
        assertThat(ByteRange.exclusive((byte) (Byte.MAX_VALUE - 4), Byte.MAX_VALUE), contains((byte) (Byte.MAX_VALUE - 4),
                (byte) (Byte.MAX_VALUE - 3), (byte) (Byte.MAX_VALUE - 2), (byte) (Byte.MAX_VALUE - 1)));
        assertThat(ByteRange.exclusive(Byte.MIN_VALUE, (byte) (Byte.MIN_VALUE + 4)), contains(Byte.MIN_VALUE,
                (byte) (Byte.MIN_VALUE + 1), (byte) (Byte.MIN_VALUE + 2), (byte) (Byte.MIN_VALUE + 3)));
    }

    @Test
    void equality() {
        assertEquals(ByteRange.inclusive((byte) -10, (byte) 10), ByteRange.exclusive((byte) -10, (byte) 11));
        assertEquals(ByteRange.inclusive((byte) 0, (byte) 10), ByteRange.exclusive((byte) 11));
        assertEquals(ByteRange.from((byte) 0).to((byte) 10), ByteRange.inclusive((byte) 0, (byte) 10));
        assertEquals(ByteRange.from((byte) 0).until((byte) 10), ByteRange.exclusive((byte) 0, (byte) 10));
    }
}
