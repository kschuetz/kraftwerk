package dev.marksman.composablerandom.examples;

import dev.marksman.composablerandom.OldGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.legacy.OldFrequencyEntry.entry;
import static dev.marksman.composablerandom.legacy.builtin.Generators.frequency;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsState {
    private final String code;

    public static UsState usState(String code) {
        return new UsState(code);
    }

    private static class Generators {
        private static final OldGenerator<UsState> usState = frequency(
                entry(39, "CA"), entry(28, "TX"), entry(21, "FL"),
                entry(19, "NY"), entry(12, "PA"), entry(12, "IL"),
                entry(11, "OH"), entry(10, "GA"), entry(10, "NC"),
                entry(9, "MI"), entry(8, "NJ"), entry(8, "VA"),
                entry(7, "WA"), entry(7, "AZ"), entry(6, "MA"),
                entry(6, "TN"), entry(6, "IN"), entry(6, "MO"),
                entry(6, "MD"), entry(5, "WI"), entry(5, "CO"),
                entry(5, "MN"), entry(5, "SC"), entry(4, "AL"),
                entry(4, "LA"), entry(4, "KY"), entry(4, "OR"),
                entry(3, "OK"), entry(3, "CT"), entry(3, "UT"),
                entry(3, "IA"), entry(3, "NV"), entry(3, "AR"),
                entry(2, "MS"), entry(2, "KS"), entry(2, "NM"),
                entry(1, "WV"), entry(1, "NE"), entry(1, "ID"),
                entry(1, "HI"), entry(1, "NH"), entry(1, "ME"),
                entry(1, "MO"), entry(1, "RI"), entry(1, "DE"),
                entry(1, "SD"), entry(1, "ND"), entry(1, "AK"),
                entry(1, "VT"), entry(1, "WY"))
                .fmap(UsState::usState);
    }

    public static OldGenerator<UsState> generateUsState() {
        return Generators.usState;
    }

    public static void main(String[] args) {
        streamFrom(generateUsState().fmap(UsState::getCode)).next(100).forEach(System.out::println);
    }
}
