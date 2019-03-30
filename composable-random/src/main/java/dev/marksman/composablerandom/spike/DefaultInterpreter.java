package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AllArgsConstructor;

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
                this::handleNextIntExclusive,
                this::handleNextIntBetween,
                this::handleNextIntIndex,
                HANDLE_NEXT_LONG,
                this::handleNextLongExclusive,
                this::handleNextLongBetween,
                this::handleNextLongIndex,
                HANDLE_NEXT_GAUSSIAN,
                this::handleNextBytes,
                this::handleSized,
                this::handleLabeled);
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

    private Fn1<RandomState, Result<? extends RandomState, Integer>> handleNextIntExclusive(HasIntExclusiveBound instruction) {
        return input -> input.nextInt(instruction.getBound());
    }

    private Fn1<RandomState, Result<? extends RandomState, Integer>> handleNextIntBetween(HasIntInclusiveRange instruction) {
        return null;
    }

    private Fn1<RandomState, Result<? extends RandomState, Integer>> handleNextIntIndex(HasIntExclusiveBound instruction) {
        return null;
    }

    private Fn1<RandomState, Result<? extends RandomState, Long>> handleNextLongExclusive(HasLongExclusiveBound instruction) {
        return null;
    }

    private Fn1<RandomState, Result<? extends RandomState, Long>> handleNextLongBetween(HasLongInclusiveRange instruction) {
        return null;
    }

    private Fn1<RandomState, Result<? extends RandomState, Long>> handleNextLongIndex(HasLongExclusiveBound instruction) {
        return null;
    }

    private Fn1<RandomState, Result<? extends RandomState, Byte[]>> handleNextBytes(HasIntCount instruction) {
        return null;
    }

    private <A> Fn1<RandomState, Result<? extends RandomState, A>> handleSized(Instruction.Sized<A> instruction) {
        //TODO: sized
//        return execute(input, fn.apply(5));
        return null;
    }

    private <A> Fn1<RandomState, Result<? extends RandomState, A>> handleLabeled(Instruction.Labeled<A> instruction) {
//        return execute(input, operand);
        return null;
    }

}
