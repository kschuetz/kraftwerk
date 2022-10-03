package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import software.kes.kraftwerk.constraints.BigDecimalRange;
import software.kes.kraftwerk.constraints.BigIntegerRange;
import software.kes.kraftwerk.constraints.ByteRange;
import software.kes.kraftwerk.constraints.DoubleRange;
import software.kes.kraftwerk.constraints.DurationRange;
import software.kes.kraftwerk.constraints.FloatRange;
import software.kes.kraftwerk.constraints.IntRange;
import software.kes.kraftwerk.constraints.LocalDateRange;
import software.kes.kraftwerk.constraints.LocalDateTimeRange;
import software.kes.kraftwerk.constraints.LocalTimeRange;
import software.kes.kraftwerk.constraints.LongRange;
import software.kes.kraftwerk.constraints.ShortRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static software.kes.kraftwerk.Generators.generateOrderedPair;
import static software.kes.kraftwerk.Primitives.generateByte;
import static software.kes.kraftwerk.Primitives.generateDouble;
import static software.kes.kraftwerk.Primitives.generateFloat;
import static software.kes.kraftwerk.Primitives.generateInt;
import static software.kes.kraftwerk.Primitives.generateLong;
import static software.kes.kraftwerk.Primitives.generateShort;
import static software.kes.kraftwerk.Temporal.DEFAULT_DURATION_RANGE;
import static software.kes.kraftwerk.Temporal.DEFAULT_LOCAL_DATE_RANGE;
import static software.kes.kraftwerk.Temporal.generateDuration;
import static software.kes.kraftwerk.Temporal.generateLocalDate;
import static software.kes.kraftwerk.Temporal.generateLocalDateTime;
import static software.kes.kraftwerk.Temporal.generateLocalTime;

final class Ranges {
    private static final DoubleRange FULL_DOUBLE_RANGE = DoubleRange.inclusive(Double.MIN_VALUE, Double.MAX_VALUE);
    private static final FloatRange FULL_FLOAT_RANGE = FloatRange.inclusive(-Float.MAX_VALUE, Float.MAX_VALUE);

    private Ranges() {
    }

    static Generator<IntRange> generateIntRange() {
        return generateIntRange(IntRange.fullRange());
    }

    static Generator<IntRange> generateIntRange(IntRange parentRange) {
        return generateOrderedPair(generateInt(parentRange))
                .fmap(into(IntRange::inclusive));
    }

    static Generator<LongRange> generateLongRange() {
        return generateLongRange(LongRange.fullRange());
    }

    static Generator<LongRange> generateLongRange(LongRange parentRange) {
        return generateOrderedPair(generateLong(parentRange))
                .fmap(into(LongRange::inclusive));
    }

    static Generator<ShortRange> generateShortRange() {
        return generateShortRange(ShortRange.fullRange());
    }

    static Generator<ShortRange> generateShortRange(ShortRange parentRange) {
        return generateOrderedPair(generateShort(parentRange))
                .fmap(into(ShortRange::inclusive));
    }

    static Generator<ByteRange> generateByteRange() {
        return generateByteRange(ByteRange.fullRange());
    }

    static Generator<ByteRange> generateByteRange(ByteRange parentRange) {
        return generateOrderedPair(generateByte(parentRange))
                .fmap(into(ByteRange::inclusive));
    }

    static Generator<DoubleRange> generateDoubleRange() {
        return generateDoubleRange(FULL_DOUBLE_RANGE);
    }

    static Generator<DoubleRange> generateDoubleRange(DoubleRange parentRange) {
        return generateOrderedPair(generateDouble(parentRange))
                .fmap(into(DoubleRange::inclusive));
    }

    static Generator<FloatRange> generateFloatRange() {
        return generateFloatRange(FULL_FLOAT_RANGE);
    }

    static Generator<FloatRange> generateFloatRange(FloatRange parentRange) {
        return generateOrderedPair(generateFloat(parentRange))
                .fmap(into(FloatRange::inclusive));
    }

    static Generator<BigIntegerRange> generateBigIntegerRange() {
        return generateBigIntegerRange(BigNumbers.DEFAULT_BIG_INTEGER_RANGE);
    }

    static Generator<BigIntegerRange> generateBigIntegerRange(BigIntegerRange parentRange) {
        return generateOrderedPair(BigNumbers.generateBigInteger(parentRange))
                .fmap(into(BigIntegerRange::inclusive));
    }

    static Generator<BigDecimalRange> generateBigDecimalRange() {
        return generateBigDecimalRange(BigNumbers.DEFAULT_BIG_DECIMAL_RANGE);
    }

    static Generator<BigDecimalRange> generateBigDecimalRange(BigDecimalRange parentRange) {
        return generateOrderedPair(BigNumbers.generateBigDecimal(parentRange))
                .fmap(into(BigDecimalRange::inclusive));
    }

    static Generator<LocalDateRange> generateLocalDateRange() {
        return generateLocalDateRange(DEFAULT_LOCAL_DATE_RANGE);
    }

    static Generator<LocalDateRange> generateLocalDateRange(LocalDateRange parentRange) {
        return generateLocalDate(parentRange).pair().fmap(Ranges::createLocalDateRange);
    }

    static Generator<LocalTimeRange> generateLocalTimeRange() {
        return generateLocalTimeRange(LocalTimeRange.fullRange());
    }

    static Generator<LocalTimeRange> generateLocalTimeRange(LocalTimeRange parentRange) {
        return generateLocalTime(parentRange).pair().fmap(Ranges::createLocalTimeRange);
    }

    static Generator<LocalDateTimeRange> generateLocalDateTimeRange() {
        return generateLocalDateTimeRange(DEFAULT_LOCAL_DATE_RANGE);
    }

    static Generator<LocalDateTimeRange> generateLocalDateTimeRange(LocalDateRange parentRange) {
        return generateLocalDateTime(parentRange).pair().fmap(Ranges::createLocalDateTimeRange);
    }

    static Generator<LocalDateTimeRange> generateLocalDateTimeRange(LocalDateTimeRange parentRange) {
        return generateLocalDateTime(parentRange).pair().fmap(Ranges::createLocalDateTimeRange);
    }

    static Generator<DurationRange> generateDurationRange() {
        return generateDurationRange(DEFAULT_DURATION_RANGE);
    }

    static Generator<DurationRange> generateDurationRange(DurationRange parentRange) {
        return generateOrderedPair(generateDuration(parentRange))
                .fmap(into(DurationRange::inclusive));
    }

    private static LocalDateRange createLocalDateRange(Tuple2<LocalDate, LocalDate> pair) {
        GTE.gte(pair._1(), pair._2());

        if (pair._1().isBefore(pair._2())) {
            return LocalDateRange.inclusive(pair._1(), pair._2());
        } else {
            return LocalDateRange.inclusive(pair._2(), pair._1());
        }
    }

    private static LocalTimeRange createLocalTimeRange(Tuple2<LocalTime, LocalTime> pair) {
        if (pair._1().isBefore(pair._2())) {
            return LocalTimeRange.inclusive(pair._1(), pair._2());
        } else {
            return LocalTimeRange.inclusive(pair._2(), pair._1());
        }
    }

    private static LocalDateTimeRange createLocalDateTimeRange(Tuple2<LocalDateTime, LocalDateTime> pair) {
        if (pair._1().isBefore(pair._2())) {
            return LocalDateTimeRange.inclusive(pair._1(), pair._2());
        } else {
            return LocalDateTimeRange.inclusive(pair._2(), pair._1());
        }
    }
}
