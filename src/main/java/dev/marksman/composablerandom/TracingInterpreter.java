package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.GeneratorImpl.generator;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.StandardParameters.defaultParameters;
import static dev.marksman.composablerandom.Trace.trace;
import static dev.marksman.composablerandom.primitives.AggregateImpl.tracedAggregateImpl;
import static dev.marksman.composablerandom.primitives.ConstantImpl.constantImpl;
import static dev.marksman.composablerandom.primitives.CustomImpl.customImpl;
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
import static dev.marksman.composablerandom.primitives.Product2Impl.tracedProduct2Impl;
import static dev.marksman.composablerandom.primitives.Product3Impl.tracedProduct3Impl;
import static dev.marksman.composablerandom.primitives.Product4Impl.tracedProduct4Impl;
import static dev.marksman.composablerandom.primitives.Product5Impl.tracedProduct5Impl;
import static dev.marksman.composablerandom.primitives.Product6Impl.tracedProduct6Impl;
import static dev.marksman.composablerandom.primitives.Product7Impl.tracedProduct7Impl;
import static dev.marksman.composablerandom.primitives.Product8Impl.tracedProduct8Impl;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class TracingInterpreter {
    public static final int INFINITE_ELISION_COUNT = 6;

    private final GeneratorImpl<Trace<Integer>> sizeGenerator;

    private TracingInterpreter(Parameters parameters) {
        SizeSelector sizeSelector = parameters.getSizeSelector();
        this.sizeGenerator = compile(Generator.<Integer>generate(sizeSelector::selectSize)
                .labeled("sized"));
    }

    private <A> Result<Seed, Trace<A>> traceResult(Generator<A> gen, Result<? extends Seed, A> resultValue) {
        return result(resultValue.getNextState(), trace(resultValue.getValue(), gen));
    }

    private <A> GeneratorImpl<Trace<A>> traced(Generator<A> gen, GeneratorImpl<A> g) {
        return input -> traceResult(gen, g.run(input));
    }

    public <A> GeneratorImpl<Trace<A>> compile(Generator<A> gen) {

        if (gen instanceof Generator.Constant) {
            return traced(gen, constantImpl(((Generator.Constant<A>) gen).getValue()));
        }

        if (gen instanceof Generator.Custom) {
            return traced(gen, customImpl(((Generator.Custom<A>) gen).getFn()));
        }

        if (gen instanceof Generator.Mapped) {
            return handleMapped((Generator.Mapped<?, A>) gen);
        }

        if (gen instanceof Generator.FlatMapped) {
            return handleFlatMapped((Generator.FlatMapped<?, A>) gen);
        }

        if (gen instanceof Generator.Tap) {
            return handleTap((Generator.Tap<?, A>) gen);
        }

        if (gen instanceof Generator.NextInt) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntImpl());
        }

        if (gen instanceof Generator.NextLong) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongImpl());
        }

        if (gen instanceof Generator.NextBoolean) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextBooleanImpl());
        }

        if (gen instanceof Generator.NextDouble) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextDoubleImpl());
        }

        if (gen instanceof Generator.NextFloat) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextFloatImpl());
        }

        if (gen instanceof Generator.NextIntBounded) {
            int bound = ((Generator.NextIntBounded) gen).getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntBoundedImpl(bound));
        }

        if (gen instanceof Generator.NextIntExclusive) {
            Generator.NextIntExclusive g1 = (Generator.NextIntExclusive) gen;
            int origin = g1.getOrigin();
            int bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntExclusiveImpl(origin, bound));
        }

        if (gen instanceof Generator.NextIntBetween) {
            Generator.NextIntBetween g1 = (Generator.NextIntBetween) gen;
            int min = g1.getMin();
            int max = g1.getMax();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntBetweenImpl(min, max));
        }

        if (gen instanceof Generator.NextIntIndex) {
            Generator.NextIntIndex g1 = (Generator.NextIntIndex) gen;
            int bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntIndexImpl(bound));
        }

        if (gen instanceof Generator.NextLongBounded) {
            long bound = ((Generator.NextLongBounded) gen).getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongBoundedImpl(bound));
        }

        if (gen instanceof Generator.NextLongExclusive) {
            Generator.NextLongExclusive g1 = (Generator.NextLongExclusive) gen;
            long origin = g1.getOrigin();
            long bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongExclusiveImpl(origin, bound));
        }

        if (gen instanceof Generator.NextLongBetween) {
            Generator.NextLongBetween g1 = (Generator.NextLongBetween) gen;
            long min = g1.getMin();
            long max = g1.getMax();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongBetweenImpl(min, max));
        }

        if (gen instanceof Generator.NextLongIndex) {
            Generator.NextLongIndex g1 = (Generator.NextLongIndex) gen;
            long bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongIndexImpl(bound));
        }

        if (gen instanceof Generator.NextGaussian) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextGaussianImpl());
        }

        if (gen instanceof Generator.NextByte) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextByteImpl());
        }

        if (gen instanceof Generator.NextShort) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextShortImpl());
        }

        if (gen instanceof Generator.NextBytes) {
            Generator.NextBytes g1 = (Generator.NextBytes) gen;
            int count = g1.getCount();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextBytesImpl(count));
        }

        if (gen instanceof Generator.WithMetadata) {
            //noinspection unchecked
            return handleWithMetadata((Generator.WithMetadata) gen);
        }

        if (gen instanceof Generator.Sized) {
            return handleSized((Generator.Sized<A>) gen);
        }

        if (gen instanceof Generator.Aggregate) {
            Generator.Aggregate g1 = (Generator.Aggregate) gen;
            //noinspection unchecked
            Iterable<Generator<A>> elements = g1.getElements();

            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedAggregateImpl(gen, g1.getInitialBuilderSupplier(), g1.getAddFn(),
                    g1.getBuildFn(), map(this::compile, elements));
        }

        if (gen instanceof Generator.Product2) {
            Generator.Product2 g1 = (Generator.Product2) gen;
            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedProduct2Impl(gen,
                    compile(g1.getA()),
                    compile(g1.getB()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.Product3) {
            Generator.Product3 g1 = (Generator.Product3) gen;
            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedProduct3Impl(gen,
                    compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.Product4) {
            Generator.Product4 g1 = (Generator.Product4) gen;
            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedProduct4Impl(gen,
                    compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.Product5) {
            Generator.Product5 g1 = (Generator.Product5) gen;
            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedProduct5Impl(gen,
                    compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    g1.getCombine());
        }

        if (gen instanceof Generator.Product6) {
            Generator.Product6 g1 = (Generator.Product6) gen;
            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedProduct6Impl(gen,
                    compile(g1.getA()),
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
            return (GeneratorImpl<Trace<A>>) tracedProduct7Impl(gen,
                    compile(g1.getA()),
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
            return (GeneratorImpl<Trace<A>>) tracedProduct8Impl(gen,
                    compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    compile(g1.getF()),
                    compile(g1.getG()),
                    compile(g1.getH()),
                    g1.getCombine());
        }

        throw new IllegalStateException("Unimplemented generator");
    }


    private <In, Out> GeneratorImpl<Trace<Out>> handleMapped(Generator.Mapped<In, Out> gen) {
        return mappedImpl(t -> trace(gen.getFn().apply(t.getResult()),
                gen, singletonList(t)),
                compile(gen.getOperand()));
    }

    private <In, Out> GeneratorImpl<Trace<Out>> handleFlatMapped(Generator.FlatMapped<In, Out> gen) {
        Fn1<? super In, ? extends Generator<Out>> fn = gen.getFn();
        GeneratorImpl<Trace<In>> g1 = compile(gen.getOperand());

        return generator(rs -> {
            Result<? extends Seed, Trace<In>> r1 = g1.run(rs);
            Trace<In> trace1 = r1.getValue();
            Result<? extends Seed, Trace<Out>> r2 = compile(fn.apply(trace1.getResult()))
                    .run(r1.getNextState());
            Trace<Out> trace2 = r2.getValue();
            return result(r2.getNextState(),
                    trace(trace2.getResult(), gen, asList(trace1, trace2)));
        });
    }

//
//    @SuppressWarnings("unchecked")
//    private <Elem, Out> Generator<Trace<Out>> handleInfinite(Generator.Infinite<Elem> generator) {
//        Generator<Trace<Elem>> inner = compile(generator.getGenerate());
//        Generator<ImmutableNonEmptyIterable<Trace<Elem>>> traceInfinite = infiniteImpl(inner);
//
//        return compiledGenerator(rs -> {
//            Result<? extends Seed, ImmutableNonEmptyIterable<Trace<Elem>>> r1 = traceInfinite.run(rs);
//            ImmutableNonEmptyIterable<Trace<Elem>> tracedValues = r1.getValue();
//            return result(r1.getNextState(),
//                    (Trace<Out>) trace(tracedValues.fmap(Trace::getResult),
//                            generator,
//                            tracedValues.take(INFINITE_ELISION_COUNT).fmap(upcast())));
//        });
//
//    }

    // TODO: handleTap
    private <Elem, Out> GeneratorImpl<Trace<Out>> handleTap(Generator.Tap<Elem, Out> gen) {
        throw new UnsupportedOperationException();
    }

    private <A> GeneratorImpl<Trace<A>> handleSized(Generator.Sized<A> gen) {
        Fn1<Integer, Generator<A>> fn = gen.getFn();

        return generator(rs -> {
            Result<? extends Seed, Trace<Integer>> r1 = sizeGenerator.run(rs);
            Trace<Integer> sizeTrace = r1.getValue();
            Generator<A> inner = fn.apply(sizeTrace.getResult());
            Result<? extends Seed, Trace<A>> r2 = compile(inner)
                    .run(r1.getNextState());
            Trace<A> innerTrace = r2.getValue();
            return result(r2.getNextState(),
                    trace(innerTrace.getResult(), inner, asList(sizeTrace, innerTrace)));
        });

    }

    private <A> GeneratorImpl<Trace<A>> handleWithMetadata(Generator.WithMetadata<A> gen) {
        GeneratorImpl<Trace<A>> inner = compile(gen.getOperand());
        return generator(rs -> {
            Result<? extends Seed, Trace<A>> run = inner.run(rs);
            Trace<A> innerTrace = run.getValue();
            return result(run.getNextState(),
                    trace(innerTrace.getResult(), gen, singletonList(innerTrace)));
        });
    }

    public static TracingInterpreter tracingInterpreter(Parameters parameters) {
        return new TracingInterpreter(parameters);
    }

    public static TracingInterpreter tracingInterpreter() {
        return tracingInterpreter(defaultParameters());
    }

}
