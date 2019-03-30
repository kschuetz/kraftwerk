package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor()
public class DefaultInterpreter {

    private static final Fn1<Instruction.NextBoolean, Fn1<RandomState, ? extends Result<? extends RandomState, ?>>>
            HANDLE_NEXT_BOOLEAN = constantly(RandomState::nextBoolean);

    private static final Fn1<Instruction.NextDouble, Fn1<RandomState, ? extends Result<? extends RandomState, ?>>>
            HANDLE_NEXT_DOUBLE = constantly(RandomState::nextDouble);

    private static final Fn1<Instruction.NextFloat, Fn1<RandomState, ? extends Result<? extends RandomState, ?>>>
            HANDLE_NEXT_FLOAT = constantly(RandomState::nextFloat);

    private static final Fn1<Instruction.NextInt, Fn1<RandomState, ? extends Result<? extends RandomState, ?>>>
            HANDLE_NEXT_INT = constantly(RandomState::nextInt);

    private static final Fn1<Instruction.NextLong, Fn1<RandomState, ? extends Result<? extends RandomState, ?>>>
            HANDLE_NEXT_LONG = constantly(RandomState::nextLong);

    private static final Fn1<Instruction.NextGaussian, Fn1<RandomState, ? extends Result<? extends RandomState, ?>>>
            HANDLE_NEXT_GAUSSIAN = constantly(RandomState::nextGaussian);

    public <A> Result<RandomState, A> execute(RandomState input, Instruction<A> instruction) {

        Fn1<RandomState, ? extends Result<? extends RandomState, ?>> inputFn = instruction.match(
                _pure -> _in -> result(_in, _pure.getValue()),
                this::handleMapped,
                this::handleFlatMapped,
                HANDLE_NEXT_BOOLEAN,
                HANDLE_NEXT_DOUBLE,
                HANDLE_NEXT_FLOAT,
                HANDLE_NEXT_INT,
                this::handleNextIntBounded,
                null,  // TODO: nextIntExclusive
                this::handleNextIntBetween,
                this::handleNextIntIndex,
                HANDLE_NEXT_LONG,
                this::handleNextLongBounded,
                null,  // TODO: nextLongExclusive
                this::handleNextLongBetween,
                this::handleNextLongIndex,
                HANDLE_NEXT_GAUSSIAN,
                this::handleNextBytes,
                this::handleSized,
                this::handleLabeled,
                this::handleProduct8);
        Result<? extends RandomState, ?> result = inputFn.apply(input);

        //noinspection unchecked
        return (Result<RandomState, A>) result;
    }

    private <In, Out> Fn1<RandomState, Result<? extends RandomState, Out>> handleMapped(Instruction.Mapped<In, Out> mapped) {
        return input -> execute(input, mapped.getOperand())
                .fmap(mapped.getFn());
    }

    private <In, Out> Fn1<RandomState, Result<? extends RandomState, Out>> handleFlatMapped(Instruction.FlatMapped<In, Out> flatMapped) {
        return input -> execute(execute(input, flatMapped.getOperand()).getNextState(),
                flatMapped.getFn()
                        .apply(execute(input, flatMapped.getOperand())
                                .getValue()));
    }

    private Fn1<RandomState, Result<? extends RandomState, Integer>> handleNextIntBounded(HasIntExclusiveBound instruction) {
        return input -> input.nextIntBounded(instruction.getBound());
    }

    private Fn1<RandomState, Result<? extends RandomState, Integer>> handleNextIntBetween(HasIntInclusiveRange instruction) {
        return input -> input.nextIntBetween(instruction.getMin(), instruction.getMax());
    }

    private Fn1<RandomState, Result<? extends RandomState, Integer>> handleNextIntIndex(HasIntExclusiveBound instruction) {
        return handleNextIntBounded(instruction);
    }

    private Fn1<RandomState, Result<? extends RandomState, Long>> handleNextLongBounded(HasLongExclusiveBound instruction) {
        return input -> input.nextLongBounded(instruction.getBound());
    }

    private Fn1<RandomState, Result<? extends RandomState, Long>> handleNextLongBetween(HasLongInclusiveRange instruction) {
        return input -> input.nextLongBetween(instruction.getMin(), instruction.getMax());
    }

    private Fn1<RandomState, Result<? extends RandomState, Long>> handleNextLongIndex(HasLongExclusiveBound instruction) {
        return handleNextLongBounded(instruction);
    }

    private Fn1<RandomState, Result<? extends RandomState, Byte[]>> handleNextBytes(HasIntCount instruction) {
        final int count = Math.max(instruction.getCount(), 0);
        return (RandomState input) -> {
            byte[] buffer = new byte[count];
            Result<? extends RandomState, Unit> next = input.nextBytes(buffer);
            Byte[] result = new Byte[count];
            int i = 0;
            for (byte b : buffer) {
                result[i++] = b;
            }
            return next.fmap(__ -> result);
        };
    }

    private <A> Fn1<RandomState, Result<? extends RandomState, A>> handleSized(Instruction.Sized<A> instruction) {
        //TODO: sized
        return input -> execute(input, instruction.getFn().apply(5));
    }

    private <A> Fn1<RandomState, Result<? extends RandomState, A>> handleLabeled(Instruction.Labeled<A> instruction) {
        return input -> execute(input, instruction.getOperand());
    }

    private <A, B, C, D, E, F, G, H> Fn1<RandomState, Result<? extends RandomState, Tuple8<A, B, C, D, E, F, G, H>>> handleProduct8(Instruction.Product8<A, B, C, D, E, F, G, H> instruction) {
        return input -> {
            Result<RandomState, A> r1 = execute(input, instruction.getA());
            Result<RandomState, B> r2 = execute(r1.getNextState(), instruction.getB());
            Result<RandomState, C> r3 = execute(r2.getNextState(), instruction.getC());
            Result<RandomState, D> r4 = execute(r3.getNextState(), instruction.getD());
            Result<RandomState, E> r5 = execute(r4.getNextState(), instruction.getE());
            Result<RandomState, F> r6 = execute(r5.getNextState(), instruction.getF());
            Result<RandomState, G> r7 = execute(r6.getNextState(), instruction.getG());
            Result<RandomState, H> r8 = execute(r7.getNextState(), instruction.getH());
            Tuple8<A, B, C, D, E, F, G, H> result = tuple(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                    r5.getValue(), r6.getValue(), r7.getValue(), r8.getValue());
            return result(r8.getNextState(), result);
        };
    }

}
