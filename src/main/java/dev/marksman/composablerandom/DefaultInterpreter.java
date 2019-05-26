package dev.marksman.composablerandom;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.primitives.AggregateImpl.aggregateImpl;
import static dev.marksman.composablerandom.primitives.ConstantImpl.constantImpl;
import static dev.marksman.composablerandom.primitives.CustomImpl.customImpl;
import static dev.marksman.composablerandom.primitives.FlatMappedImpl.flatMappedImpl;
import static dev.marksman.composablerandom.primitives.MappedImpl.mappedImpl;
import static dev.marksman.composablerandom.primitives.NextBooleanImpl.nextBooleanImpl;
import static dev.marksman.composablerandom.primitives.NextByteImpl.nextByteImpl;
import static dev.marksman.composablerandom.primitives.NextBytesImpl.nextBytesImpl;
import static dev.marksman.composablerandom.primitives.NextDoubleImpl.nextDoubleImpl;
import static dev.marksman.composablerandom.primitives.NextFloatImpl.nextFloatImpl;
import static dev.marksman.composablerandom.primitives.NextGaussianImpl.nextGaussianImpl;
import static dev.marksman.composablerandom.primitives.NextIntBetweenImpl.nextIntBetweenImpl;
import static dev.marksman.composablerandom.primitives.NextIntBoundedImpl.nextIntBoundedImpl;
import static dev.marksman.composablerandom.primitives.NextIntExclusiveImpl.nextIntExclusiveImpl;
import static dev.marksman.composablerandom.primitives.NextIntImpl.nextIntImpl;
import static dev.marksman.composablerandom.primitives.NextIntIndexImpl.nextIntIndexImpl;
import static dev.marksman.composablerandom.primitives.NextLongBetweenImpl.nextLongBetweenImpl;
import static dev.marksman.composablerandom.primitives.NextLongBoundedImpl.nextLongBoundedImpl;
import static dev.marksman.composablerandom.primitives.NextLongExclusiveImpl.nextLongExclusiveImpl;
import static dev.marksman.composablerandom.primitives.NextLongImpl.nextLongImpl;
import static dev.marksman.composablerandom.primitives.NextLongIndexImpl.nextLongIndexImpl;
import static dev.marksman.composablerandom.primitives.NextShortImpl.nextShortImpl;
import static dev.marksman.composablerandom.primitives.Product2Impl.product2Impl;
import static dev.marksman.composablerandom.primitives.Product3Impl.product3Impl;
import static dev.marksman.composablerandom.primitives.Product4Impl.product4Impl;
import static dev.marksman.composablerandom.primitives.Product5Impl.product5Impl;
import static dev.marksman.composablerandom.primitives.Product6Impl.product6Impl;
import static dev.marksman.composablerandom.primitives.Product7Impl.product7Impl;
import static dev.marksman.composablerandom.primitives.Product8Impl.product8Impl;
import static dev.marksman.composablerandom.primitives.SizedImpl.sizedImpl;

