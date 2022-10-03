package software.kes.kraftwerk.constraints;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateRangeTest {
    @Test
    void shouldThrowWithIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> LocalDateRange.inclusive(LocalDate.of(2020, 4, 28), LocalDate.of(2020, 4, 27)));
        assertThrows(IllegalArgumentException.class, () -> LocalDateRange.exclusive(LocalDate.of(2020, 4, 28), LocalDate.of(2020, 4, 28)));
    }

    @Test
    void inclusiveIteratesCorrectly() {
        assertThat(LocalDateRange.inclusive(LocalDate.of(2020, 4, 28), LocalDate.of(2020, 4, 28)),
                contains(LocalDate.of(2020, 4, 28)));
        assertThat(LocalDateRange.inclusive(LocalDate.of(2020, 4, 28), LocalDate.of(2020, 5, 3)),
                contains(LocalDate.of(2020, 4, 28),
                        LocalDate.of(2020, 4, 29),
                        LocalDate.of(2020, 4, 30),
                        LocalDate.of(2020, 5, 1),
                        LocalDate.of(2020, 5, 2),
                        LocalDate.of(2020, 5, 3)));
    }

    @Test
    void exclusiveIteratesCorrectly() {
        assertThat(LocalDateRange.exclusive(LocalDate.of(2020, 4, 28), LocalDate.of(2020, 4, 29)),
                contains(LocalDate.of(2020, 4, 28)));
        assertThat(LocalDateRange.exclusive(LocalDate.of(2020, 4, 28), LocalDate.of(2020, 5, 3)),
                contains(LocalDate.of(2020, 4, 28),
                        LocalDate.of(2020, 4, 29),
                        LocalDate.of(2020, 4, 30),
                        LocalDate.of(2020, 5, 1),
                        LocalDate.of(2020, 5, 2)));
    }

    @Test
    void equality() {
        assertEquals(LocalDateRange.inclusive(LocalDate.of(2020, 4, 28),
                LocalDate.of(2020, 5, 3)),
                LocalDateRange.exclusive(LocalDate.of(2020, 4, 28), LocalDate.of(2020, 5, 4)));
        assertEquals(LocalDateRange.from(LocalDate.of(2020, 4, 28)).to(LocalDate.of(2020, 5, 3)), LocalDateRange.inclusive(LocalDate.of(2020, 4, 28), LocalDate.of(2020, 5, 3)));
        assertEquals(LocalDateRange.from(LocalDate.of(2020, 4, 28)).until(LocalDate.of(2020, 5, 3)), LocalDateRange.exclusive(LocalDate.of(2020, 4, 28), LocalDate.of(2020, 5, 3)));
    }
}
