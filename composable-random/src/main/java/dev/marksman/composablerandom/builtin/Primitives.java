package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.Generator;

class Primitives {

    private static final Generator<Boolean> GENERATE_BOOLEAN = Generator.nextBoolean();
    private static final Generator<Double> GENERATE_DOUBLE = Generator.nextDouble();
    private static final Generator<Float> GENERATE_FLOAT = Generator.nextFloat();
    private static final Generator<Integer> GENERATE_INTEGER = Generator.nextInt();
    private static final Generator<Long> GENERATE_LONG = Generator.nextLong();
    private static final Generator<Double> GENERATE_GAUSSIAN = Generator.nextGaussian();

    private static final Generator<Byte> GENERATE_BYTE = GENERATE_INTEGER.fmap(Integer::byteValue);
    private static final Generator<Short> GENERATE_SHORT = GENERATE_INTEGER.fmap(Integer::shortValue);

    static Generator<Boolean> generateBoolean() {
        return GENERATE_BOOLEAN;
    }

    static Generator<Double> generateDouble() {
        return GENERATE_DOUBLE;
    }

    static Generator<Integer> generateInt() {
        return GENERATE_INTEGER;
    }

    static Generator<Integer> generateInt(int min, int max) {
        return Generator.nextIntBetween(min, max);
    }

    static Generator<Integer> generateIntExclusive(int bound) {
        return Generator.nextIntBounded(bound);
    }

    static Generator<Integer> generateIntExclusive(int origin, int bound) {
        return Generator.nextIntExclusive(origin, bound);
    }

    static Generator<Integer> generateIntIndex(int bound) {
        return Generator.nextIntIndex(bound);
    }

    static Generator<Float> generateFloat() {
        return GENERATE_FLOAT;
    }

    static Generator<Long> generateLong() {
        return GENERATE_LONG;
    }

    static Generator<Long> generateLong(long min, long max) {
        return Generator.nextLongBetween(min, max);
    }

    static Generator<Long> generateLongExclusive(long bound) {
        return Generator.nextLongBounded(bound);
    }

    static Generator<Long> generateLongExclusive(long origin, long bound) {
        return Generator.nextLongExclusive(origin, bound);
    }

    static Generator<Long> generateLongIndex(long bound) {
        return Generator.nextLongIndex(bound);
    }

    static Generator<Double> generateGaussian() {
        return GENERATE_GAUSSIAN;
    }

    static Generator<Byte[]> generateBytes(int count) {
        return Generator.nextBytes(count);
    }

    static Generator<Byte> generateByte() {
        return GENERATE_BYTE;
    }

    static Generator<Short> generateShort() {
        return GENERATE_SHORT;
    }
}
