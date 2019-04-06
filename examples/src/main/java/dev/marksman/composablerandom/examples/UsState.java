package dev.marksman.composablerandom.examples;

import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.builtin.Generators.frequency;
import static dev.marksman.composablerandom.legacy.OldGeneratedStream.streamFrom;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsState {
    private final String code;

    public static UsState usState(String code) {
        return new UsState(code);
    }

    private static class Generators {
        private static final Generator<UsState> usState = frequency(
                FrequencyEntry.entryForValue(39, "CA"), FrequencyEntry.entryForValue(28, "TX"), FrequencyEntry.entryForValue(21, "FL"),
                FrequencyEntry.entryForValue(19, "NY"), FrequencyEntry.entryForValue(12, "PA"), FrequencyEntry.entryForValue(12, "IL"),
                FrequencyEntry.entryForValue(11, "OH"), FrequencyEntry.entryForValue(10, "GA"), FrequencyEntry.entryForValue(10, "NC"),
                FrequencyEntry.entryForValue(9, "MI"), FrequencyEntry.entryForValue(8, "NJ"), FrequencyEntry.entryForValue(8, "VA"),
                FrequencyEntry.entryForValue(7, "WA"), FrequencyEntry.entryForValue(7, "AZ"), FrequencyEntry.entryForValue(6, "MA"),
                FrequencyEntry.entryForValue(6, "TN"), FrequencyEntry.entryForValue(6, "IN"), FrequencyEntry.entryForValue(6, "MO"),
                FrequencyEntry.entryForValue(6, "MD"), FrequencyEntry.entryForValue(5, "WI"), FrequencyEntry.entryForValue(5, "CO"),
                FrequencyEntry.entryForValue(5, "MN"), FrequencyEntry.entryForValue(5, "SC"), FrequencyEntry.entryForValue(4, "AL"),
                FrequencyEntry.entryForValue(4, "LA"), FrequencyEntry.entryForValue(4, "KY"), FrequencyEntry.entryForValue(4, "OR"),
                FrequencyEntry.entryForValue(3, "OK"), FrequencyEntry.entryForValue(3, "CT"), FrequencyEntry.entryForValue(3, "UT"),
                FrequencyEntry.entryForValue(3, "IA"), FrequencyEntry.entryForValue(3, "NV"), FrequencyEntry.entryForValue(3, "AR"),
                FrequencyEntry.entryForValue(2, "MS"), FrequencyEntry.entryForValue(2, "KS"), FrequencyEntry.entryForValue(2, "NM"),
                FrequencyEntry.entryForValue(1, "WV"), FrequencyEntry.entryForValue(1, "NE"), FrequencyEntry.entryForValue(1, "ID"),
                FrequencyEntry.entryForValue(1, "HI"), FrequencyEntry.entryForValue(1, "NH"), FrequencyEntry.entryForValue(1, "ME"),
                FrequencyEntry.entryForValue(1, "MO"), FrequencyEntry.entryForValue(1, "RI"), FrequencyEntry.entryForValue(1, "DE"),
                FrequencyEntry.entryForValue(1, "SD"), FrequencyEntry.entryForValue(1, "ND"), FrequencyEntry.entryForValue(1, "AK"),
                FrequencyEntry.entryForValue(1, "VT"), FrequencyEntry.entryForValue(1, "WY"))
                .fmap(UsState::usState);
    }

    public static Generator<UsState> generateUsState() {
        return Generators.usState;
    }

    public static void main(String[] args) {
        streamFrom(generateUsState().fmap(UsState::getCode)).next(100).forEach(System.out::println);
    }
}
