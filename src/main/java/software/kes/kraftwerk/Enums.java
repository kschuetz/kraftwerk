package software.kes.kraftwerk;

import software.kes.collectionviews.Vector;

import static software.kes.kraftwerk.Choose.chooseOneValueFromDomain;

final class Enums {
    private Enums() {
    }

    static <A extends Enum<A>> Generator<A> generateFromEnum(Class<A> enumType) {
        A[] enumConstants = enumType.getEnumConstants();
        if (enumConstants == null || enumConstants.length == 0) {
            throw new IllegalArgumentException("Class " + enumType + " has no enum constants");
        }
        return chooseOneValueFromDomain(Vector.copyFrom(enumConstants).toNonEmptyOrThrow());
    }
}
