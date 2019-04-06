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

    public <A, R> Result<RandomState, R> execute(RandomState input, Generator<A> generator) {

        if (generator instanceof Generator.Constant) {
            //noinspection unchecked
            return (Result<RandomState, R>) result(input, (((Generator.Constant) generator).getValue()));
        }

        if (generator instanceof Generator.Custom) {
            Generator.Custom instruction1 = (Generator.Custom) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) instruction1.getFn().apply(input);
        }

        if (generator instanceof Generator.Mapped) {
            Generator.Mapped mapped = (Generator.Mapped) generator;
            //noinspection unchecked
            return execute(input, mapped.getOperand()).fmap(mapped.getFn());
        }

        if (generator instanceof Generator.FlatMapped) {
            //noinspection unchecked
            return handleFlatMapped((Generator.FlatMapped) generator, input);
        }

        if (generator instanceof Generator.NextInt) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextInt();
        }

        if (generator instanceof Generator.NextLong) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLong();
        }

        if (generator instanceof Generator.NextBoolean) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextBoolean();
        }

        if (generator instanceof Generator.NextDouble) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextDouble();
        }

        if (generator instanceof Generator.NextFloat) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextFloat();
        }

        if (generator instanceof Generator.NextIntBounded) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextIntBounded(((Generator.NextIntBounded) generator).getBound());
        }

        if (generator instanceof Generator.NextIntExclusive) {
            Generator.NextIntExclusive instruction1 = (Generator.NextIntExclusive) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextIntExclusive(instruction1.getOrigin(), instruction1.getBound());
        }

        if (generator instanceof Generator.NextIntBetween) {
            Generator.NextIntBetween instruction1 = (Generator.NextIntBetween) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextIntBetween(instruction1.getMin(), instruction1.getMax());
        }

        if (generator instanceof Generator.NextIntIndex) {
            Generator.NextIntIndex instruction1 = (Generator.NextIntIndex) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextIntBounded(instruction1.getBound());
        }

        if (generator instanceof Generator.NextLongBounded) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLongBounded(((Generator.NextLongBounded) generator).getBound());
        }

        if (generator instanceof Generator.NextLongExclusive) {
            Generator.NextLongExclusive instruction1 = (Generator.NextLongExclusive) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLongExclusive(instruction1.getOrigin(), instruction1.getBound());
        }

        if (generator instanceof Generator.NextLongBetween) {
            Generator.NextLongBetween instruction1 = (Generator.NextLongBetween) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLongBetween(instruction1.getMin(), instruction1.getMax());
        }

        if (generator instanceof Generator.NextLongIndex) {
            Generator.NextLongIndex instruction1 = (Generator.NextLongIndex) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextLongBounded(instruction1.getBound());
        }

        if (generator instanceof Generator.NextGaussian) {
            //noinspection unchecked
            return (Result<RandomState, R>) input.nextGaussian();
        }

        if (generator instanceof Generator.NextBytes) {
            Generator.NextBytes instruction1 = (Generator.NextBytes) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) handleNextBytes(instruction1, input);
        }

        if (generator instanceof Generator.Labeled) {
            Generator.Labeled instruction1 = (Generator.Labeled) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) handleLabeled(instruction1, input);
        }

        if (generator instanceof Generator.Sized) {
            Generator.Sized instruction1 = (Generator.Sized) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) handleSized(instruction1, input);
        }

        if (generator instanceof Generator.Aggregate) {
            Generator.Aggregate instruction1 = (Generator.Aggregate) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) handleAggregate(instruction1, input);
        }

        if (generator instanceof Generator.Product8) {
            Generator.Product8 instruction1 = (Generator.Product8) generator;
            //noinspection unchecked
            return (Result<RandomState, R>) handleProduct8(instruction1, input);
        }

        throw new IllegalStateException("Unimplemented generator");
    }

    private <In, Out> Result<? extends RandomState, Out> handleFlatMapped(Generator.FlatMapped<In, Out> flatMapped, RandomState input) {
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

    private <A> Result<? extends RandomState, A> handleSized(Generator.Sized<A> instruction, RandomState input) {
        Result<? extends RandomState, Integer> sizeResult = sizeSelector.selectSize(input);
        return execute(sizeResult.getNextState(), instruction.getFn().apply(sizeResult.getValue()));
    }

    private <A> Result<? extends RandomState, A> handleLabeled(Generator.Labeled<A> instruction, RandomState input) {
        return execute(input, instruction.getOperand());
    }

    private <A, B, R> Result<? extends RandomState, R> handleAggregate(Generator.Aggregate<A, B, R> aggregate, RandomState input) {
        RandomState current = input;
        B builder = aggregate.getInitialBuilderSupplier().get();
        Fn2<B, A, B> addFn = aggregate.getAddFn();
        for (Generator<A> generator : aggregate.getInstructions()) {
            Result<RandomState, A> next = execute(current, generator);
            builder = addFn.apply(builder, next.getValue());
            current = next.getNextState();
        }
        return result(current, aggregate.getBuildFn().apply(builder));
    }

    private <A, B, C, D, E, F, G, H> Result<? extends RandomState, Tuple8<A, B, C, D, E, F, G, H>> handleProduct8(Generator.Product8<A, B, C, D, E, F, G, H> instruction, RandomState input) {
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
