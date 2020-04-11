package examples.components;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.constraints.IntRange;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static dev.marksman.kraftwerk.weights.MaybeWeights.nothings;
import static examples.components.City.generateCity;
import static examples.components.Street.generateStreet;
import static examples.components.UsState.generateUsState;
import static examples.components.ZipCode.generateZipCode;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {
    String number;
    Street street;
    Maybe<String> unit;
    City city;
    UsState state;
    ZipCode zipCode;

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
                frequencyMap(3, Generators.generateInt(IntRange.from(0).to(990)).fmap(n -> 100 + 10 * n))
                        .add(3, Generators.generateInt(IntRange.from(0).to(990)).fmap(n -> 101 + 10 * n))
                        .add(4, Generators.generateInt(IntRange.from(10).to(999)))
                        .toGenerator()
                        .fmap(Object::toString);


        static Generator<String> unit =
                Generators.tupled(Generators.chooseOneOfValues(" #", ", Apt. ", ", Suite "),
                        Generators.chooseOneOf(Generators.generateInt(IntRange.from(100).to(3000)), Generators.generateInt(IntRange.from(1).to(99))))
                        .fmap(into((unitName, number) -> unitName + number));

        static Generator<Address> address =
                Generators.product(number,
                        generateStreet(),
                        unit.maybe(nothings(4).toJusts(1)),
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
