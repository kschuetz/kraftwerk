package dev.marksman.composablerandom.legacy;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.GeneratorImpl;
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

    public <A> GeneratorImpl<A> compile(Generator<A> gen) {
        if (gen instanceof Generator.Constant) {
            return constantImpl(((Generator.Constant<A>) gen).getValue());
        }

        if (gen instanceof Generator.Custom) {
            return customImpl(((Generator.Custom<A>) gen).getFn());
        }

        if (gen instanceof Generator.Mapped) {
            return handleMapped((Generator.Mapped<?, A>) gen);
        }

        if (gen instanceof Generator.FlatMapped) {
            return handleFlatMapped((Generator.FlatMapped<?, A>) gen);
        }

        if (gen instanceof Generator.NextInt) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntImpl();
        }

        if (gen instanceof Generator.NextLong) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongImpl();
        }

        if (gen instanceof Generator.NextBoolean) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextBooleanImpl();
        }

        if (gen instanceof Generator.NextDouble) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextDoubleImpl();
        }

        if (gen instanceof Generator.NextFloat) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextFloatImpl();
        }

        if (gen instanceof Generator.NextIntBounded) {
            int bound = ((Generator.NextIntBounded) gen).getBound();
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntBoundedImpl(bound);
        }

        if (gen instanceof Generator.NextIntExclusive) {
            Generator.NextIntExclusive g1 = (Generator.NextIntExclusive) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Generator.NextIntBetween) {
            Generator.NextIntBetween g1 = (Generator.NextIntBetween) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Generator.NextIntIndex) {
            Generator.NextIntIndex g1 = (Generator.NextIntIndex) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntIndexImpl(g1.getBound());
        }

        if (gen instanceof Generator.NextLongBounded) {
            Generator.NextLongBounded g1 = (Generator.NextLongBounded) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongBoundedImpl(g1.getBound());
        }

        if (gen instanceof Generator.NextLongExclusive) {
            Generator.NextLongExclusive g1 = (Generator.NextLongExclusive) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Generator.NextLongBetween) {
            Generator.NextLongBetween g1 = (Generator.NextLongBetween) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Generator.NextLongIndex) {
            Generator.NextLongIndex g1 = (Generator.NextLongIndex) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongIndexImpl(g1.getBound());
        }

        if (gen instanceof Generator.NextGaussian) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextGaussianImpl();
        }

        if (gen instanceof Generator.NextByte) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextByteImpl();
        }

        if (gen instanceof Generator.NextShort) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextShortImpl();
        }

        if (gen instanceof Generator.NextBytes) {
            Generator.NextBytes g1 = (Generator.NextBytes) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextBytesImpl(g1.getCount());
        }

        if (gen instanceof Generator.WithMetadata) {
            Generator.WithMetadata g1 = (Generator.WithMetadata) gen;
            //noinspection unchecked
            return compile(g1.getOperand());
        }

        if (gen instanceof Generator.Sized) {
            Generator.Sized g1 = (Generator.Sized) gen;

            //noinspection unchecked
            return sizedImpl(sizeSelector, rs -> compile((Generator<A>) g1.getFn().apply(rs)));
        }

        if (gen instanceof Generator.Aggregate) {
            Generator.Aggregate g1 = (Generator.Aggregate) gen;
            //noinspection unchecked
            Iterable<Generator<A>> elements = g1.getElements();

            //noinspection unchecked
            return (GeneratorImpl<A>) aggregateImpl(g1.getInitialBuilderSupplier(), g1.getAddFn(),
                    g1.getBuildFn(), map(this::compile, elements));
        }

        if (gen instanceof Generator.Product2) {
            Generator.Product2 g1 = (Generator.Product2) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product2Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.Product3) {
            Generator.Product3 g1 = (Generator.Product3) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product3Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.Product4) {
            Generator.Product4 g1 = (Generator.Product4) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product4Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.Product5) {
            Generator.Product5 g1 = (Generator.Product5) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product5Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.Product6) {
            Generator.Product6 g1 = (Generator.Product6) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product6Impl(compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    compile(g1.getF()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.Product7) {
            Generator.Product7 g1 = (Generator.Product7) gen;
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

        if (gen instanceof Generator.Product8) {
            Generator.Product8 g1 = (Generator.Product8) gen;
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

    private <In, Out> GeneratorImpl<Out> handleMapped(Generator.Mapped<In, Out> mapped) {
        return mappedImpl(mapped.getFn(), compile(mapped.getOperand()));
    }

    private <In, Out> GeneratorImpl<Out> handleFlatMapped(Generator.FlatMapped<In, Out> flatMapped) {
        return flatMappedImpl(compile(flatMapped.getOperand()), in -> compile(flatMapped.getFn().apply(in))
        );
    }

    public static DefaultInterpreterMark3 defaultInterpreter(Parameters parameters) {
        return new DefaultInterpreterMark3(parameters);
    }

}
