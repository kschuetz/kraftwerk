package examples.components;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static dev.marksman.composablerandom.Generator.*;
import static dev.marksman.composablerandom.MaybeWeights.nothingWeight;
import static dev.marksman.composablerandom.frequency.FrequencyMap.frequencyMap;
import static examples.components.City.generateCity;
import static examples.components.Street.generateStreet;
import static examples.components.UsState.generateUsState;
import static examples.components.ZipCode.generateZipCode;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {
    private final String number;
    private final Street street;
    private final Maybe<String> unit;
    private final City city;
    private final UsState state;
    private final ZipCode zipCode;

    public String prettyMultiLine() {
        return renderPretty(true);
    }

    public String prettySingleLine() {
        return renderPretty(false);
    }

    private String renderPretty(boolean multiLine) {
        return number +
                " " +
                street.pretty() +
                unit.orElse("") +
                (multiLine ? "\n" : "; ") +
                city.pretty() +
                ", " +
                state.pretty() +
                " " +
                zipCode.pretty();
    }

    private static class generators {
        static Generator<String> number =
                frequencyMap(3, generateInt(0, 990).fmap(n -> 100 + 10 * n))
                        .add(3, generateInt(0, 990).fmap(n -> 101 + 10 * n))
                        .add(4, generateInt(10, 999))
                        .toGenerator()
                        .fmap(Object::toString);


        static Generator<String> unit =
                tupled(chooseOneOfValues(" #", ", Apt. ", ", Suite "),
                        chooseOneOf(generateInt(100, 3000), generateInt(1, 99)))
                        .fmap(into((unitName, number) -> unitName + number));

        static Generator<Address> address =
                product(number,
                        generateStreet(),
                        unit.maybe(nothingWeight(4).toJust(1)),
                        generateCity(),
                        generateUsState(),
                        generateZipCode(),
                        Address::address);
    }

    public static Generator<Address> generateAddress() {
        return generators.address;
    }

    public static Address address(String number, Street street, Maybe<String> unit,
                                  City city, UsState state, ZipCode zipCode) {
        return new Address(number, street, unit, city, state, zipCode);
    }
}
