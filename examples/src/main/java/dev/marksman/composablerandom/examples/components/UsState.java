package dev.marksman.composablerandom.examples.components;

import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.FrequencyEntry.entryForValue;
import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.Generator.frequency;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsState {
    private final String code;

    public static UsState usState(String code) {
        return new UsState(code);
    }

    public String pretty() {
        return code;
    }

    private static class generators {
        private static final Generator<UsState> usState = frequency(
                entryForValue(39, "CA"), entryForValue(28, "TX"), entryForValue(21, "FL"),
                entryForValue(19, "NY"), entryForValue(12, "PA"), entryForValue(12, "IL"),
                entryForValue(11, "OH"), entryForValue(10, "GA"), entryForValue(10, "NC"),
                entryForValue(9, "MI"), entryForValue(8, "NJ"), entryForValue(8, "VA"),
                entryForValue(7, "WA"), entryForValue(7, "AZ"), entryForValue(6, "MA"),
                entryForValue(6, "TN"), entryForValue(6, "IN"), entryForValue(6, "MO"),
                entryForValue(6, "MD"), entryForValue(5, "WI"), entryForValue(5, "CO"),
                entryForValue(5, "MN"), entryForValue(5, "SC"), entryForValue(4, "AL"),
                entryForValue(4, "LA"), entryForValue(4, "KY"), entryForValue(4, "OR"),
                entryForValue(3, "OK"), entryForValue(3, "CT"), entryForValue(3, "UT"),
                entryForValue(3, "IA"), entryForValue(3, "NV"), entryForValue(3, "AR"),
                entryForValue(2, "MS"), entryForValue(2, "KS"), entryForValue(2, "NM"),
                entryForValue(1, "WV"), entryForValue(1, "NE"), entryForValue(1, "ID"),
                entryForValue(1, "HI"), entryForValue(1, "NH"), entryForValue(1, "ME"),
                entryForValue(1, "MO"), entryForValue(1, "RI"), entryForValue(1, "DE"),
                entryForValue(1, "SD"), entryForValue(1, "ND"), entryForValue(1, "AK"),
                entryForValue(1, "VT"), entryForValue(1, "WY"))
                .fmap(UsState::usState);
    }

    public static Generator<UsState> generateUsState() {
        return generators.usState;
    }

    public static void main(String[] args) {
        streamFrom(generateUsState().fmap(UsState::getCode)).next(100).forEach(System.out::println);
    }
}
