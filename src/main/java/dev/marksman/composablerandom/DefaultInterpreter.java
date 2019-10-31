package dev.marksman.composablerandom;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.legacy.primitives.AggregateImpl.aggregateImpl;
import static dev.marksman.composablerandom.legacy.primitives.ConstantImpl.constantImpl;
import static dev.marksman.composablerandom.legacy.primitives.CustomImpl.customImpl;
import static dev.marksman.composablerandom.legacy.primitives.FlatMappedImpl.flatMappedImpl;
import static dev.marksman.composablerandom.legacy.primitives.InjectSpecialValuesImpl.mixInSpecialValuesImpl;
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
import static dev.marksman.composablerandom.legacy.primitives.TapImpl.tapImpl;

public class DefaultInterpreter implements Interpreter {
    @Override
    public <A> GeneratorImpl<A> handle(InterpreterContext context, Generator<A> gen) {
        if (gen instanceof Primitives.Constant) {
            return constantImpl(((Primitives.Constant<A>) gen).getValue());
        }

        if (gen instanceof Generator.Custom) {
            return customImpl(((Generator.Custom<A>) gen).getFn());
        }

        if (gen instanceof Primitives.Mapped) {
            return handleMapped(context, (Primitives.Mapped<?, A>) gen);
        }

        if (gen instanceof Primitives.FlatMapped) {
            return handleFlatMapped(context, (Primitives.FlatMapped<?, A>) gen);
        }

        if (gen instanceof Primitives.Tap) {
            return handleTap(context, (Primitives.Tap<?, A>) gen);
        }

        if (gen instanceof Primitives.NextInt) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntImpl();
        }

        if (gen instanceof Primitives.NextLong) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongImpl();
        }

        if (gen instanceof Primitives.NextBoolean) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextBooleanImpl();
        }

        if (gen instanceof Primitives.NextDouble) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextDoubleImpl();
        }

        if (gen instanceof Primitives.NextFloat) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextFloatImpl();
        }

        if (gen instanceof Primitives.NextIntBounded) {
            int bound = ((Primitives.NextIntBounded) gen).getBound();
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntBoundedImpl(bound);
        }

        if (gen instanceof Primitives.NextIntExclusive) {
            Primitives.NextIntExclusive g1 = (Primitives.NextIntExclusive) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Primitives.NextIntBetween) {
            Primitives.NextIntBetween g1 = (Primitives.NextIntBetween) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Primitives.NextIntIndex) {
            Primitives.NextIntIndex g1 = (Primitives.NextIntIndex) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextIntIndexImpl(g1.getBound());
        }

        if (gen instanceof Primitives.NextLongBounded) {
            Primitives.NextLongBounded g1 = (Primitives.NextLongBounded) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongBoundedImpl(g1.getBound());
        }

        if (gen instanceof Primitives.NextLongExclusive) {
            Primitives.NextLongExclusive g1 = (Primitives.NextLongExclusive) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongExclusiveImpl(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Primitives.NextLongBetween) {
            Primitives.NextLongBetween g1 = (Primitives.NextLongBetween) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongBetweenImpl(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Primitives.NextLongIndex) {
            Primitives.NextLongIndex g1 = (Primitives.NextLongIndex) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextLongIndexImpl(g1.getBound());
        }

        if (gen instanceof Primitives.NextGaussian) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextGaussianImpl();
        }

        if (gen instanceof Primitives.NextByte) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextByteImpl();
        }

        if (gen instanceof Primitives.NextShort) {
            //noinspection unchecked
            return (GeneratorImpl<A>) nextShortImpl();
        }

        if (gen instanceof Primitives.NextBytes) {
            Primitives.NextBytes g1 = (Primitives.NextBytes) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) nextBytesImpl(g1.getCount());
        }

        if (gen instanceof Primitives.WithMetadata) {
            Primitives.WithMetadata g1 = (Primitives.WithMetadata) gen;
            //noinspection unchecked
            return context.recurse(g1.getOperand());
        }

        if (gen instanceof Primitives.Sized) {
            Primitives.Sized g1 = (Primitives.Sized) gen;

            //noinspection unchecked
            return sizedImpl(context.getParameters().getSizeSelector(),
                    rs -> context.recurse((Generator<A>) g1.getFn().apply(rs)));
        }

        if (gen instanceof Primitives.Aggregate) {
            Primitives.Aggregate g1 = (Primitives.Aggregate) gen;
            //noinspection unchecked
            Iterable<Generator<A>> elements = g1.getElements();

            //noinspection unchecked
            return (GeneratorImpl<A>) aggregateImpl(g1.getInitialBuilderSupplier(), g1.getAddFn(),
                    g1.getBuildFn(), map(context::recurse, elements));
        }

        if (gen instanceof Primitives.Product2) {
            Primitives.Product2 g1 = (Primitives.Product2) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product2Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product3) {
            Primitives.Product3 g1 = (Primitives.Product3) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product3Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product4) {
            Primitives.Product4 g1 = (Primitives.Product4) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product4Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product5) {
            Primitives.Product5 g1 = (Primitives.Product5) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product5Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    context.recurse(g1.getE()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product6) {
            Primitives.Product6 g1 = (Primitives.Product6) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product6Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    context.recurse(g1.getE()),
                    context.recurse(g1.getF()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product7) {
            Primitives.Product7 g1 = (Primitives.Product7) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product7Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    context.recurse(g1.getE()),
                    context.recurse(g1.getF()),
                    context.recurse(g1.getG()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product8) {
            Primitives.Product8 g1 = (Primitives.Product8) gen;
            //noinspection unchecked
            return (GeneratorImpl<A>) product8Impl(context.recurse(g1.getA()),
                    context.recurse(g1.getB()),
                    context.recurse(g1.getC()),
                    context.recurse(g1.getD()),
                    context.recurse(g1.getE()),
                    context.recurse(g1.getF()),
                    context.recurse(g1.getG()),
                    context.recurse(g1.getH()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.InjectSpecialValues) {
            Generator.InjectSpecialValues<A> g1 = (Generator.InjectSpecialValues<A>) gen;
            NonEmptyFiniteIterable<A> acc = g1.getSpecialValues();
            while (g1.getInner() instanceof Generator.InjectSpecialValues) {
                g1 = (Generator.InjectSpecialValues<A>) g1.getInner();
                acc = acc.concat(g1.getSpecialValues());
            }
            ImmutableNonEmptyVector<A> specialValues = NonEmptyVector.copyFromOrThrow(acc);
            return mixInSpecialValuesImpl(specialValues, 20 + 3 * specialValues.size(),
                    context.recurse(g1.getInner()));
        }

        return context.callNextHandler(gen);
    }

    private <In, Out> GeneratorImpl<Out> handleMapped(InterpreterContext context,
                                                      Primitives.Mapped<In, Out> mapped) {
        return mappedImpl(mapped.getFn(), context.recurse(mapped.getOperand()));
    }

    private <In, Out> GeneratorImpl<Out> handleFlatMapped(InterpreterContext context,
                                                          Primitives.FlatMapped<In, Out> flatMapped) {
        return flatMappedImpl(context.recurse(flatMapped.getOperand()),
                in -> context.recurse(flatMapped.getFn().apply(in))
        );
    }

    private <In, Out> GeneratorImpl<Out> handleTap(InterpreterContext context, Primitives.Tap<In, Out> gen) {
        return tapImpl(context.recurse(gen.getInner()), gen.getFn());
    }

    public static DefaultInterpreter defaultInterpreter() {
        return new DefaultInterpreter();
    }

}
