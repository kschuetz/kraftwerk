package dev.marksman.nonempty;

class Util {
    static void validateBounds(int size, int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index must be >= 0");
        } else if (index >= size) {
            throw new IndexOutOfBoundsException("Index exceeds size");
        }
    }
}
