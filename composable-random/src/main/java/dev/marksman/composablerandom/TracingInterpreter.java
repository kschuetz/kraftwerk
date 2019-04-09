package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.hlist.*;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generator.Product6;
import dev.marksman.composablerandom.primitives.AggregateImpl;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.CompiledGenerator.compiledGenerator;
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
    private final CompiledGenerator<Trace<Integer>> sizeGenerator;

    private TracingInterpreter(Parameters parameters) {
        SizeSelector sizeSelector = parameters.getSizeSelector();
        this.sizeGenerator = compile(Generator.<Integer>generator(sizeSelector::selectSize)
                .labeled("sized"));
    }

    private <A> Result<RandomState, Trace<A>> traceResult(Generator<A> generator, Result<? extends RandomState, A> resultValue) {
        return result(resultValue.getNextState(), trace(resultValue.getValue(), generator));
    }

    private <A> CompiledGenerator<Trace<A>> traced(Generator<A> generator, CompiledGenerator<A> g) {
        return input -> traceResult(generator, g.run(input));
    }

    public <A> CompiledGenerator<Trace<A>> compile(Generator<A> generator) {

        if (generator instanceof Generator.Constant) {
            return traced(generator, constantImpl(((Generator.Constant<A>) generator).getValue()));
        }

        if (generator instanceof Generator.Custom) {
            return traced(generator, customImpl(((Generator.Custom<A>) generator).getFn()));
        }

        if (generator instanceof Generator.Mapped) {
            return handleMapped((Generator.Mapped<?, A>) generator);
        }

        if (generator instanceof Generator.FlatMapped) {
            return handleFlatMapped((Generator.FlatMapped<?, A>) generator);
        }

        if (generator instanceof Generator.NextInt) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextIntImpl());
        }

        if (generator instanceof Generator.NextLong) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextLongImpl());
        }

        if (generator instanceof Generator.NextBoolean) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextBooleanImpl());
        }

        if (generator instanceof Generator.NextDouble) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextDoubleImpl());
        }

        if (generator instanceof Generator.NextFloat) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextFloatImpl());
        }

        if (generator instanceof Generator.NextIntBounded) {
            int bound = ((Generator.NextIntBounded) generator).getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextIntBoundedImpl(bound));
        }

        if (generator instanceof Generator.NextIntExclusive) {
            Generator.NextIntExclusive g1 = (Generator.NextIntExclusive) generator;
            int origin = g1.getOrigin();
            int bound = g1.getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextIntExclusiveImpl(origin, bound));
        }

        if (generator instanceof Generator.NextIntBetween) {
            Generator.NextIntBetween g1 = (Generator.NextIntBetween) generator;
            int min = g1.getMin();
            int max = g1.getMax();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextIntBetweenImpl(min, max));
        }

        if (generator instanceof Generator.NextIntIndex) {
            Generator.NextIntIndex g1 = (Generator.NextIntIndex) generator;
            int bound = g1.getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextIntIndexImpl(bound));
        }

        if (generator instanceof Generator.NextLongBounded) {
            long bound = ((Generator.NextLongBounded) generator).getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextLongBoundedImpl(bound));
        }

        if (generator instanceof Generator.NextLongExclusive) {
            Generator.NextLongExclusive g1 = (Generator.NextLongExclusive) generator;
            long origin = g1.getOrigin();
            long bound = g1.getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextLongExclusiveImpl(origin, bound));
        }

        if (generator instanceof Generator.NextLongBetween) {
            Generator.NextLongBetween g1 = (Generator.NextLongBetween) generator;
            long min = g1.getMin();
            long max = g1.getMax();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextLongBetweenImpl(min, max));
        }

        if (generator instanceof Generator.NextLongIndex) {
            Generator.NextLongIndex g1 = (Generator.NextLongIndex) generator;
            long bound = g1.getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextLongIndexImpl(bound));
        }

        if (generator instanceof Generator.NextGaussian) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextGaussianImpl());
        }

        if (generator instanceof Generator.NextByte) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextByteImpl());
        }

        if (generator instanceof Generator.NextShort) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextShortImpl());
        }

        if (generator instanceof Generator.NextBytes) {
            Generator.NextBytes g1 = (Generator.NextBytes) generator;
            int count = g1.getCount();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextBytesImpl(count));
        }

        if (generator instanceof Generator.WithMetadata) {
            //noinspection unchecked
            return handleWithMetadata((Generator.WithMetadata) generator);
        }

        if (generator instanceof Generator.Sized) {
            return handleSized((Generator.Sized<A>) generator);
        }

        if (generator instanceof Generator.Aggregate) {
            //noinspection unchecked
            return handleAggregate((Generator.Aggregate) generator);
        }

        if (generator instanceof Generator.Product2) {
            //noinspection unchecked
            return handleProduct2((Generator.Product2) generator);
        }

        if (generator instanceof Generator.Product3) {
            //noinspection unchecked
            return handleProduct3((Generator.Product3) generator);
        }

        if (generator instanceof Generator.Product4) {
            //noinspection unchecked
            return handleProduct4((Generator.Product4) generator);
        }

        if (generator instanceof Generator.Product5) {
            //noinspection unchecked
            return handleProduct5((Generator.Product5) generator);
        }

        if (generator instanceof Generator.Product6) {
            //noinspection unchecked
            return handleProduct6((Generator.Product6) generator);
        }

        if (generator instanceof Generator.Product7) {
            //noinspection unchecked
            return handleProduct7((Generator.Product7) generator);
        }

        if (generator instanceof Generator.Product8) {
            //noinspection unchecked
            return handleProduct8((Generator.Product8) generator);
        }

        throw new IllegalStateException("Unimplemented generator");
    }


    private <In, Out> CompiledGenerator<Trace<Out>> handleMapped(Generator.Mapped<In, Out> generator) {
        return mappedImpl(t -> trace(generator.getFn().apply(t.getResult()),
                generator, singletonList(t)),
                compile(generator.getOperand()));
    }

    private <In, Out> CompiledGenerator<Trace<Out>> handleFlatMapped(Generator.FlatMapped<In, Out> generator) {
        Fn1<? super In, ? extends Generator<Out>> fn = generator.getFn();
        CompiledGenerator<Trace<In>> g1 = compile(generator.getOperand());

        return compiledGenerator(rs -> {
            Result<? extends RandomState, Trace<In>> r1 = g1.run(rs);
            Trace<In> trace1 = r1.getValue();
            Result<? extends RandomState, Trace<Out>> r2 = compile(fn.apply(trace1.getResult()))
                    .run(r1.getNextState());
            Trace<Out> trace2 = r2.getValue();
            return result(r2.getNextState(),
                    trace(trace2.getResult(), generator, asList(trace1, trace2)));
        });
    }

    private <A> CompiledGenerator<Trace<A>> handleSized(Generator.Sized<A> generator) {
        Fn1<Integer, Generator<A>> fn = generator.getFn();

        return compiledGenerator(rs -> {
            Result<? extends RandomState, Trace<Integer>> r1 = sizeGenerator.run(rs);
            Trace<Integer> sizeTrace = r1.getValue();
            Generator<A> inner = fn.apply(sizeTrace.getResult());
            Result<? extends RandomState, Trace<A>> r2 = compile(inner)
                    .run(r1.getNextState());
            Trace<A> innerTrace = r2.getValue();
            return result(r2.getNextState(),
                    trace(innerTrace.getResult(), inner, asList(sizeTrace, innerTrace)));
        });

    }

    private <A> CompiledGenerator<Trace<A>> handleWithMetadata(Generator.WithMetadata<A> generator) {
        CompiledGenerator<Trace<A>> inner = compile(generator.getOperand());
        return compiledGenerator(rs -> {
            Result<? extends RandomState, Trace<A>> run = inner.run(rs);
            Trace<A> innerTrace = run.getValue();
            return result(run.getNextState(),
                    trace(innerTrace.getResult(), generator, singletonList(innerTrace)));
        });
    }

    private <Elem, Builder, Out> CompiledGenerator<Trace<Out>> handleAggregate(Generator.Aggregate<Elem, Builder, Out> generator) {
        @SuppressWarnings("UnnecessaryLocalVariable")
        AggregateImpl<Trace<Elem>, TraceCollector<Builder>, Trace<Out>> aggregator = aggregateImpl(
                () -> new TraceCollector<>(generator.getInitialBuilderSupplier().get()),
                (tc, tracedElem) -> {
                    tc.state = generator.getAddFn().apply(tc.state, tracedElem.getResult());
                    tc.traces.add(tracedElem);
                    return tc;
                },
                tc -> trace(generator.getBuildFn().apply(tc.state), generator, tc.traces),
                map(this::compile, generator.getElements()));
        return aggregator;
    }

    private <A, B> CompiledGenerator<Trace<Tuple2<A, B>>> handleProduct2(
            Generator.Product2<A, B> generator) {
        CompiledGenerator<Trace<A>> ca = compile(generator.getA());
        CompiledGenerator<Trace<B>> cb = compile(generator.getB());

        return compiledGenerator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();

            return result(rb.getNextState(),
                    trace(tuple(ta.getResult(),
                            tb.getResult()),
                            generator,
                            asList(ta, tb)));
        });
    }

    private <A, B, C> CompiledGenerator<Trace<Tuple3<A, B, C>>> handleProduct3(
            Generator.Product3<A, B, C> generator) {
        CompiledGenerator<Trace<A>> ca = compile(generator.getA());
        CompiledGenerator<Trace<B>> cb = compile(generator.getB());
        CompiledGenerator<Trace<C>> cc = compile(generator.getC());

        return compiledGenerator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());
            Result<? extends RandomState, Trace<C>> rc = cc.run(rb.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();
            Trace<C> tc = rc.getValue();

            return result(rc.getNextState(),
                    trace(tuple(ta.getResult(),
                            tb.getResult(),
                            tc.getResult()),
                            generator,
                            asList(ta, tb, tc)));
        });
    }

    private <A, B, C, D> CompiledGenerator<Trace<Tuple4<A, B, C, D>>> handleProduct4(
            Generator.Product4<A, B, C, D> generator) {
        CompiledGenerator<Trace<A>> ca = compile(generator.getA());
        CompiledGenerator<Trace<B>> cb = compile(generator.getB());
        CompiledGenerator<Trace<C>> cc = compile(generator.getC());
        CompiledGenerator<Trace<D>> cd = compile(generator.getD());

        return compiledGenerator(in -> {
            Result<? extends RandomState, Trace<A>> ra = ca.run(in);
            Result<? extends RandomState, Trace<B>> rb = cb.run(ra.getNextState());
            Result<? extends RandomState, Trace<C>> rc = cc.run(rb.getNextState());
            Result<? extends RandomState, Trace<D>> rd = cd.run(rc.getNextState());

            Trace<A> ta = ra.getValue();
            Trace<B> tb = rb.getValue();
            Trace<C> tc = rc.getValue();
            Trace<D> td = rd.getValue();

            return result(rd.getNextState(),
                    trace(tuple(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult()),
                            generator,
                            asList(ta, tb, tc, td)));
        });
    }

    private <A, B, C, D, E> CompiledGenerator<Trace<Tuple5<A, B, C, D, E>>> handleProduct5(
            Generator.Product5<A, B, C, D, E> generator) {
        CompiledGenerator<Trace<A>> ca = compile(generator.getA());
        CompiledGenerator<Trace<B>> cb = compile(generator.getB());
        CompiledGenerator<Trace<C>> cc = compile(generator.getC());
        CompiledGenerator<Trace<D>> cd = compile(generator.getD());
        CompiledGenerator<Trace<E>> ce = compile(generator.getE());

        return compiledGenerator(in -> {
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
                    trace(tuple(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult(),
                            te.getResult()),
                            generator,
                            asList(ta, tb, tc, td, te)));
        });
    }

    private <A, B, C, D, E, F> CompiledGenerator<Trace<Tuple6<A, B, C, D, E, F>>> handleProduct6(
            Product6<A, B, C, D, E, F> generator) {
        CompiledGenerator<Trace<A>> ca = compile(generator.getA());
        CompiledGenerator<Trace<B>> cb = compile(generator.getB());
        CompiledGenerator<Trace<C>> cc = compile(generator.getC());
        CompiledGenerator<Trace<D>> cd = compile(generator.getD());
        CompiledGenerator<Trace<E>> ce = compile(generator.getE());
        CompiledGenerator<Trace<F>> cf = compile(generator.getF());

        return compiledGenerator(in -> {
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
                    trace(tuple(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult(),
                            te.getResult(),
                            tf.getResult()),
                            generator,
                            asList(ta, tb, tc, td, te, tf)));
        });
    }

    private <A, B, C, D, E, F, G> CompiledGenerator<Trace<Tuple7<A, B, C, D, E, F, G>>> handleProduct7(
            Generator.Product7<A, B, C, D, E, F, G> generator) {
        CompiledGenerator<Trace<A>> ca = compile(generator.getA());
        CompiledGenerator<Trace<B>> cb = compile(generator.getB());
        CompiledGenerator<Trace<C>> cc = compile(generator.getC());
        CompiledGenerator<Trace<D>> cd = compile(generator.getD());
        CompiledGenerator<Trace<E>> ce = compile(generator.getE());
        CompiledGenerator<Trace<F>> cf = compile(generator.getF());
        CompiledGenerator<Trace<G>> cg = compile(generator.getG());

        return compiledGenerator(in -> {
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
                    trace(tuple(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult(),
                            te.getResult(),
                            tf.getResult(),
                            tg.getResult()),
                            generator,
                            asList(ta, tb, tc, td, te, tf, tg)));
        });
    }

    private <A, B, C, D, E, F, G, H> CompiledGenerator<Trace<Tuple8<A, B, C, D, E, F, G, H>>> handleProduct8(
            Generator.Product8<A, B, C, D, E, F, G, H> generator) {
        CompiledGenerator<Trace<A>> ca = compile(generator.getA());
        CompiledGenerator<Trace<B>> cb = compile(generator.getB());
        CompiledGenerator<Trace<C>> cc = compile(generator.getC());
        CompiledGenerator<Trace<D>> cd = compile(generator.getD());
        CompiledGenerator<Trace<E>> ce = compile(generator.getE());
        CompiledGenerator<Trace<F>> cf = compile(generator.getF());
        CompiledGenerator<Trace<G>> cg = compile(generator.getG());
        CompiledGenerator<Trace<H>> ch = compile(generator.getH());

        return compiledGenerator(in -> {
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
                    trace(tuple(ta.getResult(),
                            tb.getResult(),
                            tc.getResult(),
                            td.getResult(),
                            te.getResult(),
                            tf.getResult(),
                            tg.getResult(),
                            th.getResult()),
                            generator,
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
