package dev.marksman.composablerandom.examples.components;

import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.Generator.*;
import static dev.marksman.composablerandom.domain.Characters.digits;
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
        static Generator<String> fiveDigits = generateStringFromCharacters(5, digits());
        static Generator<String> fourDigits = generateStringFromCharacters(4, digits());

        static Generator<ZipCode> zipCode =
                frequencyMap(7, fiveDigits)
                        .add(1, generateString(fiveDigits, constant("-"), fourDigits))
                        .toGenerator()
                        .fmap(ZipCode::zipCode);
    }

    public static Generator<ZipCode> generateZipCode() {
        return generators.zipCode;
    }
}
