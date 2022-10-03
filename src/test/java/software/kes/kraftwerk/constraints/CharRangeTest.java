package software.kes.kraftwerk.constraints;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharRangeTest {
    @Test
    void shouldThrowWithIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> CharRange.inclusive('b', 'a'));
        assertThrows(IllegalArgumentException.class, () -> CharRange.exclusive('a', 'a'));
    }

    @Test
    void inclusiveIteratesCorrectly() {
        assertThat(CharRange.inclusive('a', 'j'), contains('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'));
        assertThat(CharRange.inclusive((char) (Character.MAX_VALUE - 4), Character.MAX_VALUE), contains((char) (Character.MAX_VALUE - 4),
                (char) (Character.MAX_VALUE - 3), (char) (Character.MAX_VALUE - 2), (char) (Character.MAX_VALUE - 1), Character.MAX_VALUE));
        assertThat(CharRange.inclusive(Character.MIN_VALUE, (char) (Character.MIN_VALUE + 4)), contains(Character.MIN_VALUE,
                (char) (Character.MIN_VALUE + 1), (char) (Character.MIN_VALUE + 2), (char) (Character.MIN_VALUE + 3), (char) (Character.MIN_VALUE + 4)));
    }

    @Test
    void exclusiveIteratesCorrectly() {
        assertThat(CharRange.exclusive('a', 'j'), contains('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'));
        assertThat(CharRange.exclusive((char) (Character.MAX_VALUE - 4), Character.MAX_VALUE), contains((char) (Character.MAX_VALUE - 4),
                (char) (Character.MAX_VALUE - 3), (char) (Character.MAX_VALUE - 2), (char) (Character.MAX_VALUE - 1)));
        assertThat(CharRange.exclusive(Character.MIN_VALUE, (char) (Character.MIN_VALUE + 4)), contains(Character.MIN_VALUE,
                (char) (Character.MIN_VALUE + 1), (char) (Character.MIN_VALUE + 2), (char) (Character.MIN_VALUE + 3)));
    }

    @Test
    void equality() {
        assertEquals(CharRange.inclusive('a', 'y'), CharRange.exclusive('a', 'z'));
        assertEquals(CharRange.from('a').to('z'), CharRange.inclusive('a', 'z'));
        assertEquals(CharRange.from('a').until('z'), CharRange.exclusive('a', 'z'));
    }
}
