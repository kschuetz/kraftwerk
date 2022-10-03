package software.kes.kraftwerk;

import software.kes.kraftwerk.constraints.BigDecimalRange;
import software.kes.kraftwerk.constraints.BigIntegerRange;
import software.kes.kraftwerk.constraints.IntRange;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import static java.math.BigDecimal.ROUND_DOWN;
import static software.kes.kraftwerk.Bias.applyBiasSetting;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateLong;

class BigNumbers {
    static final BigIntegerRange DEFAULT_BIG_INTEGER_RANGE =
            BigIntegerRange.from(BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE))
                    .to(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
    static final BigDecimalRange DEFAULT_BIG_DECIMAL_RANGE =
            BigDecimalRange.from(BigDecimal.valueOf(Long.MIN_VALUE, 0).subtract(BigDecimal.ONE))
                    .to(BigDecimal.valueOf(Long.MAX_VALUE).add(BigDecimal.ONE));
    private static final Generator<Integer> DEFAULT_GENERATE_DECIMAL_PLACES = generateInt(IntRange.from(-1).to(20));

    static Generator<BigInteger> generateBigInteger() {
        return generateBigInteger(DEFAULT_BIG_INTEGER_RANGE);
    }

    static Generator<BigInteger> generateBigInteger(BigIntegerRange range) {
        BigInteger min = range.minInclusive();
        if (min.signum() == 0) {
            return generateBigIntegerExclusive(range.maxExclusive());
        } else {
            return generateBigIntegerExclusive(min, range.maxExclusive());
        }
    }

    static Generator<BigDecimal> generateBigDecimal() {
        return generateBigDecimal(DEFAULT_GENERATE_DECIMAL_PLACES, DEFAULT_BIG_DECIMAL_RANGE);
    }

    static Generator<BigDecimal> generateBigDecimal(BigDecimalRange range) {
        return generateBigDecimal(DEFAULT_GENERATE_DECIMAL_PLACES, range);
    }

    static Generator<BigDecimal> generateBigDecimal(Generator<Integer> generateDecimalPlaces, BigDecimalRange range) {
        return generateDecimalPlaces.flatMap(decimalPlaces -> generateBigDecimal(decimalPlaces, range));
    }

    static Generator<BigDecimal> generateBigDecimal(IntRange decimalPlacesRange, BigDecimalRange range) {
        return generateInt(decimalPlacesRange).flatMap(decimalPlaces -> generateBigDecimal(decimalPlaces, range));
    }

    static Generator<BigDecimal> generateBigDecimal(int decimalPlaces, BigDecimalRange range) {
        BigDecimal min = range.min();
        BigDecimal max = range.max();
        int minScale = min.scale();
        int maxScale = max.scale();
        int shift = Math.max(decimalPlaces, Math.max(minScale, maxScale));

        BigInteger integerOrigin = makeMinInclusive(range.minIncluded(), movePointRight(shift, min));
        BigInteger integerBound = makeMaxExclusive(range.maxIncluded(), movePointRight(shift, max));
        BigInteger integerRange = integerBound.subtract(integerOrigin);

        if (integerRange.signum() < 1) {
            throw new IllegalArgumentException("range too narrow");
        }

        return applyBiasSetting(bs -> bs.bigDecimalBias(range),
                generateBigIntegerExclusive(integerRange)
                        .fmap(n -> movePointLeft(shift, integerOrigin.add(n))
                                .setScale(decimalPlaces, ROUND_DOWN)));
    }

    private static Generator<BigInteger> generateBigIntegerExclusive(BigInteger bound) {
        return applyBiasSetting(bs -> bs.bigIntegerBias(BigIntegerRange.exclusive(bound)),
                generateLong().fmap(s -> {
                    int bitLength = bound.bitLength();
                    Random rnd = new Random();
                    rnd.setSeed(s);
                    BigInteger result;
                    do {
                        result = new BigInteger(bitLength, rnd);
                    } while (result.compareTo(bound) >= 0);
                    return result;
                }));
    }

    private static Generator<BigInteger> generateBigIntegerExclusive(BigInteger origin, BigInteger bound) {
        BigInteger range = bound.subtract(origin);
        if (range.signum() < 1) throw new IllegalArgumentException("bound must be > origin");
        return generateBigIntegerExclusive(range).fmap(origin::add);
    }

    private static BigInteger movePointRight(int decimalPlaces, BigDecimal input) {
        return input.movePointRight(decimalPlaces).toBigInteger();
    }

    private static BigDecimal movePointLeft(int decimalPlaces, BigInteger input) {
        return new BigDecimal(input).movePointLeft(decimalPlaces);
    }

    private static BigInteger makeMinInclusive(boolean minIncluded, BigInteger min) {
        return minIncluded ? min : min.add(BigInteger.ONE);
    }

    private static BigInteger makeMaxExclusive(boolean maxIncluded, BigInteger max) {
        return maxIncluded ? max.add(BigInteger.ONE) : max;
    }
}
