package dev.marksman.composablerandom;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.instructions.AggregateImpl.aggregateImpl;
import static dev.marksman.composablerandom.instructions.ConstantImpl.constantImpl;
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
import static dev.marksman.composablerandom.instructions.SizedImpl.sizedImpl;

public class DefaultInterpreter {
    private final SizeSelector sizeSelector;

    private DefaultInterpreter(Context context) {
        this.sizeSelector = SizeSelectors.sizeSelector(context.getSizeParameters());
    }

    public <A> CompiledGenerator<A> compile(Generator<A> generator) {
        if (generator instanceof Generator.Constant) {
            return constantImpl(((Generator.Constant<A>) generator).getValue());
        }

        if (generator instanceof Generator.Custom) {
            return customImpl(((Generator.Custom<A>) generator).getFn());
        }

        if (generator instanceof Generator.Mapped) {
            return handleMapped((Generator.Mapped<?, A>) generator);
        }

        if (generator instanceof Generator.FlatMapped) {
            return handleFlatMapped((Generator.FlatMapped<?, A>) generator);
        }

        if (generator instanceof Generator.NextInt) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntImpl();
        }

        if (generator instanceof Generator.NextLong) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongImpl();
        }

        if (generator instanceof Generator.NextBoolean) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextBooleanImpl();
        }

        if (generator instanceof Generator.NextDouble) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextDoubleImpl();
        }

        if (generator instanceof Generator.NextFloat) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextFloatImpl();
        }

        if (generator instanceof Generator.NextIntBounded) {
            int bound = ((Generator.NextIntBounded) generator).getBound();
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntBoundedImpl(bound);
        }

        if (generator instanceof Generator.NextIntExclusive) {
            Generator.NextIntExclusive instruction1 = (Generator.NextIntExclusive) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntExclusiveImpl(instruction1.getOrigin(), instruction1.getBound());
        }

        if (generator instanceof Generator.NextIntBetween) {
            Generator.NextIntBetween instruction1 = (Generator.NextIntBetween) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntBetweenImpl(instruction1.getMin(), instruction1.getMax());
        }

        if (generator instanceof Generator.NextIntIndex) {
            Generator.NextIntIndex instruction1 = (Generator.NextIntIndex) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntIndexImpl(instruction1.getBound());
        }

        if (generator instanceof Generator.NextLongBounded) {
            Generator.NextLongBounded instruction1 = (Generator.NextLongBounded) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongBoundedImpl(instruction1.getBound());
        }

        if (generator instanceof Generator.NextLongExclusive) {
            Generator.NextLongExclusive instruction1 = (Generator.NextLongExclusive) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongExclusiveImpl(instruction1.getOrigin(), instruction1.getBound());
        }

        if (generator instanceof Generator.NextLongBetween) {
            Generator.NextLongBetween instruction1 = (Generator.NextLongBetween) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongBetweenImpl(instruction1.getMin(), instruction1.getMax());
        }

        if (generator instanceof Generator.NextLongIndex) {
            Generator.NextLongIndex instruction1 = (Generator.NextLongIndex) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongIndexImpl(instruction1.getBound());
        }

        if (generator instanceof Generator.NextGaussian) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextGaussianImpl();
        }

        if (generator instanceof Generator.NextBytes) {
            Generator.NextBytes instruction1 = (Generator.NextBytes) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextBytesImpl(instruction1.getCount());
        }

        if (generator instanceof Generator.Labeled) {
            Generator.Labeled instruction1 = (Generator.Labeled) generator;
            //noinspection unchecked
            return compile(instruction1.getOperand());
        }

        if (generator instanceof Generator.AttachApplicationData) {
            Generator.AttachApplicationData instruction1 = (Generator.AttachApplicationData) generator;
            //noinspection unchecked
            return compile(instruction1.getOperand());
        }

        if (generator instanceof Generator.Sized) {
            Generator.Sized instruction1 = (Generator.Sized) generator;

            //noinspection unchecked
            return sizedImpl(sizeSelector, rs -> compile((Generator<A>) instruction1.getFn().apply(rs)));
        }

        if (generator instanceof Generator.Aggregate) {
            Generator.Aggregate instruction1 = (Generator.Aggregate) generator;
            //noinspection unchecked
            Iterable<Generator<A>> instructions = instruction1.getInstructions();

            //noinspection unchecked
            return (CompiledGenerator<A>) aggregateImpl(instruction1.getInitialBuilderSupplier(), instruction1.getAddFn(),
                    instruction1.getBuildFn(), map(this::compile, instructions));
        }

        if (generator instanceof Generator.Product8) {
            Generator.Product8 instruction1 = (Generator.Product8) generator;
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

        throw new IllegalStateException("Unimplemented generator");
    }

    private <In, Out> CompiledGenerator<Out> handleMapped(Generator.Mapped<In, Out> mapped) {
        return mappedImpl(mapped.getFn(), compile(mapped.getOperand()));
    }

    private <In, Out> CompiledGenerator<Out> handleFlatMapped(Generator.FlatMapped<In, Out> flatMapped) {
        return flatMappedImpl(in -> compile(flatMapped.getFn().apply(in)),
                compile(flatMapped.getOperand()));
    }

    public static DefaultInterpreter defaultInterpreter(Context context) {
        return new DefaultInterpreter(context);
    }

}
