package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.instructions.AggregateImpl;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static dev.marksman.composablerandom.instructions.AggregateImpl.aggregateImpl;
import static dev.marksman.composablerandom.instructions.ConstantImpl.constantImpl;
import static dev.marksman.composablerandom.instructions.CustomImpl.customImpl;
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
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class TracingInterpreter {
    private final CompiledGenerator<Trace<Integer>> sizeGenerator;

    private TracingInterpreter(Context context) {
        SizeSelector sizeSelector = SizeSelectors.sizeSelector(context.getSizeParameters());
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
            Generator.NextIntExclusive instruction1 = (Generator.NextIntExclusive) generator;
            int origin = instruction1.getOrigin();
            int bound = instruction1.getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextIntExclusiveImpl(origin, bound));
        }

        if (generator instanceof Generator.NextIntBetween) {
            Generator.NextIntBetween instruction1 = (Generator.NextIntBetween) generator;
            int min = instruction1.getMin();
            int max = instruction1.getMax();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextIntBetweenImpl(min, max));
        }

        if (generator instanceof Generator.NextIntIndex) {
            Generator.NextIntIndex instruction1 = (Generator.NextIntIndex) generator;
            int bound = instruction1.getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextIntIndexImpl(bound));
        }

        if (generator instanceof Generator.NextLongBounded) {
            long bound = ((Generator.NextLongBounded) generator).getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextLongBoundedImpl(bound));
        }

        if (generator instanceof Generator.NextLongExclusive) {
            Generator.NextLongExclusive instruction1 = (Generator.NextLongExclusive) generator;
            long origin = instruction1.getOrigin();
            long bound = instruction1.getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextLongExclusiveImpl(origin, bound));
        }

        if (generator instanceof Generator.NextLongBetween) {
            Generator.NextLongBetween instruction1 = (Generator.NextLongBetween) generator;
            long min = instruction1.getMin();
            long max = instruction1.getMax();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextLongBetweenImpl(min, max));
        }

        if (generator instanceof Generator.NextLongIndex) {
            Generator.NextLongIndex instruction1 = (Generator.NextLongIndex) generator;
            long bound = instruction1.getBound();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextLongIndexImpl(bound));
        }

        if (generator instanceof Generator.NextGaussian) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextGaussianImpl());
        }

        if (generator instanceof Generator.NextBytes) {
            Generator.NextBytes instruction1 = (Generator.NextBytes) generator;
            int count = instruction1.getCount();
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) nextBytesImpl(count));
        }

        if (generator instanceof Generator.WithMetadata) {
            //noinspection unchecked
            return traced(generator, (CompiledGenerator<A>) compile(generator));
        }

        if (generator instanceof Generator.Sized) {
            return handleSized((Generator.Sized<A>) generator);
        }

        if (generator instanceof Generator.Aggregate) {
            //noinspection unchecked
            return handleAggregate((Generator.Aggregate) generator);
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

        return customImpl(rs -> {
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

        return customImpl(rs -> {
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
                map(this::compile, generator.getInstructions()));
        return aggregator;
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

        return customImpl(in -> {
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
            Tuple8<A, B, C, D, E, F, G, H> tuple = tuple(ta.getResult(),
                    tb.getResult(),
                    tc.getResult(),
                    td.getResult(),
                    te.getResult(),
                    tf.getResult(),
                    tg.getResult(),
                    th.getResult());

            return result(rh.getNextState(),
                    trace(tuple, generator, asList(ta, tb, tc, td, te, tf, tg, th)));
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

}
