package dev.marksman.composablerandom;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.Generator.generator;

public class Primitives {

    private static final Generator<Boolean> GENERATE_BOOLEAN = generator(Instruction.nextBoolean());
    private static final Generator<Double> GENERATE_DOUBLE = generator(Instruction.nextDouble());
    private static final Generator<Float> GENERATE_FLOAT = generator(Instruction.nextFloat());
    private static final Generator<Integer> GENERATE_INTEGER = generator(Instruction.nextInt());
    private static final Generator<Long> GENERATE_LONG = generator(Instruction.nextLong());
    private static final Generator<Double> GENERATE_GAUSSIAN = generator(Instruction.nextGaussian());

    private static final Generator<Byte> GENERATE_BYTE = GENERATE_INTEGER.fmap(Integer::byteValue);
    private static final Generator<Short> GENERATE_SHORT = GENERATE_INTEGER.fmap(Integer::shortValue);

    private static final Generator<Boolean> GENERATE_TRUE = constant(true);
    private static final Generator<Boolean> GENERATE_FALSE = constant(false);

    public static Generator<Boolean> generateBoolean() {
        return GENERATE_BOOLEAN;
    }

    public static Generator<Double> generateDouble() {
        return GENERATE_DOUBLE;
    }

    public static Generator<Integer> generateInt() {
        return GENERATE_INTEGER;
    }

    public static Generator<Integer> generateInt(int min, int max) {
        return generator(Instruction.nextIntBetween(min, max));
    }

    public static Generator<Integer> generateIntExclusive(int bound) {
        return generator(Instruction.nextIntBounded(bound));
    }

    public static Generator<Integer> generateIntExclusive(int origin, int bound) {
        return generator(Instruction.nextIntExclusive(origin, bound));
    }

    public static Generator<Integer> generateIntIndex(int bound) {
        return generator(Instruction.nextIntIndex(bound));
    }

    public static Generator<Float> generateFloat() {
        return GENERATE_FLOAT;
    }

    public static Generator<Long> generateLong() {
        return GENERATE_LONG;
    }

    public static Generator<Long> generateLong(long min, long max) {
        return generator(Instruction.nextLongBetween(min, max));
    }

    public static Generator<Long> generateLongExclusive(long bound) {
        return generator(Instruction.nextLongBounded(bound));
    }

    public static Generator<Long> generateLongExclusive(long origin, long bound) {
        return generator(Instruction.nextLongExclusive(origin, bound));
    }

    public static Generator<Double> generateGaussian() {
        return GENERATE_GAUSSIAN;
    }

    public static Generator<Byte[]> generateBytes(int count) {
        return generator(Instruction.nextBytes(count));
    }

    public static Generator<Byte> generateByte() {
        return GENERATE_BYTE;
    }

    public static Generator<Short> generateShort() {
        return GENERATE_SHORT;
    }
}
