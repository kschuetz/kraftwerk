package examples.components;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.domain.Characters.numeric;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ZipCode {
    private final String value;

    public static ZipCode zipCode(String value) {
        return new ZipCode(value);
    }

    public String pretty() {
        return value;
    }

    private static class generators {
        static Generator<String> fiveDigits = Generators.generateStringFromCharacters(5, numeric());
        static Generator<String> fourDigits = Generators.generateStringFromCharacters(4, numeric());

        static Generator<ZipCode> zipCode =
                frequencyMap(fiveDigits.weighted(7))
                        .add(Generators.generateString(fiveDigits, Generators.constant("-"), fourDigits))
                        .toGenerator()
                        .fmap(ZipCode::zipCode);
    }

    public static Generator<ZipCode> generateZipCode() {
        return generators.zipCode;
    }
}
