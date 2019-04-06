package dev.marksman.composablerandom.spike;

import dev.marksman.composablerandom.*;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.instructions.AggregateImpl.aggregateImpl;
import static dev.marksman.composablerandom.instructions.CustomImpl.customImpl;
import static dev.marksman.composablerandom.instructions.FlatMappedImpl.flatMappedImpl;
import static dev.marksman.composablerandom.instructions.MappedImpl.mappedImpl;
import static dev.marksman.composablerandom.instructions.NextBooleanImpl.nextBooleanImpl;
import static dev.marksman.composablerandom.instructions.NextBytesImpl.nextBytesImpl;
import static dev.marksman.composablerandom.instructions.NextDoubleImpl.nextDoubleImpl;
import static dev.marksman.composablerandom.instructions.NextFloatImpl.nextFloatImpl;
import static dev.marksman.composablerandom.instructions.NextGaussianImpl.nextGaussianImpl;
import static dev.marksman.composablerandom.instructions.NextIntBetweenImpl.nextIntBetweenImpl;
import static dev.marksman.composablerandom.instructions.NextIntBoundedImpl.nextIntBoundedImpl;
import static dev.marksman.composablerandom.instructions.NextIntExclusiveImpl.nextIntExclusiveImpl;
import static dev.marksman.composablerandom.instructions.NextIntImpl.nextIntImpl;
import static dev.marksman.composablerandom.instructions.NextIntIndexImpl.nextIntIndexImpl;
import static dev.marksman.composablerandom.instructions.NextLongBetweenImpl.nextLongBetweenImpl;
import static dev.marksman.composablerandom.instructions.NextLongBoundedImpl.nextLongBoundedImpl;
import static dev.marksman.composablerandom.instructions.NextLongExclusiveImpl.nextLongExclusiveImpl;
import static dev.marksman.composablerandom.instructions.NextLongImpl.nextLongImpl;
import static dev.marksman.composablerandom.instructions.NextLongIndexImpl.nextLongIndexImpl;
import static dev.marksman.composablerandom.instructions.Product8Impl.product8Impl;
import static dev.marksman.composablerandom.instructions.PureImpl.pureImpl;
import static dev.marksman.composablerandom.instructions.SizedImpl.sizedImpl;

public class AlternateInterpreter {
    private final SizeSelector sizeSelector;

    private AlternateInterpreter(Context context) {
        this.sizeSelector = SizeSelectors.sizeSelector(context.getSizeParameters());
    }

    public <A> CompiledGenerator<A> compile(Instruction<A> instruction) {
        if (instruction instanceof Instruction.Constant) {
            return pureImpl(((Instruction.Constant<A>) instruction).getValue());
        }

        if (instruction instanceof Instruction.Custom) {
            return customImpl(((Instruction.Custom<A>) instruction).getFn());
        }

        if (instruction instanceof Instruction.Mapped) {
            return handleMapped((Instruction.Mapped<?, A>) instruction);
        }

        if (instruction instanceof Instruction.FlatMapped) {
            return handleFlatMapped((Instruction.FlatMapped<?, A>) instruction);
        }

        if (instruction instanceof Instruction.NextInt) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntImpl();
        }

