package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GTE.gte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static dev.marksman.kraftwerk.Generators.generateBoolean;
import static dev.marksman.kraftwerk.Generators.generateByte;
import static dev.marksman.kraftwerk.Generators.generateDouble;
import static dev.marksman.kraftwerk.Generators.generateFloat;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateShort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.Assert.assertForAll;
import static testsupport.CoversRange.coversRange;
import static testsupport.Sample.sample;

class PrimitivesTest {

    @Test
    void testIntInclusiveInBounds() {
        assertForAll(generateInt(IntRange.from(0).to(0)), eq(0));
        assertForAll(generateInt(IntRange.from(-256).to(256)), n -> n >= -256 && n <= 256);
        assertForAll(generateInt(IntRange.from(0).to(1)), n -> n == 0 || n == 1);
        assertForAll(generateInt(IntRange.from(-1).to(1)), n -> n >= -1 && n <= 1);
        assertForAll(generateInt(IntRange.from(-1).to(1)), n -> n >= -1 && n <= 1);
        assertForAll(generateInt(IntRange.from(Integer.MIN_VALUE).to(0)), lte(0));
        assertForAll(generateInt(IntRange.from(Integer.MIN_VALUE).to(-1)), lt(0));
        assertForAll(generateInt(IntRange.from(0).to(Integer.MAX_VALUE)), gte(0));
        assertForAll(generateInt(IntRange.from(0).to(Integer.MAX_VALUE)), gte(0));
        assertForAll(generateInt().flatMap(lowerBound ->
                        generateInt(IntRange.from(lowerBound).to(Integer.MAX_VALUE))
                                .flatMap(upperBound -> generateInt(IntRange.from(lowerBound).to(upperBound))
                                        .fmap(n -> n >= lowerBound && n <= upperBound))),
                id());

    }

//    @Test
//    void testIntExclusiveInBounds() {
//        assertForAll(generateIntExclusive(Integer.MAX_VALUE), gte(0));
//        assertForAll(generateIntExclusive(Integer.MAX_VALUE).flatMap(Generators::generateIntExclusive), gte(0));
//        assertForAll(generateInt(Integer.MIN_VALUE, Integer.MAX_VALUE - 1)
//                        .flatMap(lowerBound -> generateInt(lowerBound + 1, Integer.MAX_VALUE)
//                                .flatMap(upperBound -> generateIntExclusive(lowerBound, upperBound)
//                                        .fmap(n -> n >= lowerBound && n < upperBound))),
//                id());
//    }

    @Test
    void testIntFullRange() {
        Seed initial = Seed.random();
        assertEquals(sample(generateInt(IntRange.from(Integer.MIN_VALUE).to(Integer.MAX_VALUE)), initial),
                sample(generateInt(), initial));
    }

    @Test
    void testLongInclusiveInBounds() {
        assertForAll(generateLong(LongRange.inclusive(0L, 0L)), eq(0L));
        assertForAll(generateLong(LongRange.inclusive(-256, 256)), n -> n >= -256 && n <= 256);
        assertForAll(generateLong(LongRange.inclusive(0, 1)), n -> n == 0 || n == 1);
        assertForAll(generateLong(LongRange.inclusive(-1, 1)), n -> n >= -1 && n <= 1);
        assertForAll(generateLong(LongRange.inclusive(-1, 1)), n -> n >= -1 && n <= 1);
        assertForAll(generateLong(LongRange.inclusive(Long.MIN_VALUE, 0L)), lte(0L));
        assertForAll(generateLong(LongRange.inclusive(Long.MIN_VALUE, -1)), lt(0L));
        assertForAll(generateLong(LongRange.inclusive(0, Long.MAX_VALUE)), gte(0L));
        assertForAll(generateLong(LongRange.inclusive(0, Long.MAX_VALUE)), gte(0L));
        assertForAll(generateLong().flatMap(lowerBound ->
                        generateLong(LongRange.inclusive(lowerBound, Long.MAX_VALUE))
                                .flatMap(upperBound -> generateLong(LongRange.inclusive(lowerBound, upperBound))
                                        .fmap(n -> n >= lowerBound && n <= upperBound))),
                id());

    }

//    @Test
//    void testLongExclusiveInBounds() {
//        assertForAll(Generators.generateLong(LongRange.exclusive(Long.MAX_VALUE)), gte(0L));
//        assertForAll(Generators.generateLong(LongRange.exclusive(Long.MAX_VALUE)).flatMap(Generators::generateLongExclusive), gte(0L));
//        assertForAll(generateLong(Long.MIN_VALUE, Long.MAX_VALUE - 1)
//                        .flatMap(lowerBound -> generateLong(lowerBound + 1, Long.MAX_VALUE)
//                                .flatMap(upperBound -> Generators.generateLongExclusive(lowerBound, upperBound)
//                                        .fmap(n -> n >= lowerBound && n < upperBound))),
//                id());
//    }

    @Test
    void testLongFullRange() {
        Seed initial = Seed.random();
        assertEquals(sample(generateLong(LongRange.inclusive(Long.MIN_VALUE, Long.MAX_VALUE)), initial),
                sample(generateLong(), initial));
    }

    @Test
    void testDoubleBetween0and1() {
        assertForAll(Generators.generateDoubleFractional(), n -> n >= 0.0d && n <= 1.0d);
    }

    @Test
    void testDoubleScaledInBounds() {
        assertForAll(generateDouble(DoubleRange.exclusive(999.0d)), n -> n >= 0.0d && n <= 999.0d);
        assertForAll(generateDouble(DoubleRange.exclusive(999.0d).negate()), n -> n >= -999.0d && n <= 0.0d);
    }

