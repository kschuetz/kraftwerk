package dev.marksman.composablerandom.spike;

import static dev.marksman.composablerandom.spike.NewGenerator.constant;
import static dev.marksman.composablerandom.spike.NewGenerator.generator;

public class Primitives {

    private static final NewGenerator<Boolean> GENERATE_BOOLEAN = generator(Instruction.nextBoolean());
    private static final NewGenerator<Double> GENERATE_DOUBLE = generator(Instruction.nextDouble());
    private static final NewGenerator<Float> GENERATE_FLOAT = generator(Instruction.nextFloat());
    private static final NewGenerator<Integer> GENERATE_INTEGER = generator(Instruction.nextInt());
    private static final NewGenerator<Long> GENERATE_LONG = generator(Instruction.nextLong());
    private static final NewGenerator<Double> GENERATE_GAUSSIAN = generator(Instruction.nextGaussian());

    private static final NewGenerator<Byte> GENERATE_BYTE = GENERATE_INTEGER.fmap(Integer::byteValue);
    private static final NewGenerator<Short> GENERATE_SHORT = GENERATE_INTEGER.fmap(Integer::shortValue);

    private static final NewGenerator<Boolean> GENERATE_TRUE = constant(true);
    private static final NewGenerator<Boolean> GENERATE_FALSE = constant(false);

    public static NewGenerator<Boolean> generateBoolean() {
        return GENERATE_BOOLEAN;
    }

    public static NewGenerator<Double> generateDouble() {
        return GENERATE_DOUBLE;
    }

    public static NewGenerator<Integer> generateInt() {
        return GENERATE_INTEGER;
    }

    public static NewGenerator<Integer> generateInt(int min, int max) {
        return generator(Instruction.nextIntBetween(min, max));
    }

    public static NewGenerator<Integer> generateIntExclusive(int bound) {
        return generator(Instruction.nextIntBounded(bound));
    }

    public static NewGenerator<Integer> generateIntExclusive(int origin, int bound) {
        return generator(Instruction.nextIntExclusive(origin, bound));
    }

    public static NewGenerator<Integer> generateIntIndex(int bound) {
        return generator(Instruction.nextIntIndex(bound));
    }

    public static NewGenerator<Float> generateFloat() {
        return GENERATE_FLOAT;
    }

    public static NewGenerator<Long> generateLong() {
        return GENERATE_LONG;
    }

    public static NewGenerator<Long> generateLong(long min, long max) {
        return generator(Instruction.nextLongBetween(min, max));
    }

    public static NewGenerator<Long> generateLongExclusive(long bound) {
        return generator(Instruction.nextLongBounded(bound));
    }

    public static NewGenerator<Long> generateLongExclusive(long origin, long bound) {
        return generator(Instruction.nextLongExclusive(origin, bound));
    }

    public static NewGenerator<Double> generateGaussian() {
        return GENERATE_GAUSSIAN;
    }

    public static NewGenerator<Byte[]> generateBytes(int count) {
        return generator(Instruction.nextBytes(count));
    }

    public static NewGenerator<Byte> generateByte() {
        return GENERATE_BYTE;
    }

    public static NewGenerator<Short> generateShort() {
        return GENERATE_SHORT;
    }
}
