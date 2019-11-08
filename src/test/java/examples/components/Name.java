package examples.components;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.domain.Characters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;
import static dev.marksman.kraftwerk.FrequencyEntry.entry;
import static dev.marksman.kraftwerk.GeneratedStream.streamFrom;
import static dev.marksman.kraftwerk.MaybeWeights.nothingWeight;
import static java.util.Arrays.asList;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Name {
    private final String first;
    private final Maybe<String> middle;
    private final String last;
    private final Maybe<String> suffix;

    public String pretty() {
        return first +
                middle.match(__ -> "", s -> " " + s)
                + " " + last
                + suffix.match(__ -> "", s -> ", " + s);
    }

    public static Name name(String first, Maybe<String> middle, String last, Maybe<String> suffix) {
        return new Name(first, middle, last, suffix);
    }

    private static class generators {
        static final Generator<String> initial = Generators.chooseOneFromDomain(Characters.alphaUpper()).fmap(c -> c + ".");

        static final Generator<String> givenNames =
                Generators.chooseOneOfValues("Alice", "Barbara", "Bart", "Billy", "Bobby", "Carol", "Cindy", "Elizabeth",
                        "Eric", "George", "Greg", "Homer", "James", "Jan", "John", "Kenny", "Kyle", "Linda", "Lisa",
                        "Maggie", "Marcia", "Marge", "Mary", "Mike", "Oliver", "Patricia", "Peter", "Stan");

        static final Generator<String> first =
                Generators.frequency(entry(15, givenNames),
                        entry(1, initial));

        static final Generator<String> middle =
                Generators.frequency(entry(1, givenNames),
                        entry(5, initial));

        static final Generator<String> last =
                Generators.chooseOneFromCollection(asList(
                        "Allen", "Anderson", "Brown", "Clark", "Davis", "Foobar", "Garcia", "Hall", "Harris",
                        "Hernandez", "Jackson", "Johnson", "Jones", "King", "Lee", "Lewis", "Lopez", "Martin",
                        "Martinez", "Miller", "Moore", "Qwerty", "Robinson", "Rodriguez", "Smith", "Taylor",
                        "Thomas", "Thompson", "Walker", "White", "Williams", "Wilson", "Wright", "Young"
                ));

        static final Generator<String> suffix = Generators.chooseOneOfValues("Jr.", "III", "Sr.");

        static final Generator<Name> name = Generators.tupled(
                first,
                middle.maybe(nothingWeight(6).toJust(1)),
                last,
                suffix.maybe(nothingWeight(19).toJust(1))
        ).fmap(into4(Name::name));

    }

    public static Generator<Name> generateName() {
        return generators.name;
    }

    public static void main(String[] args) {
        streamFrom(Generators.generateNonEmptyMap(Generators.generateInt(0, 255), generateName().fmap(Name::pretty)))
                .next(100).forEach(System.out::println);
    }

}