public class DefaultInterpreter implements Interpreter {
    @Override
    public <A> CompiledGenerator<A> handle(InterpreterContext context, Generator<A> generator) {
        if (generator instanceof Generator.Constant) {
            return constantImpl(((Generator.Constant<A>) generator).getValue());
        }

        if (generator instanceof Generator.Custom) {
            return customImpl(((Generator.Custom<A>) generator).getFn());
        }

        if (generator instanceof Generator.Mapped) {
            return handleMapped(context, (Generator.Mapped<?, A>) generator);
        }

        if (generator instanceof Generator.FlatMapped) {
            return handleFlatMapped(context, (Generator.FlatMapped<?, A>) generator);
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
            Generator.NextIntExclusive g1 = (Generator.NextIntExclusive) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (generator instanceof Generator.NextIntBetween) {
            Generator.NextIntBetween g1 = (Generator.NextIntBetween) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (generator instanceof Generator.NextIntIndex) {
            Generator.NextIntIndex g1 = (Generator.NextIntIndex) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextIntIndexImpl(g1.getBound());
        }

        if (generator instanceof Generator.NextLongBounded) {
            Generator.NextLongBounded g1 = (Generator.NextLongBounded) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongBoundedImpl(g1.getBound());
        }

        if (generator instanceof Generator.NextLongExclusive) {
            Generator.NextLongExclusive g1 = (Generator.NextLongExclusive) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (generator instanceof Generator.NextLongBetween) {
            Generator.NextLongBetween g1 = (Generator.NextLongBetween) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (generator instanceof Generator.NextLongIndex) {
            Generator.NextLongIndex g1 = (Generator.NextLongIndex) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextLongIndexImpl(g1.getBound());
        }

        if (generator instanceof Generator.NextGaussian) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextGaussianImpl();
        }

        if (generator instanceof Generator.NextByte) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextByteImpl();
        }

        if (generator instanceof Generator.NextShort) {
            //noinspection unchecked
            return (CompiledGenerator<A>) nextShortImpl();
        }

        if (generator instanceof Generator.NextBytes) {
            Generator.NextBytes g1 = (Generator.NextBytes) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) nextBytesImpl(g1.getCount());
        }

        if (generator instanceof Generator.WithMetadata) {
            Generator.WithMetadata g1 = (Generator.WithMetadata) generator;
            //noinspection unchecked
            return context.recurse(g1.getOperand());
        }

        if (generator instanceof Generator.Sized) {
            Generator.Sized g1 = (Generator.Sized) generator;

            //noinspection unchecked
            return sizedImpl(context.getParameters().getSizeSelector(),
                    rs -> context.recurse((Generator<A>) g1.getFn().apply(rs)));
        }

        if (generator instanceof Generator.Aggregate) {
            Generator.Aggregate g1 = (Generator.Aggregate) generator;
            //noinspection unchecked
            Iterable<Generator<A>> elements = g1.getElements();

            //noinspection unchecked
            return (CompiledGenerator<A>) aggregateImpl(g1.getInitialBuilderSupplier(), g1.getAddFn(),
                    g1.getBuildFn(), map(context::recurse, elements));
        }

        if (generator instanceof Generator.Product2) {
            Generator.Product2 g1 = (Generator.Product2) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) product2Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    g1.getCombine());
        }

        if (generator instanceof Generator.Product3) {
            Generator.Product3 g1 = (Generator.Product3) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) product3Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    g1.getCombine());
        }

        if (generator instanceof Generator.Product4) {
            Generator.Product4 g1 = (Generator.Product4) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) product4Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    g1.getCombine());
        }

        if (generator instanceof Generator.Product5) {
            Generator.Product5 g1 = (Generator.Product5) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) product5Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    context.recurse(g1.getE()),
                    g1.getCombine());
        }

        if (generator instanceof Generator.Product6) {
            Generator.Product6 g1 = (Generator.Product6) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) product6Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    context.recurse(g1.getE()),
                    context.recurse(g1.getF()),
                    g1.getCombine());
        }

        if (generator instanceof Generator.Product7) {
            Generator.Product7 g1 = (Generator.Product7) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) product7Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    context.recurse(g1.getE()),
                    context.recurse(g1.getF()),
                    context.recurse(g1.getG()),
                    g1.getCombine());
        }

        if (generator instanceof Generator.Product8) {
            Generator.Product8 g1 = (Generator.Product8) generator;
            //noinspection unchecked
            return (CompiledGenerator<A>) product8Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    context.recurse(g1.getE()),
                    context.recurse(g1.getF()),
                    context.recurse(g1.getG()),
                    context.recurse(g1.getH()),
                    g1.getCombine());
        }

        return context.callNextHandler(generator);
    }

    private <In, Out> CompiledGenerator<Out> handleMapped(InterpreterContext context,
                                                          Generator.Mapped<In, Out> mapped) {
        return mappedImpl(mapped.getFn(), context.recurse(mapped.getOperand()));
    }

    private <In, Out> CompiledGenerator<Out> handleFlatMapped(InterpreterContext context,
                                                              Generator.FlatMapped<In, Out> flatMapped) {
        return flatMappedImpl(in -> context.recurse(flatMapped.getFn().apply(in)),
                context.recurse(flatMapped.getOperand()));
    }

    public static DefaultInterpreter defaultInterpreter() {
        return new DefaultInterpreter();
    }

}
