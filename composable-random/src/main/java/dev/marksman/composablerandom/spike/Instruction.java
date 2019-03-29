package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.function.Function;

public abstract class Instruction<A> {

    public abstract <R> R match(Function<Pure<A>, R> pureFn,
                                Function<Mapped<?, A>, R> mappedFn);


    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pure<A> extends Instruction<A> {
        private final A value;

        @Override
        public <R> R match(Function<Pure<A>, R> pureFn, Function<Mapped<?, A>, R> mappedFn) {
            return pureFn.apply(this);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Mapped<In, Out> extends Instruction<Out> {
        private final Fn1<In, Out> fn;
        private final Instruction<In> operand;

        @Override
        public <R> R match(Function<Pure<Out>, R> pureFn, Function<Mapped<?, Out>, R> mappedFn) {
            return mappedFn.apply(this);
        }
    }

    public static <A> Instruction<A> pure(A a) {
        return new Pure<>(a);
    }

    public static <A, B> Instruction<B> mapped(Function<A, B> fn, Instruction<A> operand) {
        return new Mapped<>(fn::apply, operand);
    }

    public static <A, B> Instruction<B> flatMapped(Function<A, Instruction<B>> fn, Instruction<A> instruction) {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Boolean> nextBoolean() {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Double> nextDouble() {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Float> nextFloat() {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Integer> nextInt() {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Integer> nextIntExclusive(int bound) {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Integer> nextInt(int min, int max) {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Integer> nextIntIndex(int bound) {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Long> nextLong() {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Long> nextLongExclusive(long bound) {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Long> nextLong(long min, long max) {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Long> nextLongIndex(long bound) {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Double> nextGaussian() {
        throw new UnsupportedOperationException();
    }

    public static Instruction<Byte[]> nextBytes(int count) {
        throw new UnsupportedOperationException();
    }

    public static <A> Instruction<A> sized(Function<Integer, Instruction<A>> fn) {
        throw new UnsupportedOperationException();
    }

    public static <A> Instruction<A> labeled(String label, Instruction<A> instruction) {
        throw new UnsupportedOperationException();
    }

}
