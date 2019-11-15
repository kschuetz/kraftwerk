package examples.components;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static dev.marksman.kraftwerk.FrequencyEntry.entry;
import static dev.marksman.kraftwerk.FrequencyEntry.entryForValue;
import static dev.marksman.kraftwerk.GeneratedStream.streamFrom;
import static dev.marksman.kraftwerk.weights.MaybeWeights.nothingWeight;
import static examples.components.City.generateCityRootName;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Street {
    private final Maybe<String> compass;
    private final String name;
    private final String suffix;

    public String pretty() {
        return compass.match(__ -> "", s -> s + " ")
                + name
                + " " + suffix;
    }

    public static Street street(Maybe<String> compass, String name, String suffix) {
        return new Street(compass, name, suffix);
    }

    private static class generators {
        static final Generator<String> compass =
                Generators.frequency(entry(8, Generators.chooseOneOfValues("N.", "S.", "W.", "E.")),
                        entry(1, Generators.chooseOneOfValues("NW", "NE", "SW", "SE")));

        static final Generator<String> ordinal =
                Generators.generateInt(1, 99).fmap(n -> {
                    if (n == 11) return "11th";
                    else if (n % 10 == 1) return n + "st";
                    else if (n % 10 == 2) return n + "nd";
                    else if (n % 10 == 3) return n + "rd";
                    else return n + "th";
                });

        static final Generator<String> president =
                Generators.chooseOneOfValues("Washington", "Adams", "Jefferson", "Madison", "Monroe", "Lincoln");

        static final Generator<String> tree =
                Generators.chooseOneOfValues("Oak", "Maple", "Elm", "Pine", "Spruce", "Sycamore", "Birch", "Apple", "Peach");

        static final Generator<String> suffix =
                Generators.frequency(entryForValue(10, "St."),
                        entryForValue(7, "Ave."),
                        entryForValue(5, "Rd."),
                        entryForValue(3, "Dr."),
                        entryForValue(3, "La."),
                        entryForValue(2, "Blvd."),
                        entryForValue(1, "Ct."));

        static final Generator<String> name =
                Generators.frequency(entry(3, ordinal),
                        entry(2, tree),
                        entry(2, president),
                        entry(2, generateCityRootName()));

        static final Generator<Street> street = Generators.tupled(
                compass.maybe(nothingWeight(3).toJust(1)),
                name,
                suffix)
                .fmap(into3(Street::street));
    }

    public static Generator<Street> generateStreet() {
        return generators.street;
    }

    public static void main(String[] args) {
        streamFrom(generateStreet().fmap(Street::pretty)).next(100).forEach(System.out::println);
    }
}
