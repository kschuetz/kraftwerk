package examples.components;

import com.jnape.palatable.lambda.adt.Maybe;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.constraints.IntRange;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static examples.components.City.generateCity;
import static examples.components.Street.generateStreet;
import static examples.components.UsState.generateUsState;
import static examples.components.ZipCode.generateZipCode;
import static software.kes.kraftwerk.Generators.chooseOneOf;
import static software.kes.kraftwerk.Generators.chooseOneOfValues;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateProduct;
import static software.kes.kraftwerk.Generators.generateTuple;
import static software.kes.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static software.kes.kraftwerk.weights.MaybeWeights.nothings;

public final class Address {
    private final String number;
    private final Street street;
    private final Maybe<String> unit;
    private final City city;
    private final UsState state;
    private final ZipCode zipCode;

    private Address(String number, Street street, Maybe<String> unit, City city, UsState state, ZipCode zipCode) {
        this.number = number;
        this.street = street;
        this.unit = unit;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public static Generator<Address> generateAddress() {
        return generators.address;
    }

    public static Address address(String number, Street street, Maybe<String> unit,
                                  City city, UsState state, ZipCode zipCode) {
        return new Address(number, street, unit, city, state, zipCode);
    }

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

    public String getNumber() {
        return this.number;
    }

    public Street getStreet() {
        return this.street;
    }

    public Maybe<String> getUnit() {
        return this.unit;
    }

    public City getCity() {
        return this.city;
    }

    public UsState getState() {
        return this.state;
    }

    public ZipCode getZipCode() {
        return this.zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!number.equals(address.number)) return false;
        if (!street.equals(address.street)) return false;
        if (!unit.equals(address.unit)) return false;
        if (!city.equals(address.city)) return false;
        if (!state.equals(address.state)) return false;
        return zipCode.equals(address.zipCode);
    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + street.hashCode();
        result = 31 * result + unit.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + zipCode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
                "number='" + number + '\'' +
                ", street=" + street +
                ", unit=" + unit +
                ", city=" + city +
                ", state=" + state +
                ", zipCode=" + zipCode +
                '}';
    }

    private static class generators {
        static Generator<String> number =
                frequencyMap(generateInt(IntRange.from(0).to(990)).fmap(n -> 100 + 10 * n).weighted(3))
                        .add(generateInt(IntRange.from(0).to(990)).fmap(n -> 101 + 10 * n).weighted(3))
                        .add(generateInt(IntRange.from(10).to(999)).weighted(4))
                        .toGenerator()
                        .fmap(Object::toString);

        static Generator<String> unit =
                generateTuple(chooseOneOfValues(" #", ", Apt. ", ", Suite "),
                        chooseOneOf(generateInt(IntRange.from(100).to(3000)), generateInt(IntRange.from(1).to(99))))
                        .fmap(into((unitName, number) -> unitName + number));

        static Generator<Address> address =
                generateProduct(number,
                        generateStreet(),
                        unit.maybe(nothings(4).toJusts(1)),
                        generateCity(),
                        generateUsState(),
                        generateZipCode(),
                        Address::address);
    }
}
