package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.StandardParameters.defaultParameters;
import static dev.marksman.composablerandom.Trace.trace;
import static dev.marksman.composablerandom.legacy.primitives.AggregateImpl.tracedAggregateImpl;
import static dev.marksman.composablerandom.legacy.primitives.ConstantImpl.constantImpl;
import static dev.marksman.composablerandom.legacy.primitives.CustomImpl.customImpl;
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
import static dev.marksman.composablerandom.legacy.primitives.Product2Impl.tracedProduct2Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product3Impl.tracedProduct3Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product4Impl.tracedProduct4Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product5Impl.tracedProduct5Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product6Impl.tracedProduct6Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product7Impl.tracedProduct7Impl;
import static dev.marksman.composablerandom.legacy.primitives.Product8Impl.tracedProduct8Impl;
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

    private <A> Result<LegacySeed, Trace<A>> traceResult(Generator<A> gen, Result<? extends LegacySeed, A> resultValue) {
        return result(resultValue.getNextState(), trace(resultValue.getValue(), gen));
    }

    private <A> GeneratorImpl<Trace<A>> traced(Generator<A> gen, GeneratorImpl<A> g) {
        return input -> traceResult(gen, g.run(input));
    }

    public <A> GeneratorImpl<Trace<A>> compile(Generator<A> gen) {

        if (gen instanceof Primitives.Constant) {
            return traced(gen, constantImpl(((Primitives.Constant<A>) gen).getValue()));
        }

        if (gen instanceof Generator.Custom) {
            return traced(gen, customImpl(((Generator.Custom<A>) gen).getFn()));
        }

        if (gen instanceof Primitives.Mapped) {
            return handleMapped((Primitives.Mapped<?, A>) gen);
        }

        if (gen instanceof Primitives.FlatMapped) {
            return handleFlatMapped((Primitives.FlatMapped<?, A>) gen);
        }

        if (gen instanceof Primitives.Tap) {
            return handleTap((Primitives.Tap<?, A>) gen);
        }

        if (gen instanceof Primitives.NextInt) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntImpl());
        }

        if (gen instanceof Primitives.NextLong) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongImpl());
        }

        if (gen instanceof Primitives.NextBoolean) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextBooleanImpl());
        }

        if (gen instanceof Primitives.NextDouble) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextDoubleImpl());
        }

        if (gen instanceof Primitives.NextFloat) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextFloatImpl());
        }

        if (gen instanceof Primitives.NextIntBounded) {
            int bound = ((Primitives.NextIntBounded) gen).getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntBoundedImpl(bound));
        }

        if (gen instanceof Primitives.NextIntExclusive) {
            Primitives.NextIntExclusive g1 = (Primitives.NextIntExclusive) gen;
            int origin = g1.getOrigin();
            int bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntExclusiveImpl(origin, bound));
        }

        if (gen instanceof Primitives.NextIntBetween) {
            Primitives.NextIntBetween g1 = (Primitives.NextIntBetween) gen;
            int min = g1.getMin();
            int max = g1.getMax();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntBetweenImpl(min, max));
        }

        if (gen instanceof Primitives.NextIntIndex) {
            Primitives.NextIntIndex g1 = (Primitives.NextIntIndex) gen;
            int bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextIntIndexImpl(bound));
        }

        if (gen instanceof Primitives.NextLongBounded) {
            long bound = ((Primitives.NextLongBounded) gen).getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongBoundedImpl(bound));
        }

        if (gen instanceof Primitives.NextLongExclusive) {
            Primitives.NextLongExclusive g1 = (Primitives.NextLongExclusive) gen;
            long origin = g1.getOrigin();
            long bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongExclusiveImpl(origin, bound));
        }

        if (gen instanceof Primitives.NextLongBetween) {
            Primitives.NextLongBetween g1 = (Primitives.NextLongBetween) gen;
            long min = g1.getMin();
            long max = g1.getMax();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongBetweenImpl(min, max));
        }

        if (gen instanceof Primitives.NextLongIndex) {
            Primitives.NextLongIndex g1 = (Primitives.NextLongIndex) gen;
            long bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextLongIndexImpl(bound));
        }

        if (gen instanceof Primitives.NextGaussian) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextGaussianImpl());
        }

        if (gen instanceof Primitives.NextByte) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextByteImpl());
        }

        if (gen instanceof Primitives.NextShort) {
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextShortImpl());
        }

        if (gen instanceof Primitives.NextBytes) {
            Primitives.NextBytes g1 = (Primitives.NextBytes) gen;
            int count = g1.getCount();
            //noinspection unchecked
            return traced(gen, (GeneratorImpl<A>) nextBytesImpl(count));
        }

        if (gen instanceof Primitives.WithMetadata) {
            //noinspection unchecked
            return handleWithMetadata((Primitives.WithMetadata) gen);
        }

        if (gen instanceof Primitives.Sized) {
            return handleSized((Primitives.Sized<A>) gen);
        }

        if (gen instanceof Primitives.Aggregate) {
            Primitives.Aggregate g1 = (Primitives.Aggregate) gen;
            //noinspection unchecked
            Iterable<Generator<A>> elements = g1.getElements();

            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedAggregateImpl(gen, g1.getInitialBuilderSupplier(), g1.getAddFn(),
                    g1.getBuildFn(), map(this::compile, elements));
        }

        if (gen instanceof Primitives.Product2) {
            Primitives.Product2 g1 = (Primitives.Product2) gen;
            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedProduct2Impl(gen,
                    compile(g1.getA()),
                    compile(g1.getB()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product3) {
            Primitives.Product3 g1 = (Primitives.Product3) gen;
            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedProduct3Impl(gen,
                    compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product4) {
            Primitives.Product4 g1 = (Primitives.Product4) gen;
            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedProduct4Impl(gen,
                    compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product5) {
            Primitives.Product5 g1 = (Primitives.Product5) gen;
            //noinspection unchecked
            return (GeneratorImpl<Trace<A>>) tracedProduct5Impl(gen,
                    compile(g1.getA()),
                    compile(g1.getB()),
                    compile(g1.getC()),
                    compile(g1.getD()),
                    compile(g1.getE()),
                    g1.getCombine());
        }

        if (gen instanceof Primitives.Product6) {
            Primitives.Product6 g1 = (Primitives.Product6) gen;
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

        if (gen instanceof Primitives.Product7) {
            Primitives.Product7 g1 = (Primitives.Product7) gen;
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

        if (gen instanceof Primitives.Product8) {
            Primitives.Product8 g1 = (Primitives.Product8) gen;
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


    private <In, Out> GeneratorImpl<Trace<Out>> handleMapped(Primitives.Mapped<In, Out> gen) {
        return mappedImpl(t -> trace(gen.getFn().apply(t.getResult()),
                gen, singletonList(t)),
                compile(gen.getOperand()));
    }

    private <In, Out> GeneratorImpl<Trace<Out>> handleFlatMapped(Primitives.FlatMapped<In, Out> gen) {
        Fn1<? super In, ? extends Generator<Out>> fn = gen.getFn();
        GeneratorImpl<Trace<In>> g1 = compile(gen.getOperand());

        return new GeneratorImpl<Trace<Out>>() {
            @Override
            public Result<? extends LegacySeed, Trace<Out>> run(LegacySeed input) {
                Result<? extends LegacySeed, Trace<In>> r1 = g1.run(input);
                Trace<In> trace1 = r1.getValue();
                Result<? extends LegacySeed, Trace<Out>> r2 = compile(fn.apply(trace1.getResult()))
                        .run(r1.getNextState());
                Trace<Out> trace2 = r2.getValue();
                return result(r2.getNextState(),
                        trace(trace2.getResult(), gen, asList(trace1, trace2)));
            }
        };

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
    private <Elem, Out> GeneratorImpl<Trace<Out>> handleTap(Primitives.Tap<Elem, Out> gen) {
        throw new UnsupportedOperationException();
    }

    private <A> GeneratorImpl<Trace<A>> handleSized(Primitives.Sized<A> gen) {
        Fn1<Integer, Generator<A>> fn = gen.getFn();

        return new GeneratorImpl<Trace<A>>() {
            @Override
            public Result<? extends LegacySeed, Trace<A>> run(LegacySeed input) {
                Result<? extends LegacySeed, Trace<Integer>> r1 = sizeGenerator.run(input);
                Trace<Integer> sizeTrace = r1.getValue();
                Generator<A> inner = fn.apply(sizeTrace.getResult());
                Result<? extends LegacySeed, Trace<A>> r2 = compile(inner)
                        .run(r1.getNextState());
                Trace<A> innerTrace = r2.getValue();
                return result(r2.getNextState(),
                        trace(innerTrace.getResult(), inner, asList(sizeTrace, innerTrace)));
            }
        };

    }

    private <A> GeneratorImpl<Trace<A>> handleWithMetadata(Primitives.WithMetadata<A> gen) {
        GeneratorImpl<Trace<A>> inner = compile(gen.getOperand());

        return new GeneratorImpl<Trace<A>>() {
            @Override
            public Result<? extends LegacySeed, Trace<A>> run(LegacySeed input) {
                Result<? extends LegacySeed, Trace<A>> run = inner.run(input);
                Trace<A> innerTrace = run.getValue();
                return result(run.getNextState(),
                        trace(innerTrace.getResult(), gen, singletonList(innerTrace)));
            }
        };
    }

    public static TracingInterpreter tracingInterpreter(Parameters parameters) {
        return new TracingInterpreter(parameters);
    }

    public static TracingInterpreter tracingInterpreter() {
        return tracingInterpreter(defaultParameters());
    }

}
