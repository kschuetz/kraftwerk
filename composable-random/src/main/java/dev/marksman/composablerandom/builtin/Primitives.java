package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.OldGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.metadata.StandardMetadata;

import static dev.marksman.composablerandom.OldGenerator.constant;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.metadata.StandardMetadata.labeled;

class Primitives {

    private static StandardMetadata INT_METADATA = labeled("generateInt");
    private static StandardMetadata LONG_METADATA = labeled("generateLong");
    private static StandardMetadata BYTES_METADATA = labeled("generateBytes");

    private static final OldGenerator<Boolean> GENERATE_BOOLEAN = OldGenerator.generator(RandomState::nextBoolean)
            .withLabel("generateBoolean");
    private static final OldGenerator<Double> GENERATE_DOUBLE = OldGenerator.generator(RandomState::nextDouble)
            .withLabel("generateDouble");
    private static final OldGenerator<Float> GENERATE_FLOAT = OldGenerator.generator(RandomState::nextFloat)
            .withLabel("generateFloat");
    private static final OldGenerator<Integer> GENERATE_INTEGER = OldGenerator.generator(RandomState::nextInt)
            .withMetadata(INT_METADATA);
    private static final OldGenerator<Long> GENERATE_LONG = OldGenerator.generator(RandomState::nextLong)
            .withMetadata(LONG_METADATA);
    private static final OldGenerator<Double> GENERATE_GAUSSIAN = OldGenerator.generator(RandomState::nextGaussian)
            .withLabel("generateGaussian");

    private static final OldGenerator<Byte> GENERATE_BYTE = GENERATE_INTEGER.fmap(Integer::byteValue)
            .withLabel("generateByte");
    private static final OldGenerator<Short> GENERATE_SHORT = GENERATE_INTEGER.fmap(Integer::shortValue)
            .withLabel("generateShort");

    private static final OldGenerator<Boolean> GENERATE_TRUE = constant(true);
    private static final OldGenerator<Boolean> GENERATE_FALSE = constant(false);

    static OldGenerator<Boolean> generateBoolean() {
        return GENERATE_BOOLEAN;
    }

    static OldGenerator<Boolean> generateBoolean(int trueWeight, int falseWeight) {
        return Weighted.leftRight(trueWeight, falseWeight,
                "trueWeight", "falseWeight",
                () -> GENERATE_TRUE, () -> GENERATE_FALSE);
    }

    static OldGenerator<Double> generateDouble() {
        return GENERATE_DOUBLE;
    }

    static OldGenerator<Integer> generateInt() {
        return GENERATE_INTEGER;
    }

    static OldGenerator<Integer> generateInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be >= min");
        }
        if (max == Integer.MAX_VALUE) {
            if (min == Integer.MIN_VALUE) {
                return generateInt();
            } else {
                return generateIntExclusive(min - 1, max)
                        .fmap(n -> n + 1);
            }
        } else {
            return generateIntExclusive(min, max + 1);
        }
    }

    static OldGenerator<Integer> generateIntExclusive(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }
        return OldGenerator.generator(INT_METADATA, s -> s.nextIntBounded(bound));
    }

    static OldGenerator<Integer> generateIntExclusive(int origin, int bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        long n = (long) bound - origin;
        long m = n - 1;
        if (n < Integer.MAX_VALUE) {
            return generateIntExclusive((int) n).fmap(r -> origin + r);
        } else if ((n & m) == 0) {
            // power of two
            return generateInt().fmap(r -> (r & (int) m) + origin);
        } else return OldGenerator.generator(INT_METADATA, rg0 -> {
            Result<? extends RandomState, Integer> rg1 = rg0.nextInt();
            RandomState current = rg1._1();
            int r = rg1._2();
            for (int u = r >>> 1;
                 u + m - (r = u % (int) n) < 0; ) {
                Result<? extends RandomState, Integer> next = current.nextInt();
                u = next._2() >>> 1;
                current = next._1();
            }
            r += origin;

            return result(current, r);
        });
    }

    static OldGenerator<Float> generateFloat() {
        return GENERATE_FLOAT;
    }

    static OldGenerator<Long> generateLong() {
        return GENERATE_LONG;
    }

    static OldGenerator<Long> generateLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be >= min");
        }
        if (max == Long.MAX_VALUE) {
            if (min == Long.MIN_VALUE) {
                return generateLong();
            } else {
                return generateLongExclusive(min - 1, max)
                        .fmap(n -> n + 1);
            }
        } else {
            return generateLongExclusive(min, max + 1);
        }
    }

    static OldGenerator<Long> generateLongExclusive(long bound) {
        if (bound <= Integer.MAX_VALUE) {
            return generateIntExclusive((int) bound).fmap(Integer::longValue);
        } else {
            return generateLongExclusive(0, bound);
        }
    }

    static OldGenerator<Long> generateLongExclusive(long origin, long bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        long n = bound - origin;
        long m = n - 1;

        if ((n & m) == 0L) {
            // power of two
            return generateLong().fmap(r -> (r & m) + origin);
        } else return OldGenerator.generator(LONG_METADATA, rg0 -> {
            Result<? extends RandomState, Long> rg1 = rg0.nextLong();
            RandomState current = rg1._1();
            long r = rg1._2();
            for (long u = r >>> 1;
                 u + m - (r = u % n) < 0L; ) {
                Result<? extends RandomState, Long> next = current.nextLong();
                u = next._2() >>> 1;
                current = next._1();
            }
            r += origin;

            return result(current, r);
        });
    }

    static OldGenerator<Double> generateGaussian() {
        return GENERATE_GAUSSIAN;
    }

    static OldGenerator<Byte[]> generateBytes(int count) {
        return OldGenerator.generator(BYTES_METADATA, s -> {
            byte[] buffer = new byte[count];
            Result<? extends RandomState, Unit> next = s.nextBytes(buffer);
            Byte[] result = new Byte[count];
            int i = 0;
            for (byte b : buffer) {
                result[i++] = b;
            }
            return result(next._1(), result);
        });
    }

    static OldGenerator<Byte> generateByte() {
        return GENERATE_BYTE;
    }

    static OldGenerator<Short> generateShort() {
        return GENERATE_SHORT;
    }
}
