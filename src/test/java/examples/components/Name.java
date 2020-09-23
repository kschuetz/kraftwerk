package examples.components;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.domain.Characters;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;
import static dev.marksman.kraftwerk.weights.MaybeWeights.nothings;
import static java.util.Arrays.asList;

public final class Name {
    private final String first;
    private final Maybe<String> middle;
    private final String last;
    private final Maybe<String> suffix;

    private Name(String first, Maybe<String> middle, String last, Maybe<String> suffix) {
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.suffix = suffix;
    }

    public static Name name(String first, Maybe<String> middle, String last, Maybe<String> suffix) {
        return new Name(first, middle, last, suffix);
    }

    public static Generator<Name> generateName() {
        return generators.name;
    }

    public static void main(String[] args) {
        Generators.generateNonEmptyMap(Generators.generateInt(IntRange.from(0).to(255)), generateName().fmap(Name::pretty))
                .run()
                .take(100)
                .forEach(System.out::println);
    }

    public String pretty() {
        return first +
                middle.match(__ -> "", s -> " " + s)
                + " " + last
                + suffix.match(__ -> "", s -> ", " + s);
    }

    public String getFirst() {
        return this.first;
    }

    public Maybe<String> getMiddle() {
        return this.middle;
    }

    public String getLast() {
        return this.last;
    }

    public Maybe<String> getSuffix() {
        return this.suffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Name name = (Name) o;

        if (!first.equals(name.first)) return false;
        if (!middle.equals(name.middle)) return false;
        if (!last.equals(name.last)) return false;
        return suffix.equals(name.suffix);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + middle.hashCode();
        result = 31 * result + last.hashCode();
        result = 31 * result + suffix.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Name{" +
                "first='" + first + '\'' +
                ", middle=" + middle +
                ", last='" + last + '\'' +
                ", suffix=" + suffix +
                '}';
    }

    private static class generators {
        static final Generator<String> initial = Generators.chooseOneValueFromDomain(Characters.alphaUpper()).fmap(c -> c + ".");

        static final Generator<String> givenNames =
                Generators.chooseOneOfValues("Alice", "Barbara", "Bart", "Billy", "Bobby", "Carol", "Cindy", "Elizabeth",
                        "Eric", "George", "Greg", "Homer", "James", "Jan", "John", "Kenny", "Kyle", "Linda", "Lisa",
                        "Maggie", "Marcia", "Marge", "Mary", "Mike", "Oliver", "Patricia", "Peter", "Stan");

        static final Generator<String> first =
                Generators.chooseOneOfWeighted(givenNames.weighted(15),
                        initial.weighted(1));

        static final Generator<String> middle =
                Generators.chooseOneOfWeighted(givenNames.weighted(1),
                        initial.weighted(5));

        static final Generator<String> last =
                Generators.chooseOneValueFromCollection(asList(
                        "Allen", "Anderson", "Brown", "Clark", "Davis", "Foobar", "Garcia", "Hall", "Harris",
                        "Hernandez", "Jackson", "Johnson", "Jones", "King", "Lee", "Lewis", "Lopez", "Martin",
                        "Martinez", "Miller", "Moore", "Qwerty", "Robinson", "Rodriguez", "Smith", "Taylor",
                        "Thomas", "Thompson", "Walker", "White", "Williams", "Wilson", "Wright", "Young"
                ));

        static final Generator<String> suffix = Generators.chooseOneOfValues("Jr.", "III", "Sr.");

        static final Generator<Name> name = Generators.tupled(
                first,
                middle.maybe(nothings(6).toJusts(1)),
                last,
                suffix.maybe(nothings(19).toJusts(1))
        ).fmap(into4(Name::name));

    }

}
