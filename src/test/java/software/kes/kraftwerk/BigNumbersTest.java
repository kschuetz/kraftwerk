package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.kraftwerk.constraints.BigDecimalRange;
import software.kes.kraftwerk.constraints.BigIntegerRange;
import software.kes.kraftwerk.constraints.IntRange;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static software.kes.kraftwerk.Generators.generateBigDecimal;
import static software.kes.kraftwerk.Generators.generateBigInteger;
import static software.kes.kraftwerk.Generators.generateInt;
import static testsupport.Assert.assertAlwaysInRange;
import static testsupport.Assert.assertForAll;
import static testsupport.CoversRange.coversRange;

class BigNumbersTest {
    @Nested
    @DisplayName("BigIntegers")
    class BigIntegers {
        private final BigInteger LONG_MIN_VALUE = BigInteger.valueOf(Long.MIN_VALUE);
        private final BigInteger LONG_MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

        @Test
        void alwaysInRange() {
            assertAlwaysInRange(BigIntegerRange.from(BigInteger.ZERO).to(BigInteger.TEN), Generators::generateBigInteger);
            assertAlwaysInRange(BigIntegerRange.from(BigInteger.ONE.negate()).to(BigInteger.ONE), Generators::generateBigInteger);
            assertAlwaysInRange(BigIntegerRange.from(LONG_MIN_VALUE.subtract(BigInteger.ONE))
                    .to(LONG_MAX_VALUE.add(BigInteger.ONE)), Generators::generateBigInteger);
            assertAlwaysInRange(BigIntegerRange.from(LONG_MIN_VALUE.subtract(LONG_MAX_VALUE))
                    .to(LONG_MIN_VALUE), Generators::generateBigInteger);
            assertAlwaysInRange(BigIntegerRange.from(LONG_MAX_VALUE)
                    .to(LONG_MAX_VALUE.add(LONG_MAX_VALUE)), Generators::generateBigInteger);
        }

        @Test
        void testCoversRange() {
            int[] f = new int[256];
            generateBigInteger(BigIntegerRange.from(BigInteger.valueOf(-128)).to(BigInteger.valueOf(127)))
                    .run()
                    .take(2560)
                    .forEach(n -> f[n.intValue() + 128] += 1);

            assertTrue(coversRange(f));
        }
    }

    @Nested
    @DisplayName("BigDecimals")
    class BigDecimals {
        private final BigDecimal LONG_MIN_VALUE = BigDecimal.valueOf(Long.MIN_VALUE);
        private final BigDecimal LONG_MAX_VALUE = BigDecimal.valueOf(Long.MAX_VALUE);

        @Test
        void alwaysInRange() {
            assertAlwaysInRange(BigDecimalRange.from(BigDecimal.ZERO).to(BigDecimal.TEN), Generators::generateBigDecimal);
            assertAlwaysInRange(BigDecimalRange.from(BigDecimal.ONE.negate()).to(BigDecimal.ONE), Generators::generateBigDecimal);
            assertAlwaysInRange(BigDecimalRange.from(LONG_MIN_VALUE.subtract(BigDecimal.ONE))
                    .to(LONG_MAX_VALUE.add(BigDecimal.ONE)), Generators::generateBigDecimal);
            assertAlwaysInRange(BigDecimalRange.from(LONG_MIN_VALUE.subtract(LONG_MAX_VALUE))
                    .to(LONG_MIN_VALUE), Generators::generateBigDecimal);
            assertAlwaysInRange(BigDecimalRange.from(LONG_MAX_VALUE)
                    .to(LONG_MAX_VALUE.add(LONG_MAX_VALUE)), Generators::generateBigDecimal);
        }

        @Test
        void constantDecimalPlaces() {
            BigDecimalRange range = BigDecimalRange.from(new BigDecimal("-999999.9999999999")).to(new BigDecimal("999999.9999999999"));

            assertForAll(generateBigDecimal(0, range), n -> range.includes(n) && n.scale() == 0);
            assertForAll(generateBigDecimal(1, range), n -> range.includes(n) && n.scale() == 1);
            assertForAll(generateBigDecimal(5, range), n -> range.includes(n) && n.scale() == 5);
            assertForAll(generateBigDecimal(10, range), n -> range.includes(n) && n.scale() == 10);
            assertForAll(generateBigDecimal(-1, range), n -> range.includes(n) && n.scale() == -1);
            assertForAll(generateBigDecimal(-5, range), n -> range.includes(n) && n.scale() == -5);
            assertForAll(generateBigDecimal(-10, range), n -> range.includes(n) && n.scale() == -10);
        }

        @Test
        void generatedNumberOfDecimalPlaces() {
            BigDecimalRange range = BigDecimalRange.from(new BigDecimal("-999999.9999999999")).to(new BigDecimal("999999.9999999999"));

            Generator<Tuple2<Integer, BigDecimal>> generateTestCase = generateInt(IntRange.from(-10).to(10))
                    .flatMap(decimalPlaces -> generateBigDecimal(decimalPlaces, range)
                            .fmap((BigDecimal n) -> tuple(decimalPlaces, n)));
            assertForAll(generateTestCase, testCase -> range.includes(testCase._2()) && testCase._2().scale() == testCase._1());
        }

        @Test
        void rangeOfDecimalPlaces() {
            BigDecimalRange range = BigDecimalRange.from(new BigDecimal("-999999.9999999999")).to(new BigDecimal("999999.9999999999"));
            IntRange decimalPlacesRange = IntRange.from(-10).to(10);
            assertForAll(generateBigDecimal(decimalPlacesRange, range),
                    n -> range.includes(n) && decimalPlacesRange.includes(n.scale()));
        }
    }
}