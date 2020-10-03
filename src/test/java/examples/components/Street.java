package examples.components;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.IntRange;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.chooseOneOfWeighted;
import static dev.marksman.kraftwerk.Generators.chooseOneOfWeightedValues;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateTuple;
import static dev.marksman.kraftwerk.Weighted.weighted;
import static dev.marksman.kraftwerk.weights.MaybeWeights.nothings;
import static examples.components.City.generateCityRootName;

public final class Street {
    private final Maybe<String> compass;
    private final String name;
    private final String suffix;

    private Street(Maybe<String> compass, String name, String suffix) {
        this.compass = compass;
        this.name = name;
        this.suffix = suffix;
    }

    public String pretty() {
        return compass.match(__ -> "", s -> s + " ")
                + name
                + " " + suffix;
    }

    public static Street street(Maybe<String> compass, String name, String suffix) {
        return new Street(compass, name, suffix);
    }

    public Maybe<String> getCompass() {
        return this.compass;
    }

    public String getName() {
        return this.name;
    }

    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Street street = (Street) o;

        if (!compass.equals(street.compass)) return false;
        if (!name.equals(street.name)) return false;
        return suffix.equals(street.suffix);
    }

    @Override
    public int hashCode() {
        int result = compass.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + suffix.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Street{" +
                "compass=" + compass +
                ", name='" + name + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }

    private static class generators {
        static final Generator<String> compass =
                chooseOneOfWeighted(chooseOneOfValues("N.", "S.", "W.", "E.").weighted(8),
                        chooseOneOfValues("NW", "NE", "SW", "SE").weighted(1));

        static final Generator<String> ordinal =
                generateInt(IntRange.from(1).to(99)).fmap(n -> {
                    if (n == 11) return "11th";
                    else if (n % 10 == 1) return n + "st";
                    else if (n % 10 == 2) return n + "nd";
                    else if (n % 10 == 3) return n + "rd";
                    else return n + "th";
                });

        static final Generator<String> president =
                chooseOneOfValues("Washington", "Adams", "Jefferson", "Madison", "Monroe", "Lincoln");

        static final Generator<String> tree =
                chooseOneOfValues("Oak", "Maple", "Elm", "Pine", "Spruce", "Sycamore", "Birch", "Apple", "Peach");

        static final Generator<String> suffix =
                chooseOneOfWeightedValues(weighted(10, "St."),
                        weighted(7, "Ave."),
                        weighted(5, "Rd."),
                        weighted(3, "Dr."),
                        weighted(3, "La."),
                        weighted(2, "Blvd."),
                        weighted(1, "Ct."));

        static final Generator<String> name =
                chooseOneOfWeighted(weighted(3, ordinal),
                        weighted(2, tree),
                        weighted(2, president),
                        weighted(2, generateCityRootName()));

        static final Generator<Street> street = generateTuple(
                compass.maybe(nothings(3).toJusts(1)),
                name,
                suffix)
                .fmap(into3(Street::street));
    }

    public static Generator<Street> generateStreet() {
        return generators.street;
    }

    public static void main(String[] args) {
        generateStreet()
                .fmap(Street::pretty)
                .run()
                .take(100)
                .forEach(System.out::println);
    }
}
