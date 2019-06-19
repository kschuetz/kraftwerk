package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generate.Product6;
import dev.marksman.composablerandom.primitives.AggregateImpl;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.Generator.generator;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.StandardParameters.defaultParameters;
import static dev.marksman.composablerandom.Trace.trace;
import static dev.marksman.composablerandom.primitives.AggregateImpl.aggregateImpl;
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
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class TracingInterpreter {
    public static final int INFINITE_ELISION_COUNT = 6;

    private final Generator<Trace<Integer>> sizeGenerator;

    private TracingInterpreter(Parameters parameters) {
        SizeSelector sizeSelector = parameters.getSizeSelector();
        this.sizeGenerator = compile(Generate.<Integer>generate(sizeSelector::selectSize)
                .labeled("sized"));
    }

    private <A> Result<RandomState, Trace<A>> traceResult(Generate<A> gen, Result<? extends RandomState, A> resultValue) {
        return result(resultValue.getNextState(), trace(resultValue.getValue(), gen));
    }

    private <A> Generator<Trace<A>> traced(Generate<A> gen, Generator<A> g) {
        return input -> traceResult(gen, g.run(input));
    }

    public <A> Generator<Trace<A>> compile(Generate<A> gen) {

        if (gen instanceof Generate.Constant) {
            return traced(gen, constantImpl(((Generate.Constant<A>) gen).getValue()));
        }

        if (gen instanceof Generate.Custom) {
            return traced(gen, customImpl(((Generate.Custom<A>) gen).getFn()));
        }

        if (gen instanceof Generate.Mapped) {
            return handleMapped((Generate.Mapped<?, A>) gen);
        }

        if (gen instanceof Generate.FlatMapped) {
            return handleFlatMapped((Generate.FlatMapped<?, A>) gen);
        }

        if (gen instanceof Generate.Tap) {
            return handleTap((Generate.Tap<?, A>) gen);
        }

        if (gen instanceof Generate.NextInt) {
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextIntImpl());
        }

        if (gen instanceof Generate.NextLong) {
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextLongImpl());
        }

        if (gen instanceof Generate.NextBoolean) {
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextBooleanImpl());
        }

        if (gen instanceof Generate.NextDouble) {
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextDoubleImpl());
        }

        if (gen instanceof Generate.NextFloat) {
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextFloatImpl());
        }

        if (gen instanceof Generate.NextIntBounded) {
            int bound = ((Generate.NextIntBounded) gen).getBound();
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextIntBoundedImpl(bound));
        }

        if (gen instanceof Generate.NextIntExclusive) {
            Generate.NextIntExclusive g1 = (Generate.NextIntExclusive) gen;
            int origin = g1.getOrigin();
            int bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextIntExclusiveImpl(origin, bound));
        }

        if (gen instanceof Generate.NextIntBetween) {
            Generate.NextIntBetween g1 = (Generate.NextIntBetween) gen;
            int min = g1.getMin();
            int max = g1.getMax();
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextIntBetweenImpl(min, max));
        }

        if (gen instanceof Generate.NextIntIndex) {
            Generate.NextIntIndex g1 = (Generate.NextIntIndex) gen;
            int bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextIntIndexImpl(bound));
        }

        if (gen instanceof Generate.NextLongBounded) {
            long bound = ((Generate.NextLongBounded) gen).getBound();
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextLongBoundedImpl(bound));
        }

        if (gen instanceof Generate.NextLongExclusive) {
            Generate.NextLongExclusive g1 = (Generate.NextLongExclusive) gen;
            long origin = g1.getOrigin();
            long bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextLongExclusiveImpl(origin, bound));
        }

        if (gen instanceof Generate.NextLongBetween) {
            Generate.NextLongBetween g1 = (Generate.NextLongBetween) gen;
            long min = g1.getMin();
            long max = g1.getMax();
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextLongBetweenImpl(min, max));
        }

        if (gen instanceof Generate.NextLongIndex) {
            Generate.NextLongIndex g1 = (Generate.NextLongIndex) gen;
            long bound = g1.getBound();
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextLongIndexImpl(bound));
        }

        if (gen instanceof Generate.NextGaussian) {
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextGaussianImpl());
        }

        if (gen instanceof Generate.NextByte) {
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextByteImpl());
        }

        if (gen instanceof Generate.NextShort) {
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextShortImpl());
        }

        if (gen instanceof Generate.NextBytes) {
            Generate.NextBytes g1 = (Generate.NextBytes) gen;
            int count = g1.getCount();
            //noinspection unchecked
            return traced(gen, (Generator<A>) nextBytesImpl(count));
        }

        if (gen instanceof Generate.WithMetadata) {
            //noinspection unchecked
            return handleWithMetadata((Generate.WithMetadata) gen);
        }

        if (gen instanceof Generate.Sized) {
            return handleSized((Generate.Sized<A>) gen);
        }

        if (gen instanceof Generate.Aggregate) {
            //noinspection unchecked
            return handleAggregate((Generate.Aggregate) gen);
        }

        if (gen instanceof Generate.Product2) {
            //noinspection unchecked
            return handleProduct2((Generate.Product2) gen);
        }

        if (gen instanceof Generate.Product3) {
            //noinspection unchecked
            return handleProduct3((Generate.Product3) gen);
        }

        if (gen instanceof Generate.Product4) {
            //noinspection unchecked
            return handleProduct4((Generate.Product4) gen);
        }

        if (gen instanceof Generate.Product5) {
            //noinspection unchecked
            return handleProduct5((Generate.Product5) gen);
        }

        if (gen instanceof Generate.Product6) {
            //noinspection unchecked
            return handleProduct6((Generate.Product6) gen);
        }

        if (gen instanceof Generate.Product7) {
            //noinspection unchecked
            return handleProduct7((Generate.Product7) gen);
        }

        if (gen instanceof Generate.Product8) {
            //noinspection unchecked
            return handleProduct8((Generate.Product8) gen);
        }

        throw new IllegalStateException("Unimplemented generator");
    }


    private <In, Out> Generator<Trace<Out>> handleMapped(Generate.Mapped<In, Out> gen) {
        return mappedImpl(t -> trace(gen.getFn().apply(t.getResult()),
                gen, singletonList(t)),
                compile(gen.getOperand()));
    }

    private <In, Out> Generator<Trace<Out>> handleFlatMapped(Generate.FlatMapped<In, Out> gen) {
        Fn1<? super In, ? extends Generate<Out>> fn = gen.getFn();
        Generator<Trace<In>> g1 = compile(gen.getOperand());

        return generator(rs -> {
            Result<? extends RandomState, Trace<In>> r1 = g1.run(rs);
            Trace<In> trace1 = r1.getValue();
            Result<? extends RandomState, Trace<Out>> r2 = compile(fn.apply(trace1.getResult()))
                    .run(r1.getNextState());
            Trace<Out> trace2 = r2.getValue();
            return result(r2.getNextState(),
                    trace(trace2.getResult(), gen, asList(trace1, trace2)));
        });
    }

