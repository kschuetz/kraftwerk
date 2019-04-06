package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn2;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.StandardContext.defaultContext;

public class DefaultInterpreter {
    private final SizeSelector sizeSelector;

    private DefaultInterpreter(Context context) {
        this.sizeSelector = SizeSelectors.sizeSelector(context.getSizeParameters());
    }

    public <A, R> Result<RandomState, R> execute(RandomState input, Instruction<A> instruction) {

        if (instruction instanceof Instruction.Constant) {
            //noinspection unchecked
            return (Result<RandomState, R>) result(input, (((Instruction.Constant) instruction).getValue()));
        }

        if (instruction instanceof Instruction.Custom) {
            Instruction.Custom instruction1 = (Instruction.Custom) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) instruction1.getFn().apply(input);
        }

        if (instruction instanceof Instruction.Mapped) {
            Instruction.Mapped mapped = (Instruction.Mapped) instruction;
            //noinspection unchecked
            return execute(input, mapped.getOperand()).fmap(mapped.getFn());
        }

        if (instruction instanceof Instruction.FlatMapped) {
            //noinspection unchecked
            return handleFlatMapped((Instruction.FlatMapped) instruction, input);
        }

        if (instruction instanceof Instruction.NextInt) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextInt();
        }

        if (instruction instanceof Instruction.NextLong) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLong();
        }

        if (instruction instanceof Instruction.NextBoolean) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextBoolean();
        }

        if (instruction instanceof Instruction.NextDouble) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextDouble();
        }

        if (instruction instanceof Instruction.NextFloat) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextFloat();
        }

        if (instruction instanceof Instruction.NextIntBounded) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextIntBounded(((Instruction.NextIntBounded) instruction).getBound());
        }

        if (instruction instanceof Instruction.NextIntExclusive) {
            Instruction.NextIntExclusive instruction1 = (Instruction.NextIntExclusive) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextIntExclusive(instruction1.getOrigin(), instruction1.getBound());
        }

        if (instruction instanceof Instruction.NextIntBetween) {
            Instruction.NextIntBetween instruction1 = (Instruction.NextIntBetween) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextIntBetween(instruction1.getMin(), instruction1.getMax());
        }

        if (instruction instanceof Instruction.NextIntIndex) {
            Instruction.NextIntIndex instruction1 = (Instruction.NextIntIndex) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextIntBounded(instruction1.getBound());
        }

        if (instruction instanceof Instruction.NextLongBounded) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLongBounded(((Instruction.NextLongBounded) instruction).getBound());
        }

        if (instruction instanceof Instruction.NextLongExclusive) {
            Instruction.NextLongExclusive instruction1 = (Instruction.NextLongExclusive) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLongExclusive(instruction1.getOrigin(), instruction1.getBound());
        }

        if (instruction instanceof Instruction.NextLongBetween) {
            Instruction.NextLongBetween instruction1 = (Instruction.NextLongBetween) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLongBetween(instruction1.getMin(), instruction1.getMax());
        }

        if (instruction instanceof Instruction.NextLongIndex) {
            Instruction.NextLongIndex instruction1 = (Instruction.NextLongIndex) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLongBounded(instruction1.getBound());
        }

        if (instruction instanceof Instruction.NextGaussian) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextGaussian();
        }

        if (instruction instanceof Instruction.NextBytes) {
            Instruction.NextBytes instruction1 = (Instruction.NextBytes) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) handleNextBytes(instruction1, input);
        }

        if (instruction instanceof Instruction.Labeled) {
            Instruction.Labeled instruction1 = (Instruction.Labeled) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) handleLabeled(instruction1, input);
        }

        if (instruction instanceof Instruction.Sized) {
            Instruction.Sized instruction1 = (Instruction.Sized) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) handleSized(instruction1, input);
        }

        if (instruction instanceof Instruction.Aggregate) {
            Instruction.Aggregate instruction1 = (Instruction.Aggregate) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) handleAggregate(instruction1, input);
        }

        if (instruction instanceof Instruction.Product8) {
            Instruction.Product8 instruction1 = (Instruction.Product8) instruction;
            //noinspection unchecked
            return (Result<RandomState, R>) handleProduct8(instruction1, input);
        }

        throw new IllegalStateException("Unimplemented instruction");
    }

    private <In, Out> Result<? extends RandomState, Out> handleFlatMapped(Instruction.FlatMapped<In, Out> flatMapped, RandomState input) {
        Result<RandomState, In> result1 = execute(input, flatMapped.getOperand());
        return execute(result1.getNextState(),
                flatMapped.getFn().apply(result1.getValue()));
    }

    private Result<? extends RandomState, Byte[]> handleNextBytes(HasIntCount instruction, RandomState input) {
        final int count = Math.max(instruction.getCount(), 0);
        byte[] buffer = new byte[count];
        Result<? extends RandomState, Unit> next = input.nextBytes(buffer);
        Byte[] result = new Byte[count];
        int i = 0;
        for (byte b : buffer) {
            result[i++] = b;
        }
        return next.fmap(__ -> result);
    }

    private <A> Result<? extends RandomState, A> handleSized(Instruction.Sized<A> instruction, RandomState input) {
        Result<? extends RandomState, Integer> sizeResult = sizeSelector.selectSize(input);
        return execute(sizeResult.getNextState(), instruction.getFn().apply(sizeResult.getValue()));
    }

    private <A> Result<? extends RandomState, A> handleLabeled(Instruction.Labeled<A> instruction, RandomState input) {
        return execute(input, instruction.getOperand());
    }

    private <A, B, R> Result<? extends RandomState, R> handleAggregate(Instruction.Aggregate<A, B, R> aggregate, RandomState input) {
        RandomState current = input;
        B builder = aggregate.getInitialBuilderSupplier().get();
        Fn2<B, A, B> addFn = aggregate.getAddFn();
        for (Instruction<A> instruction : aggregate.getInstructions()) {
            Result<RandomState, A> next = execute(current, instruction);
            builder = addFn.apply(builder, next.getValue());
            current = next.getNextState();
        }
        return result(current, aggregate.getBuildFn().apply(builder));
    }

    private <A, B, C, D, E, F, G, H> Result<? extends RandomState, Tuple8<A, B, C, D, E, F, G, H>> handleProduct8(Instruction.Product8<A, B, C, D, E, F, G, H> instruction, RandomState input) {
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
    }

    public static DefaultInterpreter defaultInterpreter() {
        return new DefaultInterpreter(defaultContext());
    }

    public static DefaultInterpreter defaultInterpreter(Context context) {
        return new DefaultInterpreter(context);
    }

}
