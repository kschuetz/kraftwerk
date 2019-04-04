package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

import static dev.marksman.composablerandom.Result.result;

public class AlternateInterpreter {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecutePure<A> implements Interpreter<A> {
        private final A value;

        @Override
        public Result<RandomState, A> execute(RandomState input) {
            return result(input, value);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteCustom<A> implements Interpreter<A> {
        private final Fn1<? super RandomState, Result<RandomState, A>> fn;

        @Override
        public Result<? extends RandomState, A> execute(RandomState input) {
            return fn.apply(input);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteMapped<In, A> implements Interpreter<A> {
        private final Fn1<In, A> fn;
        private final Interpreter<In> operand;

        @Override
        public Result<? extends RandomState, A> execute(RandomState input) {
            return operand.execute(input).fmap(fn);
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteFlatMapped<In, A> implements Interpreter<A> {
        private final Fn1<? super In, ? extends Interpreter<A>> fn;
        private final Interpreter<In> operand;

        @Override
        public Result<? extends RandomState, A> execute(RandomState input) {
            Result<? extends RandomState, In> result1 = operand.execute(input);
            return fn.apply(result1.getValue())
                    .execute(result1.getNextState());
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextInt implements Interpreter<Integer> {
        @Override
        public Result<? extends RandomState, Integer> execute(RandomState input) {
            return input.nextInt();
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextLong implements Interpreter<Long> {
        @Override
        public Result<? extends RandomState, Long> execute(RandomState input) {
            return input.nextLong();
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextBoolean implements Interpreter<Boolean> {
        @Override
        public Result<? extends RandomState, Boolean> execute(RandomState input) {
            return input.nextBoolean();
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextDouble implements Interpreter<Double> {
        @Override
        public Result<? extends RandomState, Double> execute(RandomState input) {
            return input.nextDouble();
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextFloat implements Interpreter<Float> {
        @Override
        public Result<? extends RandomState, Float> execute(RandomState input) {
            return input.nextFloat();
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextIntBounded implements Interpreter<Integer> {
        private final int bound;

        @Override
        public Result<? extends RandomState, Integer> execute(RandomState input) {
            return input.nextIntBounded(bound);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextIntExclusive implements Interpreter<Integer> {
        private final int origin;
        private final int bound;

        @Override
        public Result<? extends RandomState, Integer> execute(RandomState input) {
            return input.nextIntExclusive(origin, bound);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextIntBetween implements Interpreter<Integer> {
        private final int min;
        private final int max;

        @Override
        public Result<? extends RandomState, Integer> execute(RandomState input) {
            return input.nextIntBetween(min, max);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextIntIndex implements Interpreter<Integer> {
        private final int bound;

        @Override
        public Result<? extends RandomState, Integer> execute(RandomState input) {
            return input.nextIntBounded(bound);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextLongBounded implements Interpreter<Long> {
        private final long bound;

        @Override
        public Result<? extends RandomState, Long> execute(RandomState input) {
            return input.nextLongBounded(bound);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextLongExclusive implements Interpreter<Long> {
        private final long origin;
        private final long bound;

        @Override
        public Result<? extends RandomState, Long> execute(RandomState input) {
            return input.nextLongExclusive(origin, bound);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextLongBetween implements Interpreter<Long> {
        private final long min;
        private final long max;

        @Override
        public Result<? extends RandomState, Long> execute(RandomState input) {
            return input.nextLongBetween(min, max);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteNextLongIndex implements Interpreter<Long> {
        private final long bound;

        @Override
        public Result<? extends RandomState, Long> execute(RandomState input) {
            return input.nextLongBounded(bound);
        }
    }


    // -----

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExecuteAggregate<Elem, Builder, Output> implements Interpreter<Output> {
        private final Supplier<Builder> initialBuilderSupplier;
        private final Fn2<Builder, Elem, Builder> addFn;
        private final Fn1<Builder, Output> buildFn;
        private final Iterable<Interpreter<Elem>> instructions;

        @Override
        public Result<? extends RandomState, Output> execute(RandomState input) {
            RandomState current = input;
            Builder builder = initialBuilderSupplier.get();

            for (Interpreter<Elem> instruction : instructions) {
                Result<? extends RandomState, Elem> next = instruction.execute(current);
                builder = addFn.apply(builder, next.getValue());
                current = next.getNextState();
            }
            return result(current, buildFn.apply(builder));
        }
    }

}
