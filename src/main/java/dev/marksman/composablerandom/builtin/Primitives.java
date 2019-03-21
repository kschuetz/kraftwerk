package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.Result.result;

class Primitives {

    private static final Generator<Boolean> GENERATE_BOOLEAN = Generator.generator(RandomState::nextBoolean);
    private static final Generator<Double> GENERATE_DOUBLE = Generator.generator(RandomState::nextDouble);
    private static final Generator<Float> GENERATE_FLOAT = Generator.generator(RandomState::nextFloat);
    private static final Generator<Integer> GENERATE_INTEGER = Generator.generator(RandomState::nextInt);
    private static final Generator<Long> GENERATE_LONG = Generator.generator(RandomState::nextLong);
    private static final Generator<Double> GENERATE_GAUSSIAN = Generator.generator(RandomState::nextGaussian);

    private static final Generator<Byte> GENERATE_BYTE = GENERATE_INTEGER.fmap(Integer::byteValue);
    private static final Generator<Short> GENERATE_SHORT = GENERATE_INTEGER.fmap(Integer::shortValue);

    static Generator<Boolean> generateBoolean() {
        return GENERATE_BOOLEAN;
    }

    static Generator<Boolean> generateBoolean(int trueWeight, int falseWeight) {
        if (trueWeight < 0) {
            throw new IllegalArgumentException("trueWeight must be >= 0");
        }
        if (falseWeight < 0) {
            throw new IllegalArgumentException("falseWeight must be >= 0");
        }
        int total = trueWeight + falseWeight;
        if (total < 1) {
            throw new IllegalArgumentException("sum of weights must be >= 1");
        }
        if (trueWeight == 0) {
            return constant(false);
        } else if (falseWeight == 0) {
            return constant(true);
        } else {
            return generateIntExclusive(total).fmap(n -> n < trueWeight);
        }
    }

    static Generator<Double> generateDouble() {
        return GENERATE_DOUBLE;
    }

    static Generator<Integer> generateInt() {
        return GENERATE_INTEGER;
    }

    static Generator<Integer> generateInt(int min, int max) {
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

    static Generator<Integer> generateIntExclusive(int bound) {
        return Generator.generator(s -> s.nextInt(bound));
    }

    static Generator<Integer> generateIntExclusive(int origin, int bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        int n = bound - origin;
        int m = n - 1;
        if (n < Integer.MAX_VALUE) {
            return generateIntExclusive(n).fmap(r -> origin + r);
        } else if ((n & m) == 0) {
            // power of two
            return generateInt().fmap(r -> (r & m) + origin);
        } else return Generator.generator(rg0 -> {
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

    static Generator<Float> generateFloat() {
        return GENERATE_FLOAT;
    }

    static Generator<Long> generateLong() {
        return GENERATE_LONG;
    }

    static Generator<Long> generateLong(long min, long max) {
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

    static Generator<Long> generateLongExclusive(long bound) {
        if (bound <= Integer.MAX_VALUE) {
            return generateIntExclusive((int) bound).fmap(Integer::longValue);
        } else {
            return generateLongExclusive(0, bound);
        }
    }

    static Generator<Long> generateLongExclusive(long origin, long bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        long n = bound - origin;
        long m = n - 1;

        if ((n & m) == 0L) {
            // power of two
            return generateLong().fmap(r -> (r & m) + origin);
        } else return Generator.generator(rg0 -> {
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

    static Generator<Double> generateGaussian() {
        return GENERATE_GAUSSIAN;
    }

    static Generator<Byte[]> generateBytes(int count) {
        return Generator.generator(s -> {
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

    static Generator<Byte> generateByte() {
        return GENERATE_BYTE;
    }

    static Generator<Short> generateShort() {
        return GENERATE_SHORT;
    }
}
