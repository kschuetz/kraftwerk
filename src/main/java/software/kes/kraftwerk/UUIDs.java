package software.kes.kraftwerk;

import java.util.UUID;

final class UUIDs {
    private UUIDs() {
    }

    static Generator<UUID> generateUUID() {
        return Generators.generateProduct(
                Generators.generateLong(),
                Generators.generateLong(),
                Generators.chooseOneOfValues('8', '9', 'a', 'b'),
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
