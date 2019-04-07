package dev.marksman.composablerandom.examples.generators;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into6.into6;
import static dev.marksman.composablerandom.Generator.generateInt;
import static dev.marksman.composablerandom.builtin.Generators.*;
import static dev.marksman.composablerandom.examples.generators.City.generateCity;
import static dev.marksman.composablerandom.examples.generators.Street.generateStreet;
import static dev.marksman.composablerandom.examples.generators.UsState.generateUsState;
import static dev.marksman.composablerandom.examples.generators.ZipCode.generateZipCode;
import static dev.marksman.composablerandom.frequency.FrequencyMap.frequencyMap;

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
                tupled(number,
                        generateStreet(),
                        generateMaybe(4, 1, unit),
                        generateCity(),
                        generateUsState(),
                        generateZipCode())
                        .fmap(into6(Address::address));
    }

    public static Generator<Address> generateAddress() {
        return generators.address;
    }

    public static Address address(String number, Street street, Maybe<String> unit,
                                  City city, UsState state, ZipCode zipCode) {
        return new Address(number, street, unit, city, state, zipCode);
    }
}