    @Test
    void testFloatBetween0and1() {
        assertForAll(Generators.generateFloatFractional(), n -> n >= 0.0f && n <= 1.0f);
    }

    @Test
    void testFloatScaledInBounds() {
        assertForAll(generateFloat(FloatRange.exclusive(999.0f)), n -> n >= 0.0f && n <= 999.0f);
        assertForAll(generateFloat(FloatRange.exclusive(999.0f).negate()), n -> n >= -999.0f && n <= 0.0f);
    }

    @Test
    void testIntCoversRange() {
        int[] f0 = new int[256];
        int[] f1 = new int[256];
        int[] f2 = new int[256];
        int[] f3 = new int[256];
        generateInt()
                .run()
                .take(2560)
                .forEach(n -> {
                    f0[n & 255] += 1;
                    f1[(n >> 8) & 255] += 1;
                    f2[(n >> 16) & 255] += 1;
                    f3[(n >> 24) & 255] += 1;
                });

        assertTrue(coversRange(f0));
        assertTrue(coversRange(f1));
        assertTrue(coversRange(f2));
        assertTrue(coversRange(f3));
    }

    @Test
    void testLongCoversRange() {
        int[] f0 = new int[256];
        int[] f1 = new int[256];
        int[] f2 = new int[256];
        int[] f3 = new int[256];
        int[] f4 = new int[256];
        int[] f5 = new int[256];
        int[] f6 = new int[256];
        int[] f7 = new int[256];
        generateLong()
                .run()
                .take(2560)
                .forEach(n -> {
                    f0[(int) (n & 255)] += 1;
                    f1[(int) (n >> 8) & 255] += 1;
                    f2[(int) (n >> 16) & 255] += 1;
                    f3[(int) (n >> 24) & 255] += 1;
                    f4[(int) (n >> 32) & 255] += 1;
                    f5[(int) (n >> 40) & 255] += 1;
                    f6[(int) (n >> 48) & 255] += 1;
                    f7[(int) (n >> 56) & 255] += 1;
                });

        assertTrue(coversRange(f0));
        assertTrue(coversRange(f1));
        assertTrue(coversRange(f2));
        assertTrue(coversRange(f3));
        assertTrue(coversRange(f4));
        assertTrue(coversRange(f5));
        assertTrue(coversRange(f6));
        assertTrue(coversRange(f7));
    }

    @Test
    void testDoubleCoversRange() {
        int partitions = 512;
        int[] f = new int[partitions];
        Generators.generateDoubleFractional()
                .run()
                .take(10 * partitions)
                .forEach(n -> {
                    int bucket = (int) (n * partitions);
                    f[bucket] += 1;
                });

        assertTrue(coversRange(f));
    }

    @Test
    void testFloatCoversRange() {
        int partitions = 512;
        int[] f = new int[partitions];
        Generators.generateFloatFractional()
                .run()
                .take(10 * partitions)
                .forEach(n -> {
                    int bucket = (int) (n * partitions);
                    f[bucket] += 1;
                });

        assertTrue(coversRange(f));
    }

    @Test
    void testBooleanCoversRange() {
        int[] f = new int[2];
        generateBoolean()
                .run()
                .take(20)
                .forEach(b -> f[b ? 0 : 1] += 1);

        assertTrue(coversRange(f));
    }

    @Test
    void testShortCoversRange() {
        int[] f0 = new int[256];
        int[] f1 = new int[256];
        generateShort()
                .run()
                .take(2560)
                .forEach(n -> {
                    f0[n & 255] += 1;
                    f1[(n >> 8) & 255] += 1;
                });

        assertTrue(coversRange(f0));
        assertTrue(coversRange(f1));
    }

    @Test
    void testByteCoversRange() {
        int[] f = new int[256];
        generateByte()
                .run()
                .take(2560)
                .forEach(n -> f[n & 255] += 1);

        assertTrue(coversRange(f));
    }

//    @Test
//    void testIntExclusiveCoversRange1() {
//        int[] f = new int[256];
//        generateIntExclusive(256)
//                .run()
//                .take(2560)
//                .forEach(n -> {
//                    f[n] += 1;
//                });
//
//        assertTrue(coversRange(f));
//    }
//
//    @Test
//    void testIntExclusiveCoversRange2() {
//        int[] f = new int[256];
//        generateIntExclusive(-128, 128)
//                .run()
//                .take(2560)
//                .forEach(n -> {
//                    f[n + 128] += 1;
//                });
//
//        assertTrue(coversRange(f));
//    }

    @Test
    void testIntInclusiveCoversRange1() {
        int[] f = new int[256];
        generateInt(IntRange.from(0).to(255))
                .run()
                .take(2560)
                .forEach(n -> f[n] += 1);

        assertTrue(coversRange(f));
    }

    @Test
    void testIntInclusiveCoversRange2() {
        int[] f = new int[256];
        generateInt(IntRange.from(-128).to(127))
                .run()
                .take(2560)
                .forEach(n -> f[n + 128] += 1);

        assertTrue(coversRange(f));
    }


    @Test
    void testLongInclusiveCoversRange1() {
        int[] f = new int[256];
        generateLong(LongRange.inclusive(0, 255))
                .run()
                .take(2560)
                .forEach(n -> f[n.intValue()] += 1);

        assertTrue(coversRange(f));
    }

    @Test
    void testLongInclusiveCoversRange2() {
        int[] f = new int[256];
        generateLong(LongRange.inclusive(-128, 127))
                .run()
                .take(2560)
                .forEach(n -> f[n.intValue() + 128] += 1);

        assertTrue(coversRange(f));
    }
}
