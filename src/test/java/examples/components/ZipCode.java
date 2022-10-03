package examples.components;

import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Generators;

import static software.kes.kraftwerk.domain.Characters.numeric;
import static software.kes.kraftwerk.frequency.FrequencyMap.frequencyMap;

public final class ZipCode {
    private final String value;

    private ZipCode(String value) {
        this.value = value;
    }

    public static ZipCode zipCode(String value) {
        return new ZipCode(value);
    }

    public static Generator<ZipCode> generateZipCode() {
        return generators.zipCode;
    }

    public String pretty() {
        return value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ZipCode zipCode = (ZipCode) o;

        return value.equals(zipCode.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "ZipCode{" +
                "value='" + value + '\'' +
                '}';
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
}