//
//    @SuppressWarnings("unchecked")
//    private <Elem, Out> Generator<Trace<Out>> handleInfinite(Generate.Infinite<Elem> generator) {
//        Generator<Trace<Elem>> inner = compile(generator.getGenerate());
//        Generator<ImmutableNonEmptyIterable<Trace<Elem>>> traceInfinite = infiniteImpl(inner);
//
//        return compiledGenerator(rs -> {
//            Result<? extends RandomState, ImmutableNonEmptyIterable<Trace<Elem>>> r1 = traceInfinite.run(rs);
//            ImmutableNonEmptyIterable<Trace<Elem>> tracedValues = r1.getValue();
//            return result(r1.getNextState(),
//                    (Trace<Out>) trace(tracedValues.fmap(Trace::getResult),
//                            generator,
//                            tracedValues.take(INFINITE_ELISION_COUNT).fmap(upcast())));
//        });
//
//    }

    // TODO: handleTap
    private <Elem, Out> Generator<Trace<Out>> handleTap(Generate.Tap<Elem, Out> gen) {
        throw new UnsupportedOperationException();
    }

    private <A> Generator<Trace<A>> handleSized(Generate.Sized<A> gen) {
        Fn1<Integer, Generate<A>> fn = gen.getFn();

        return generator(rs -> {
            Result<? extends RandomState, Trace<Integer>> r1 = sizeGenerator.run(rs);
            Trace<Integer> sizeTrace = r1.getValue();
            Generate<A> inner = fn.apply(sizeTrace.getResult());
            Result<? extends RandomState, Trace<A>> r2 = compile(inner)
                    .run(r1.getNextState());
            Trace<A> innerTrace = r2.getValue();
            return result(r2.getNextState(),
                    trace(innerTrace.getResult(), inner, asList(sizeTrace, innerTrace)));
        });

    }

    private <A> Generator<Trace<A>> handleWithMetadata(Generate.WithMetadata<A> gen) {
        Generator<Trace<A>> inner = compile(gen.getOperand());
        return generator(rs -> {
            Result<? extends RandomState, Trace<A>> run = inner.run(rs);
            Trace<A> innerTrace = run.getValue();
            return result(run.getNextState(),
                    trace(innerTrace.getResult(), gen, singletonList(innerTrace)));
        });
    }

    private <Elem, Builder, Out> Generator<Trace<Out>> handleAggregate(Generate.Aggregate<Elem, Builder, Out> gen) {
        @SuppressWarnings("UnnecessaryLocalVariable")
        AggregateImpl<Trace<Elem>, TraceCollector<Builder>, Trace<Out>> aggregator = aggregateImpl(
                () -> new TraceCollector<>(gen.getInitialBuilderSupplier().apply()),
                (tc, tracedElem) -> {
                    tc.state = gen.getAddFn().apply(tc.state, tracedElem.getResult());
                    tc.traces.add(tracedElem);
                    return tc;
                },
                tc -> trace(gen.getBuildFn().apply(tc.state), gen, tc.traces),
                map(this::compile, gen.getElements()));
        return aggregator;
    }

    private <A, B, Out> Generator<Trace<Out>> handleProduct2(
            Generate.Product2<A, B, Out> gen) {
        Generator<Trace<A>> ca = compile(gen.getA());
        Generator<Trace<B>> cb = compile(gen.getB());

        return generator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();

            return result(rb.getNextState(),
                    trace(gen.getCombine().apply(ta.getResult(),
                            tb.getResult()),
                            gen,
                            asList(ta, tb)));
        });
    }

    private <A, B, C, Out> Generator<Trace<Out>> handleProduct3(
            Generate.Product3<A, B, C, Out> gen) {
        Generator<Trace<A>> ca = compile(gen.getA());
        Generator<Trace<B>> cb = compile(gen.getB());
        Generator<Trace<C>> cc = compile(gen.getC());

        return generator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());
            Result<? extends RandomState, Trace<C>> rc = cc.run(rb.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();
            Trace<C> tc = rc.getValue();

            return result(rc.getNextState(),
                    trace(gen.getCombine().apply(ta.getResult(),
                            tb.getResult(),
                            tc.getResult()),
                            gen,
                            asList(ta, tb, tc)));
        });
    }

    private <A, B, C, D, Out> Generator<Trace<Out>> handleProduct4(
            Generate.Product4<A, B, C, D, Out> gen) {
        Generator<Trace<A>> ca = compile(gen.getA());
        Generator<Trace<B>> cb = compile(gen.getB());
        Generator<Trace<C>> cc = compile(gen.getC());
        Generator<Trace<D>> cd = compile(gen.getD());

        return generator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());
            Result<? extends RandomState, Trace<C>> rc = cc.run(rb.getNextState());
            Result<? extends RandomState, Trace<D>> rd = cd.run(rc.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();
            Trace<C> tc = rc.getValue();
            Trace<D> td = rd.getValue();

            return result(rd.getNextState(),
                    trace(gen.getCombine().apply(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult()),
                            gen,
                            asList(ta, tb, tc, td)));
        });
    }

    private <A, B, C, D, E, Out> Generator<Trace<Out>> handleProduct5(
            Generate.Product5<A, B, C, D, E, Out> gen) {
        Generator<Trace<A>> ca = compile(gen.getA());
        Generator<Trace<B>> cb = compile(gen.getB());
        Generator<Trace<C>> cc = compile(gen.getC());
        Generator<Trace<D>> cd = compile(gen.getD());
        Generator<Trace<E>> ce = compile(gen.getE());

        return generator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());
            Result<? extends RandomState, Trace<C>> rc = cc.run(rb.getNextState());
            Result<? extends RandomState, Trace<D>> rd = cd.run(rc.getNextState());
            Result<? extends RandomState, Trace<E>> re = ce.run(rd.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();
            Trace<C> tc = rc.getValue();
            Trace<D> td = rd.getValue();
            Trace<E> te = re.getValue();

            return result(re.getNextState(),
                    trace(gen.getCombine().apply(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult(),
                            te.getResult()),
                            gen,
                            asList(ta, tb, tc, td, te)));
        });
    }

    private <A, B, C, D, E, F, Out> Generator<Trace<Out>> handleProduct6(
            Product6<A, B, C, D, E, F, Out> gen) {
        Generator<Trace<A>> ca = compile(gen.getA());
        Generator<Trace<B>> cb = compile(gen.getB());
        Generator<Trace<C>> cc = compile(gen.getC());
        Generator<Trace<D>> cd = compile(gen.getD());
        Generator<Trace<E>> ce = compile(gen.getE());
        Generator<Trace<F>> cf = compile(gen.getF());

        return generator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());
            Result<? extends RandomState, Trace<C>> rc = cc.run(rb.getNextState());
            Result<? extends RandomState, Trace<D>> rd = cd.run(rc.getNextState());
            Result<? extends RandomState, Trace<E>> re = ce.run(rd.getNextState());
            Result<? extends RandomState, Trace<F>> rf = cf.run(re.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();
            Trace<C> tc = rc.getValue();
            Trace<D> td = rd.getValue();
            Trace<E> te = re.getValue();
            Trace<F> tf = rf.getValue();

            return result(rf.getNextState(),
                    trace(gen.getCombine().apply(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult(),
                            te.getResult(),
                            tf.getResult()),
                            gen,
                            asList(ta, tb, tc, td, te, tf)));
        });
    }

    private <A, B, C, D, E, F, G, Out> Generator<Trace<Out>> handleProduct7(
            Generate.Product7<A, B, C, D, E, F, G, Out> gen) {
        Generator<Trace<A>> ca = compile(gen.getA());
        Generator<Trace<B>> cb = compile(gen.getB());
        Generator<Trace<C>> cc = compile(gen.getC());
        Generator<Trace<D>> cd = compile(gen.getD());
        Generator<Trace<E>> ce = compile(gen.getE());
        Generator<Trace<F>> cf = compile(gen.getF());
        Generator<Trace<G>> cg = compile(gen.getG());

        return generator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());
            Result<? extends RandomState, Trace<C>> rc = cc.run(rb.getNextState());
            Result<? extends RandomState, Trace<D>> rd = cd.run(rc.getNextState());
            Result<? extends RandomState, Trace<E>> re = ce.run(rd.getNextState());
            Result<? extends RandomState, Trace<F>> rf = cf.run(re.getNextState());
            Result<? extends RandomState, Trace<G>> rg = cg.run(rf.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();
            Trace<C> tc = rc.getValue();
            Trace<D> td = rd.getValue();
            Trace<E> te = re.getValue();
            Trace<F> tf = rf.getValue();
            Trace<G> tg = rg.getValue();

            return result(rg.getNextState(),
                    trace(gen.getCombine().apply(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult(),
                            te.getResult(),
                            tf.getResult(),
                            tg.getResult()),
                            gen,
                            asList(ta, tb, tc, td, te, tf, tg)));
        });
    }

    private <A, B, C, D, E, F, G, H, Out> Generator<Trace<Out>> handleProduct8(
            Generate.Product8<A, B, C, D, E, F, G, H, Out> gen) {
        Generator<Trace<A>> ca = compile(gen.getA());
        Generator<Trace<B>> cb = compile(gen.getB());
        Generator<Trace<C>> cc = compile(gen.getC());
        Generator<Trace<D>> cd = compile(gen.getD());
        Generator<Trace<E>> ce = compile(gen.getE());
        Generator<Trace<F>> cf = compile(gen.getF());
        Generator<Trace<G>> cg = compile(gen.getG());
        Generator<Trace<H>> ch = compile(gen.getH());

        return generator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());
            Result<? extends RandomState, Trace<C>> rc = cc.run(rb.getNextState());
            Result<? extends RandomState, Trace<D>> rd = cd.run(rc.getNextState());
            Result<? extends RandomState, Trace<E>> re = ce.run(rd.getNextState());
            Result<? extends RandomState, Trace<F>> rf = cf.run(re.getNextState());
            Result<? extends RandomState, Trace<G>> rg = cg.run(rf.getNextState());
            Result<? extends RandomState, Trace<H>> rh = ch.run(rg.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();
            Trace<C> tc = rc.getValue();
            Trace<D> td = rd.getValue();
            Trace<E> te = re.getValue();
            Trace<F> tf = rf.getValue();
            Trace<G> tg = rg.getValue();
            Trace<H> th = rh.getValue();

            return result(rh.getNextState(),
                    trace(gen.getCombine().apply(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult(),
                            te.getResult(),
                            tf.getResult(),
                            tg.getResult(),
                            th.getResult()),
                            gen,
                            asList(ta, tb, tc, td, te, tf, tg, th)));
        });
    }

    private static class TraceCollector<State> {
        ArrayList<Trace<?>> traces;
        State state;

        TraceCollector(State state) {
            this.traces = new ArrayList<>();
            this.state = state;
        }
    }

    public static TracingInterpreter tracingInterpreter(Parameters parameters) {
        return new TracingInterpreter(parameters);
    }

    public static TracingInterpreter tracingInterpreter() {
        return tracingInterpreter(defaultParameters());
    }

}
