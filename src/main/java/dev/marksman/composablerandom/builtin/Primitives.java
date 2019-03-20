package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;

import static dev.marksman.composablerandom.Generate.constant;
import static dev.marksman.composablerandom.Generate.generate;
import static dev.marksman.composablerandom.Result.result;

public class Primitives {

    private static final Generate<Boolean> GENERATE_BOOLEAN = generate(RandomState::nextBoolean);
    private static final Generate<Double> GENERATE_DOUBLE = generate(RandomState::nextDouble);
    private static final Generate<Float> GENERATE_FLOAT = generate(RandomState::nextFloat);
    private static final Generate<Integer> GENERATE_INTEGER = generate(RandomState::nextInt);
    private static final Generate<Long> GENERATE_LONG = generate(RandomState::nextLong);
    private static final Generate<Double> GENERATE_GAUSSIAN = generate(RandomState::nextGaussian);

    private static final Generate<Byte> GENERATE_BYTE = GENERATE_INTEGER.fmap(Integer::byteValue);
    private static final Generate<Short> GENERATE_SHORT = GENERATE_INTEGER.fmap(Integer::shortValue);

    public static Generate<Boolean> generateBoolean() {
        return GENERATE_BOOLEAN;
    }

    public static Generate<Boolean> generateBoolean(int trueWeight, int falseWeight) {
        if(trueWeight < 0) {
            throw new IllegalArgumentException("trueWeight must be >= 0");
        }
        if(falseWeight < 0) {
            throw new IllegalArgumentException("falseWeight must be >= 0");
        }
        int total = trueWeight + falseWeight;
        if(total < 1) {
            throw new IllegalArgumentException("sum of weights must be >= 1");
        }
        if(trueWeight == 0) {
            return constant(false);
        } else if(falseWeight == 0) {
            return constant(true);
        } else {
            return generateInt(total).fmap(n -> n < trueWeight);
        }
    }

    public static Generate<Double> generateDouble() {
        return GENERATE_DOUBLE;
    }

    public static Generate<Integer> generateInt() {
        return GENERATE_INTEGER;
    }

    public static Generate<Integer> generateInt(int bound) {
        return generate(s -> s.nextInt(bound));
    }

    public static Generate<Integer> generateInt(int origin, int bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        int n = bound - origin;
        int m = n - 1;
        if (n < Integer.MAX_VALUE) {
            return generateInt(n).fmap(r -> origin + r);
        } else if ((n & m) == 0) {
            // power of two
            return generateInt().fmap(r -> (r & m) + origin);
        } else return generate(rg0 -> {
            Result<? extends RandomState, Integer> rg1 = rg0.nextInt();
            RandomState current = rg1._1();
            int r = rg1._2();
            for (int u = r >>> 1;
                 u + m - (r = u % n) < 0; ) {
                Result<? extends RandomState, Integer> next = current.nextInt();
                u = next._2() >>> 1;
                current = next._1();
            }
            r += origin;

            return result(current, r);
        });
    }

    public static Generate<Float> generateFloat() {
        return GENERATE_FLOAT;
    }

    public static Generate<Long> generateLong() {
        return GENERATE_LONG;
    }

    public static Generate<Long> generateLong(long bound) {
        if (bound <= Integer.MAX_VALUE) {
            return generateInt((int) bound).fmap(Integer::longValue);
        } else {
            return generateLong(0, bound);
        }
    }

    public static Generate<Long> generateLong(long origin, long bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        long n = bound - origin;
        long m = n - 1;

        if ((n & m) == 0L) {
            // power of two
            return generateLong().fmap(r -> (r & m) + origin);
        } else return generate(rg0 -> {
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

    public static Generate<Double> generateGaussian() {
        return GENERATE_GAUSSIAN;
    }

    public static Generate<Byte[]> generateBytes(int count) {
        return generate(s -> {
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

    public static Generate<Byte> generateByte() {
        return GENERATE_BYTE;
    }

    public static Generate<Short> generateShort() {
        return GENERATE_SHORT;
    }
}
