package dev.marksman.composablerandom.examples;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.composablerandom.OldGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.legacy.OldFrequencyEntry.entry;
import static dev.marksman.composablerandom.legacy.builtin.OldGenerators.*;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Street {
    private final Maybe<String> compass;
    private final String name;
    private final String suffix;

    public String getFullName() {
        return compass.match(__ -> "", s -> s + " ")
                + name
                + " " + suffix;
    }

    public static final Street street(Maybe<String> compass, String name, String suffix) {
        return new Street(compass, name, suffix);
    }

    private static class Generators {
        private static final OldGenerator<String> compass =
                frequency(entry(8, chooseOneOf("N.", "S.", "W.", "E.")),
                        entry(1, chooseOneOf("NW", "NE", "SW", "SE")));

        private static final OldGenerator<String> ordinal =
                generateInt(1, 99).fmap(n -> {
                    if (n == 11) return "11th";
                    else if (n % 10 == 1) return n + "st";
                    else if (n % 10 == 2) return n + "nd";
                    else if (n % 10 == 3) return n + "rd";
                    else return n + "th";
                });

        private static final OldGenerator<String> president =
                chooseOneOf("Washington", "Adams", "Jefferson", "Madison", "Monroe", "Lincoln");

        private static final OldGenerator<String> tree =
                chooseOneOf("Oak", "Maple", "Elm", "Pine", "Spruce", "Sycamore", "Birch", "Apple", "Peach");

        private static final OldGenerator<String> suffix =
                frequency(entry(10, "St."),
                        entry(7, "Ave."),
                        entry(5, "Rd."),
                        entry(3, "Dr."),
                        entry(3, "La."),
                        entry(2, "Blvd."),
                        entry(1, "Ct."));

        private static final OldGenerator<String> name =
                frequency(entry(3, ordinal),
                        entry(2, tree),
                        entry(2, president));

        private static final OldGenerator<Street> street = tupled(
                generateMaybe(3, 1, compass),
                name,
                suffix)
                .fmap(into3(Street::street));
    }

    public static OldGenerator<Street> generateStreet() {
        return Generators.street;
    }

    public static void main(String[] args) {
        streamFrom(generateStreet().fmap(Street::getFullName)).next(100).forEach(System.out::println);
    }
}
