package dev.marksman.kraftwerk;

import java.util.UUID;

import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateProduct;

final class UUIDs {
    private UUIDs() {
    }

    static Generator<UUID> generateUUID() {
        return generateProduct(
                generateLong(),
                generateLong(),
                chooseOneOfValues('8', '9', 'a', 'b'),
                (Long l1, Long l2, Character y) -> {
                    String s = new UUID(l1, l2).toString();
                    return UUID.fromString(s.substring(0, 14) +
                            '4' +
                            s.substring(15, 19) +
                            y +
                            s.substring(20));
                });
    }
}