        if (instruction instanceof Instruction.NextLong) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongImpl();
        }

        if (instruction instanceof Instruction.NextBoolean) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextBooleanImpl();
        }

        if (instruction instanceof Instruction.NextDouble) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextDoubleImpl();
        }

        if (instruction instanceof Instruction.NextFloat) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextFloatImpl();
        }

        if (instruction instanceof Instruction.NextIntBounded) {
            int bound = ((Instruction.NextIntBounded) instruction).getBound();
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntBoundedImpl(bound);
        }

        if (instruction instanceof Instruction.NextIntExclusive) {
            Instruction.NextIntExclusive instruction1 = (Instruction.NextIntExclusive) instruction;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntExclusiveImpl(instruction1.getOrigin(), instruction1.getBound());
        }

        if (instruction instanceof Instruction.NextIntBetween) {
            Instruction.NextIntBetween instruction1 = (Instruction.NextIntBetween) instruction;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntBetweenImpl(instruction1.getMin(), instruction1.getMax());
        }

        if (instruction instanceof Instruction.NextIntIndex) {
            Instruction.NextIntIndex instruction1 = (Instruction.NextIntIndex) instruction;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntIndexImpl(instruction1.getBound());
        }

        if (instruction instanceof Instruction.NextLongBounded) {
            Instruction.NextLongBounded instruction1 = (Instruction.NextLongBounded) instruction;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongBoundedImpl(instruction1.getBound());
        }

        if (instruction instanceof Instruction.NextLongExclusive) {
            Instruction.NextLongExclusive instruction1 = (Instruction.NextLongExclusive) instruction;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongExclusiveImpl(instruction1.getOrigin(), instruction1.getBound());
        }

        if (instruction instanceof Instruction.NextLongBetween) {
            Instruction.NextLongBetween instruction1 = (Instruction.NextLongBetween) instruction;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongBetweenImpl(instruction1.getMin(), instruction1.getMax());
        }

        if (instruction instanceof Instruction.NextLongIndex) {
            Instruction.NextLongIndex instruction1 = (Instruction.NextLongIndex) instruction;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongIndexImpl(instruction1.getBound());
        }

        if (instruction instanceof Instruction.NextGaussian) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextGaussianImpl();
        }

        if (instruction instanceof Instruction.NextBytes) {
            Instruction.NextBytes instruction1 = (Instruction.NextBytes) instruction;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextBytesImpl(instruction1.getCount());
        }

        if (instruction instanceof Instruction.Labeled) {
            Instruction.Labeled instruction1 = (Instruction.Labeled) instruction;
            //noinspection unchecked
            return compile(instruction1.getOperand());
        }

        if (instruction instanceof Instruction.Sized) {
            Instruction.Sized instruction1 = (Instruction.Sized) instruction;

            //noinspection unchecked
            return sizedImpl(sizeSelector, rs -> compile((Instruction<A>) instruction1.getFn().apply(rs)));
        }

        if (instruction instanceof Instruction.Aggregate) {
            Instruction.Aggregate instruction1 = (Instruction.Aggregate) instruction;
            //noinspection unchecked
            Iterable<Instruction<A>> instructions = instruction1.getInstructions();

            //noinspection unchecked
            return (CompiledGenerator<A>) aggregateImpl(instruction1.getInitialBuilderSupplier(), instruction1.getAddFn(),
                    instruction1.getBuildFn(), map(this::compile, instructions));
        }

        if (instruction instanceof Instruction.Product8) {
            Instruction.Product8 instruction1 = (Instruction.Product8) instruction;
            //noinspection unchecked
            return (CompiledGenerator<A>) product8Impl(compile(instruction1.getA()),
                    compile(instruction1.getB()),
                    compile(instruction1.getC()),
                    compile(instruction1.getD()),
                    compile(instruction1.getE()),
                    compile(instruction1.getF()),
                    compile(instruction1.getG()),
                    compile(instruction1.getH()));
        }

        throw new IllegalStateException("Unimplemented instruction");
    }

    private <In, Out> CompiledGenerator<Out> handleMapped(Instruction.Mapped<In, Out> mapped) {
        return mappedImpl(mapped.getFn(), compile(mapped.getOperand()));
    }

    private <In, Out> CompiledGenerator<Out> handleFlatMapped(Instruction.FlatMapped<In, Out> flatMapped) {
        return flatMappedImpl(in -> compile(flatMapped.getFn().apply(in)),
                compile(flatMapped.getOperand()));
    }

    public static AlternateInterpreter alternateInterpreter(Context context) {
        return new AlternateInterpreter(context);
    }

}
