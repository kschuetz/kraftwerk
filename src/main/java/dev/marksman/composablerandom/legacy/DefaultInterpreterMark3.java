package dev.marksman.composablerandom.legacy;

import dev.marksman.composablerandom.*;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.legacy.primitives.AggregateImpl.aggregateImpl;
import static dev.marksman.composablerandom.legacy.primitives.ConstantImpl.constantImpl;
import static dev.marksman.composablerandom.legacy.primitives.CustomImpl.customImpl;
import static dev.marksman.composablerandom.legacy.primitives.FlatMappedImpl.flatMappedImpl;
import static dev.marksman.composablerandom.legacy.primitives.MappedImpl.mappedImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextBooleanImpl.nextBooleanImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextByteImpl.nextByteImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextBytesImpl.nextBytesImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextDoubleImpl.nextDoubleImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextFloatImpl.nextFloatImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextGaussianImpl.nextGaussianImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextIntBetweenImpl.nextIntBetweenImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextIntBoundedImpl.nextIntBoundedImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextIntExclusiveImpl.nextIntExclusiveImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextIntImpl.nextIntImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextIntIndexImpl.nextIntIndexImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextLongBetweenImpl.nextLongBetweenImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextLongBoundedImpl.nextLongBoundedImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextLongExclusiveImpl.nextLongExclusiveImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextLongImpl.nextLongImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextLongIndexImpl.nextLongIndexImpl;
import static dev.marksman.composablerandom.legacy.primitives.NextShortImpl.nextShortImpl;
import static dev.marksman.composablerandom.legacy.primitives.Product2Impl.product2Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product3Impl.product3Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product4Impl.product4Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product5Impl.product5Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product6Impl.product6Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product7Impl.product7Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product8Impl.product8Impl;
import static dev.marksman.composablerandom.legacy.primitives.SizedImpl.sizedImpl;

public class DefaultInterpreterMark3 {
    private final SizeSelector sizeSelector;

    private DefaultInterpreterMark3(Parameters parameters) {
        this.sizeSelector = parameters.getSizeSelector();
    }

    public <A> GeneratorImpl<A> compile(Generator<A> gen) {
        if (gen instanceof Primitives.ConstantGenerator) {
            return constantImpl(((Primitives.ConstantGenerator<A>) gen).getValue());
        }

        if (gen instanceof Generator.Custom) {
            return customImpl(((Generator.Custom<A>) gen).getFn());
        }

        if (gen instanceof Primitives.Mapped) {
            return handleMapped((Primitives.Mapped<?, A>) gen);
        }

        if (gen instanceof Primitives.FlatMapped) {
            return handleFlatMapped((Primitives.FlatMapped<?, A>) gen);
        }

        if (gen instanceof Primitives.IntGenerator) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntImpl();
        }

        if (gen instanceof Primitives.LongGenerator) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongImpl();
        }

        if (gen instanceof Primitives.BooleanGenerator) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextBooleanImpl();
        }

        if (gen instanceof Primitives.DoubleGenerator) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextDoubleImpl();
        }

        if (gen instanceof Primitives.FloatGenerator) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextFloatImpl();
        }

        if (gen instanceof Primitives.IntBoundedGenerator) {
            int bound = ((Primitives.IntBoundedGenerator) gen).getBound();
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntBoundedImpl(bound);
        }

        if (gen instanceof Primitives.IntExclusiveGenerator) {
            Primitives.IntExclusiveGenerator g1 = (Primitives.IntExclusiveGenerator) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Primitives.IntBetweenGenerator) {
            Primitives.IntBetweenGenerator g1 = (Primitives.IntBetweenGenerator) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Primitives.IntIndexGenerator) {
            Primitives.IntIndexGenerator g1 = (Primitives.IntIndexGenerator) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntIndexImpl(g1.getBound());
        }

        if (gen instanceof Primitives.LongBoundedGenerator) {
            Primitives.LongBoundedGenerator g1 = (Primitives.LongBoundedGenerator) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongBoundedImpl(g1.getBound());
        }

        if (gen instanceof Primitives.LongExclusiveGenerator) {
            Primitives.LongExclusiveGenerator g1 = (Primitives.LongExclusiveGenerator) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Primitives.LongBetweenGenerator) {
            Primitives.LongBetweenGenerator g1 = (Primitives.LongBetweenGenerator) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Primitives.LongIndexGenerator) {
            Primitives.LongIndexGenerator g1 = (Primitives.LongIndexGenerator) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongIndexImpl(g1.getBound());
        }

        if (gen instanceof Primitives.GaussianGenerator) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextGaussianImpl();
        }

        if (gen instanceof Primitives.ByteGenerator) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextByteImpl();
        }

        if (gen instanceof Primitives.ShortGenerator) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextShortImpl();
        }

        if (gen instanceof Primitives.BytesGenerator) {
            Primitives.BytesGenerator g1 = (Primitives.BytesGenerator) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextBytesImpl(g1.getCount());
        }

        if (gen instanceof Primitives.WithMetadata) {
            Primitives.WithMetadata g1 = (Primitives.WithMetadata) gen;
            //noinspection unchecked
            return compile(g1.getOperand());
        }

        if (gen instanceof Primitives.SizedGenerator) {
            Primitives.SizedGenerator g1 = (Primitives.SizedGenerator) gen;

            //noinspection unchecked
            return sizedImpl(sizeSelector, rs -> compile((Generator<A>) g1.getFn().apply(rs)));
        }

        if (gen instanceof Primitives.Aggregate) {
            Primitives.Aggregate g1 = (Primitives.Aggregate) gen;
            //noinspection unchecked
            Iterable<Generator<A>> elements = g1.getElements();

            //noinspection unchecked
            return (GeneratorImpl<A>) aggregateImpl(g1.getInitialBuilderSupplier(), g1.getAddFn(),
                    g1.getBuildFn(), map(this::compile, elements));
        }

        if (gen instanceof Primitives.Product2) {
            Primitives.Product2 g1 = (Primitives.Product2) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product2Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product3) {
            Primitives.Product3 g1 = (Primitives.Product3) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product3Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product4) {
            Primitives.Product4 g1 = (Primitives.Product4) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product4Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product5) {
            Primitives.Product5 g1 = (Primitives.Product5) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product5Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product6) {
            Primitives.Product6 g1 = (Primitives.Product6) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product6Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    compile(g1.getF()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product7) {
            Primitives.Product7 g1 = (Primitives.Product7) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product7Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    compile(g1.getF()),
                    compile(g1.getG()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product8) {
            Primitives.Product8 g1 = (Primitives.Product8) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product8Impl(compile(g1.getA()),
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

    private <In, Out> GeneratorImpl<Out> handleMapped(Primitives.Mapped<In, Out> mapped) {
        return mappedImpl(mapped.getFn(), compile(mapped.getOperand()));
    }

    private <In, Out> GeneratorImpl<Out> handleFlatMapped(Primitives.FlatMapped<In, Out> flatMapped) {
        return flatMappedImpl(compile(flatMapped.getOperand()), in -> compile(flatMapped.getFn().apply(in))
        );
    }

    public static DefaultInterpreterMark3 defaultInterpreter(Parameters parameters) {
        return new DefaultInterpreterMark3(parameters);
    }

}
