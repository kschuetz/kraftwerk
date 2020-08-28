package examples.components;

import dev.marksman.kraftwerk.Generator;

import static dev.marksman.kraftwerk.Generators.chooseOneOfWeightedValues;
import static dev.marksman.kraftwerk.Weighted.weighted;

public final class UsState {
    private final String code;

    private UsState(String code) {
        this.code = code;
    }

    public static UsState usState(String code) {
        return new UsState(code);
    }

    public static Generator<UsState> generateUsState() {
        return generators.usState;
    }

    public static void main(String[] args) {
        generateUsState()
                .fmap(UsState::getCode)
                .run()
                .take(100)
                .forEach(System.out::println);
    }

    public String pretty() {
        return code;
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsState usState = (UsState) o;

        return code.equals(usState.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "UsState{" +
                "code='" + code + '\'' +
                '}';
    }

    private static class generators {
        private static final Generator<UsState> usState = chooseOneOfWeightedValues(
                weighted(39, "CA"), weighted(28, "TX"), weighted(21, "FL"),
                weighted(19, "NY"), weighted(12, "PA"), weighted(12, "IL"),
                weighted(11, "OH"), weighted(10, "GA"), weighted(10, "NC"),
                weighted(9, "MI"), weighted(8, "NJ"), weighted(8, "VA"),
                weighted(7, "WA"), weighted(7, "AZ"), weighted(6, "MA"),
                weighted(6, "TN"), weighted(6, "IN"), weighted(6, "MO"),
                weighted(6, "MD"), weighted(5, "WI"), weighted(5, "CO"),
                weighted(5, "MN"), weighted(5, "SC"), weighted(4, "AL"),
                weighted(4, "LA"), weighted(4, "KY"), weighted(4, "OR"),
                weighted(3, "OK"), weighted(3, "CT"), weighted(3, "UT"),
                weighted(3, "IA"), weighted(3, "NV"), weighted(3, "AR"),
                weighted(2, "MS"), weighted(2, "KS"), weighted(2, "NM"),
                weighted(1, "WV"), weighted(1, "NE"), weighted(1, "ID"),
                weighted(1, "HI"), weighted(1, "NH"), weighted(1, "ME"),
                weighted(1, "MO"), weighted(1, "RI"), weighted(1, "DE"),
                weighted(1, "SD"), weighted(1, "ND"), weighted(1, "AK"),
                weighted(1, "VT"), weighted(1, "WY"))
                .fmap(UsState::usState);
    }
}
