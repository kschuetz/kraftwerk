package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.Instruction;

import static dev.marksman.composablerandom.Generator.generator;

class Primitives {

    private static final Generator<Boolean> GENERATE_BOOLEAN = generator(Instruction.nextBoolean());
    private static final Generator<Double> GENERATE_DOUBLE = generator(Instruction.nextDouble());
    private static final Generator<Float> GENERATE_FLOAT = generator(Instruction.nextFloat());
    private static final Generator<Integer> GENERATE_INTEGER = generator(Instruction.nextInt());
    private static final Generator<Long> GENERATE_LONG = generator(Instruction.nextLong());
    private static final Generator<Double> GENERATE_GAUSSIAN = generator(Instruction.nextGaussian());

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
        return generator(Instruction.nextIntBetween(min, max));
    }

    static Generator<Integer> generateIntExclusive(int bound) {
        return generator(Instruction.nextIntBounded(bound));
    }

    static Generator<Integer> generateIntExclusive(int origin, int bound) {
        return generator(Instruction.nextIntExclusive(origin, bound));
    }

    static Generator<Integer> generateIntIndex(int bound) {
        return generator(Instruction.nextIntIndex(bound));
    }

    static Generator<Float> generateFloat() {
        return GENERATE_FLOAT;
    }

    static Generator<Long> generateLong() {
        return GENERATE_LONG;
    }

    static Generator<Long> generateLong(long min, long max) {
        return generator(Instruction.nextLongBetween(min, max));
    }

    static Generator<Long> generateLongExclusive(long bound) {
        return generator(Instruction.nextLongBounded(bound));
    }

    static Generator<Long> generateLongExclusive(long origin, long bound) {
        return generator(Instruction.nextLongExclusive(origin, bound));
    }

    static Generator<Long> generateLongIndex(long bound) {
        return generator(Instruction.nextLongIndex(bound));
    }

    static Generator<Double> generateGaussian() {
        return GENERATE_GAUSSIAN;
    }

    static Generator<Byte[]> generateBytes(int count) {
        return generator(Instruction.nextBytes(count));
    }

    static Generator<Byte> generateByte() {
        return GENERATE_BYTE;
    }

    static Generator<Short> generateShort() {
        return GENERATE_SHORT;
    }
}
