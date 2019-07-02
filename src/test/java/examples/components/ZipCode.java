package examples.components;

import dev.marksman.composablerandom.Generate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.Generate.*;
import static dev.marksman.composablerandom.domain.Characters.numeric;
import static dev.marksman.composablerandom.frequency.FrequencyMap.frequencyMap;

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
        static Generate<String> fiveDigits = generateStringFromCharacters(5, numeric());
        static Generate<String> fourDigits = generateStringFromCharacters(4, numeric());

        static Generate<ZipCode> zipCode =
                frequencyMap(7, fiveDigits)
                        .add(1, generateString(fiveDigits, constant("-"), fourDigits))
                        .toGenerate()
                        .fmap(ZipCode::zipCode);
    }

    public static Generate<ZipCode> generateZipCode() {
        return generators.zipCode;
    }
}
