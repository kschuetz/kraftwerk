package dev.marksman.composablerandom.legacy;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.Parameters;
import dev.marksman.composablerandom.SizeSelector;

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

public class DefaultInterpreterMark3 {
    private final SizeSelector sizeSelector;

    private DefaultInterpreterMark3(Parameters parameters) {
        this.sizeSelector = parameters.getSizeSelector();
    }

    public <A> Generator<A> compile(Generate<A> gen) {
        if (gen instanceof Generate.Constant) {
            return constantImpl(((Generate.Constant<A>) gen).getValue());
        }

        if (gen instanceof Generate.Custom) {
            return customImpl(((Generate.Custom<A>) gen).getFn());
        }

        if (gen instanceof Generate.Mapped) {
            return handleMapped((Generate.Mapped<?, A>) gen);
        }

        if (gen instanceof Generate.FlatMapped) {
            return handleFlatMapped((Generate.FlatMapped<?, A>) gen);
        }

        if (gen instanceof Generate.NextInt) {
            //noinspection unchecked
            return (Generator<A>) nextIntImpl();
        }

        if (gen instanceof Generate.NextLong) {
            //noinspection unchecked
            return (Generator<A>) nextLongImpl();
        }

        if (gen instanceof Generate.NextBoolean) {
            //noinspection unchecked
            return (Generator<A>) nextBooleanImpl();
        }

        if (gen instanceof Generate.NextDouble) {
            //noinspection unchecked
            return (Generator<A>) nextDoubleImpl();
        }

        if (gen instanceof Generate.NextFloat) {
            //noinspection unchecked
            return (Generator<A>) nextFloatImpl();
        }

        if (gen instanceof Generate.NextIntBounded) {
            int bound = ((Generate.NextIntBounded) gen).getBound();
            //noinspection unchecked
            return (Generator<A>) nextIntBoundedImpl(bound);
        }

        if (gen instanceof Generate.NextIntExclusive) {
            Generate.NextIntExclusive g1 = (Generate.NextIntExclusive) gen;
            //noinspection unchecked
            return (Generator<A>) nextIntExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Generate.NextIntBetween) {
            Generate.NextIntBetween g1 = (Generate.NextIntBetween) gen;
            //noinspection unchecked
            return (Generator<A>) nextIntBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Generate.NextIntIndex) {
            Generate.NextIntIndex g1 = (Generate.NextIntIndex) gen;
            //noinspection unchecked
            return (Generator<A>) nextIntIndexImpl(g1.getBound());
        }

        if (gen instanceof Generate.NextLongBounded) {
            Generate.NextLongBounded g1 = (Generate.NextLongBounded) gen;
            //noinspection unchecked
            return (Generator<A>) nextLongBoundedImpl(g1.getBound());
        }

        if (gen instanceof Generate.NextLongExclusive) {
            Generate.NextLongExclusive g1 = (Generate.NextLongExclusive) gen;
            //noinspection unchecked
            return (Generator<A>) nextLongExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Generate.NextLongBetween) {
            Generate.NextLongBetween g1 = (Generate.NextLongBetween) gen;
            //noinspection unchecked
            return (Generator<A>) nextLongBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Generate.NextLongIndex) {
            Generate.NextLongIndex g1 = (Generate.NextLongIndex) gen;
            //noinspection unchecked
            return (Generator<A>) nextLongIndexImpl(g1.getBound());
        }

        if (gen instanceof Generate.NextGaussian) {
            //noinspection unchecked
            return (Generator<A>) nextGaussianImpl();
        }

        if (gen instanceof Generate.NextByte) {
            //noinspection unchecked
            return (Generator<A>) nextByteImpl();
        }

        if (gen instanceof Generate.NextShort) {
            //noinspection unchecked
            return (Generator<A>) nextShortImpl();
        }

        if (gen instanceof Generate.NextBytes) {
            Generate.NextBytes g1 = (Generate.NextBytes) gen;
            //noinspection unchecked
            return (Generator<A>) nextBytesImpl(g1.getCount());
        }

        if (gen instanceof Generate.WithMetadata) {
            Generate.WithMetadata g1 = (Generate.WithMetadata) gen;
            //noinspection unchecked
            return compile(g1.getOperand());
        }

        if (gen instanceof Generate.Sized) {
            Generate.Sized g1 = (Generate.Sized) gen;

            //noinspection unchecked
            return sizedImpl(sizeSelector, rs -> compile((Generate<A>) g1.getFn().apply(rs)));
        }

        if (gen instanceof Generate.Aggregate) {
            Generate.Aggregate g1 = (Generate.Aggregate) gen;
            //noinspection unchecked
            Iterable<Generate<A>> elements = g1.getElements();

            //noinspection unchecked
            return (Generator<A>) aggregateImpl(g1.getInitialBuilderSupplier(), g1.getAddFn(),
                    g1.getBuildFn(), map(this::compile, elements));
        }

        if (gen instanceof Generate.Product2) {
            Generate.Product2 g1 = (Generate.Product2) gen;
            //noinspection unchecked
            return (Generator<A>) product2Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    g1.getCombine());
        }

        if (gen instanceof Generate.Product3) {
            Generate.Product3 g1 = (Generate.Product3) gen;
            //noinspection unchecked
            return (Generator<A>) product3Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    g1.getCombine());
        }

        if (gen instanceof Generate.Product4) {
            Generate.Product4 g1 = (Generate.Product4) gen;
            //noinspection unchecked
            return (Generator<A>) product4Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    g1.getCombine());
        }

        if (gen instanceof Generate.Product5) {
            Generate.Product5 g1 = (Generate.Product5) gen;
            //noinspection unchecked
            return (Generator<A>) product5Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    g1.getCombine());
        }

        if (gen instanceof Generate.Product6) {
            Generate.Product6 g1 = (Generate.Product6) gen;
            //noinspection unchecked
            return (Generator<A>) product6Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    compile(g1.getF()),
                    g1.getCombine());
        }

        if (gen instanceof Generate.Product7) {
            Generate.Product7 g1 = (Generate.Product7) gen;
            //noinspection unchecked
            return (Generator<A>) product7Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    compile(g1.getF()),
                    compile(g1.getG()),
                    g1.getCombine());
        }

        if (gen instanceof Generate.Product8) {
            Generate.Product8 g1 = (Generate.Product8) gen;
            //noinspection unchecked
            return (Generator<A>) product8Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    compile(g1.getF()),
                    compile(g1.getG()),
                    compile(g1.getH()),
                    g1.getCombine());
        }

        throw new IllegalStateException("Unimplemented gen");
    }

    private <In, Out> Generator<Out> handleMapped(Generate.Mapped<In, Out> mapped) {
        return mappedImpl(mapped.getFn(), compile(mapped.getOperand()));
    }

    private <In, Out> Generator<Out> handleFlatMapped(Generate.FlatMapped<In, Out> flatMapped) {
        return flatMappedImpl(in -> compile(flatMapped.getFn().apply(in)),
                compile(flatMapped.getOperand()));
    }

    public static DefaultInterpreterMark3 defaultInterpreter(Parameters parameters) {
        return new DefaultInterpreterMark3(parameters);
    }

}
