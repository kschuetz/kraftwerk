package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.function.Function;

public abstract class Instruction<A> {

    public abstract <R> R match(Function<Pure<A>, R> pureFn,
                                Function<Mapped<?, A>, R> mappedFn,
                                Function<FlatMapped<?, A>, R> flatMappedFn,
                                Function<NextBoolean, R> nextBooleanFn,
                                Function<NextDouble, R> nextDoubleFn,
                                Function<NextFloat, R> nextFloatFn,
                                Function<NextInt, R> nextIntFn,
                                Function<NextIntExclusive, R> nextIntExclusiveFn,
                                Function<NextIntBetween, R> nextIntBetweenFn,
                                Function<NextIntIndex, R> nextIntIndexFn,
                                Function<NextLong, R> nextLongFn,
                                Function<NextLongExclusive, R> nextLongExclusiveFn,
                                Function<NextLongBetween, R> nextLongBetweenFn,
                                Function<NextLongIndex, R> nextLongIndexFn,
                                Function<NextGaussian, R> nextGaussianFn,
                                Function<NextBytes, R> nextBytesFn,
                                Function<Sized<A>, R> sizedFn,
                                Function<Labeled<A>, R> labeledFn);


    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pure<A> extends Instruction<A> {
        private final A value;

        @Override
        public <R> R match(Function<Pure<A>, R> pureFn,
                           Function<Mapped<?, A>, R> mappedFn,
                           Function<FlatMapped<?, A>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<A>, R> sizedFn,
                           Function<Labeled<A>, R> labeledFn) {
            return pureFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Mapped<In, A> extends Instruction<A> {
        private final Fn1<In, A> fn;
        private final Instruction<In> operand;

        @Override
        public <R> R match(Function<Pure<A>, R> pureFn,
                           Function<Mapped<?, A>, R> mappedFn,
                           Function<FlatMapped<?, A>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<A>, R> sizedFn,
                           Function<Labeled<A>, R> labeledFn) {
            return mappedFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FlatMapped<In, A> extends Instruction<A> {
        private final Fn1<In, Instruction<A>> fn;
        private final Instruction<In> operand;

        @Override
        public <R> R match(Function<Pure<A>, R> pureFn,
                           Function<Mapped<?, A>, R> mappedFn,
                           Function<FlatMapped<?, A>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<A>, R> sizedFn,
                           Function<Labeled<A>, R> labeledFn) {
            return flatMappedFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextBoolean extends Instruction<Boolean> {
        private static final NextBoolean INSTANCE = new NextBoolean();

        @Override
        public <R> R match(Function<Pure<Boolean>, R> pureFn,
                           Function<Mapped<?, Boolean>, R> mappedFn,
                           Function<FlatMapped<?, Boolean>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Boolean>, R> sizedFn,
                           Function<Labeled<Boolean>, R> labeledFn) {
            return nextBooleanFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextDouble extends Instruction<Double> {
        private static final NextDouble INSTANCE = new NextDouble();

        @Override
        public <R> R match(Function<Pure<Double>, R> pureFn,
                           Function<Mapped<?, Double>, R> mappedFn,
                           Function<FlatMapped<?, Double>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Double>, R> sizedFn,
                           Function<Labeled<Double>, R> labeledFn) {
            return nextDoubleFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextFloat extends Instruction<Float> {
        private static final NextFloat INSTANCE = new NextFloat();

        @Override
        public <R> R match(Function<Pure<Float>, R> pureFn,
                           Function<Mapped<?, Float>, R> mappedFn,
                           Function<FlatMapped<?, Float>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Float>, R> sizedFn,
                           Function<Labeled<Float>, R> labeledFn) {
            return nextFloatFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextInt extends Instruction<Integer> {
        private static final NextInt INSTANCE = new NextInt();

        @Override
        public <R> R match(Function<Pure<Integer>, R> pureFn,
                           Function<Mapped<?, Integer>, R> mappedFn,
                           Function<FlatMapped<?, Integer>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Integer>, R> sizedFn,
                           Function<Labeled<Integer>, R> labeledFn) {
            return nextIntFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntExclusive extends Instruction<Integer> {
        private final int bound;

        @Override
        public <R> R match(Function<Pure<Integer>, R> pureFn,
                           Function<Mapped<?, Integer>, R> mappedFn,
                           Function<FlatMapped<?, Integer>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Integer>, R> sizedFn,
                           Function<Labeled<Integer>, R> labeledFn) {
            return nextIntExclusiveFn.apply(this);
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntBetween extends Instruction<Integer> {
        private final int min;
        private final int max;

        @Override
        public <R> R match(Function<Pure<Integer>, R> pureFn,
                           Function<Mapped<?, Integer>, R> mappedFn,
                           Function<FlatMapped<?, Integer>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Integer>, R> sizedFn,
                           Function<Labeled<Integer>, R> labeledFn) {
            return nextIntBetweenFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntIndex extends Instruction<Integer> {
        private final int bound;

        @Override
        public <R> R match(Function<Pure<Integer>, R> pureFn,
                           Function<Mapped<?, Integer>, R> mappedFn,
                           Function<FlatMapped<?, Integer>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Integer>, R> sizedFn,
                           Function<Labeled<Integer>, R> labeledFn) {
            return nextIntIndexFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLong extends Instruction<Long> {
        private static final NextLong INSTANCE = new NextLong();

        @Override
        public <R> R match(Function<Pure<Long>, R> pureFn,
                           Function<Mapped<?, Long>, R> mappedFn,
                           Function<FlatMapped<?, Long>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Long>, R> sizedFn,
                           Function<Labeled<Long>, R> labeledFn) {
            return nextLongFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongExclusive extends Instruction<Long> {
        private final long bound;

        @Override
        public <R> R match(Function<Pure<Long>, R> pureFn,
                           Function<Mapped<?, Long>, R> mappedFn,
                           Function<FlatMapped<?, Long>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Long>, R> sizedFn,
                           Function<Labeled<Long>, R> labeledFn) {
            return nextLongExclusiveFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongBetween extends Instruction<Long> {
        private final long min;
        private final long max;

        @Override
        public <R> R match(Function<Pure<Long>, R> pureFn,
                           Function<Mapped<?, Long>, R> mappedFn,
                           Function<FlatMapped<?, Long>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Long>, R> sizedFn,
                           Function<Labeled<Long>, R> labeledFn) {
            return nextLongBetweenFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongIndex extends Instruction<Long> {
        private final long bound;

        @Override
        public <R> R match(Function<Pure<Long>, R> pureFn,
                           Function<Mapped<?, Long>, R> mappedFn,
                           Function<FlatMapped<?, Long>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Long>, R> sizedFn,
                           Function<Labeled<Long>, R> labeledFn) {
            return nextLongIndexFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextGaussian extends Instruction<Double> {
        private static final NextGaussian INSTANCE = new NextGaussian();

        @Override
        public <R> R match(Function<Pure<Double>, R> pureFn,
                           Function<Mapped<?, Double>, R> mappedFn,
                           Function<FlatMapped<?, Double>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Double>, R> sizedFn,
                           Function<Labeled<Double>, R> labeledFn) {
            return nextGaussianFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextBytes extends Instruction<Byte[]> {
        private final int count;

        @Override
        public <R> R match(Function<Pure<Byte[]>, R> pureFn,
                           Function<Mapped<?, Byte[]>, R> mappedFn,
                           Function<FlatMapped<?, Byte[]>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<Byte[]>, R> sizedFn,
                           Function<Labeled<Byte[]>, R> labeledFn) {
            return nextBytesFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Sized<A> extends Instruction<A> {
        private final Fn1<Integer, Instruction<A>> fn;

        @Override
        public <R> R match(Function<Pure<A>, R> pureFn,
                           Function<Mapped<?, A>, R> mappedFn,
                           Function<FlatMapped<?, A>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<A>, R> sizedFn,
                           Function<Labeled<A>, R> labeledFn) {
            return sizedFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Labeled<A> extends Instruction<A> {
        private final String label;
        private final Instruction<A> operand;

        @Override
        public <R> R match(Function<Pure<A>, R> pureFn,
                           Function<Mapped<?, A>, R> mappedFn,
                           Function<FlatMapped<?, A>, R> flatMappedFn,
                           Function<NextBoolean, R> nextBooleanFn,
                           Function<NextDouble, R> nextDoubleFn,
                           Function<NextFloat, R> nextFloatFn,
                           Function<NextInt, R> nextIntFn,
                           Function<NextIntExclusive, R> nextIntExclusiveFn,
                           Function<NextIntBetween, R> nextIntBetweenFn,
                           Function<NextIntIndex, R> nextIntIndexFn,
                           Function<NextLong, R> nextLongFn,
                           Function<NextLongExclusive, R> nextLongExclusiveFn,
                           Function<NextLongBetween, R> nextLongBetweenFn,
                           Function<NextLongIndex, R> nextLongIndexFn,
                           Function<NextGaussian, R> nextGaussianFn,
                           Function<NextBytes, R> nextBytesFn,
                           Function<Sized<A>, R> sizedFn,
                           Function<Labeled<A>, R> labeledFn) {
            return labeledFn.apply(this);
        }
    }

    public static <A> Pure<A> pure(A a) {
        return new Pure<>(a);
    }

    public static <A, B> Mapped<A, B> mapped(Function<A, B> fn, Instruction<A> operand) {
        return new Mapped<>(fn::apply, operand);
    }

    public static <A, B> FlatMapped<A, B> flatMapped(Function<A, Instruction<B>> fn, Instruction<A> operand) {
        return new FlatMapped<>(fn::apply, operand);
    }

    public static NextBoolean nextBoolean() {
        return NextBoolean.INSTANCE;
    }

    public static NextDouble nextDouble() {
        return NextDouble.INSTANCE;
    }

    public static NextFloat nextFloat() {
        return NextFloat.INSTANCE;
    }

    public static NextInt nextInt() {
        return NextInt.INSTANCE;
    }

    public static NextIntExclusive nextIntExclusive(int bound) {
        checkBound(bound);
        return new NextIntExclusive(bound);
    }

    public static NextIntBetween nextIntBetween(int min, int max) {
        checkMinMax(min, max);
        return new NextIntBetween(min, max);
    }

    public static NextIntIndex nextIntIndex(int bound) {
        checkBound(bound);
        return new NextIntIndex(bound);
    }

    public static NextLong nextLong() {
        return NextLong.INSTANCE;
    }

    public static NextLongExclusive nextLongExclusive(long bound) {
        checkBound(bound);
        return new NextLongExclusive(bound);
    }

    public static NextLongBetween nextLongBetween(long min, long max) {
        checkMinMax(min, max);
        return new NextLongBetween(min, max);
    }

    public static NextLongIndex nextLongIndex(long bound) {
        checkBound(bound);
        return new NextLongIndex(bound);
    }

    public static NextGaussian nextGaussian() {
        return NextGaussian.INSTANCE;
    }

    public static NextBytes nextBytes(int count) {
        checkCount(count);
        return new NextBytes(count);
    }

    public static <A> Sized<A> sized(Function<Integer, Instruction<A>> fn) {
        return new Sized<>(fn::apply);
    }

    public static <A> Labeled<A> labeled(String label, Instruction<A> operand) {
        return new Labeled<>(label, operand);
    }

    private static void checkBound(long bound) {
        if (bound < 1) throw new IllegalArgumentException("bound must be > 0");
    }

    private static void checkMinMax(long min, long max) {
        if (min > max) throw new IllegalArgumentException("max must be >= min");
    }

    private static void checkCount(int count) {
        if (count < 0) throw new IllegalArgumentException("count must be >= 0");
    }

}
