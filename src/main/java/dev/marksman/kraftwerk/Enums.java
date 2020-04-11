package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.Vector;

import static dev.marksman.kraftwerk.Choose.chooseOneFromDomain;

class Enums {

    static <A extends Enum<A>> Generator<A> generateFromEnum(Class<A> enumType) {
        A[] enumConstants = enumType.getEnumConstants();
        if (enumConstants == null || enumConstants.length == 0) {
            throw new IllegalArgumentException("Class " + enumType + " has no enum constants");
        }
        return chooseOneFromDomain(Vector.copyFrom(enumConstants).toNonEmptyOrThrow());
    }

}
