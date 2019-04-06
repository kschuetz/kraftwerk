package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import dev.marksman.composablerandom.instructions.AggregateImpl;
import dev.marksman.composablerandom.metadata.Metadata;
import dev.marksman.composablerandom.metadata.StandardMetadata;
import dev.marksman.composablerandom.tracing.Trace;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.instructions.AggregateImpl.aggregateImpl;
import static dev.marksman.composablerandom.instructions.ConstantImpl.constantImpl;
import static dev.marksman.composablerandom.instructions.CustomImpl.customImpl;
import static dev.marksman.composablerandom.instructions.FlatMappedImpl.flatMappedImpl;
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
import static dev.marksman.composablerandom.instructions.SizedImpl.sizedImpl;
import static dev.marksman.composablerandom.metadata.PrimitiveMetadata.primitiveMetadata;
import static dev.marksman.composablerandom.tracing.Trace.trace;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class TracingInterpreter {

    private static final Metadata PURE = primitiveMetadata("pure");
    private static final Metadata CUSTOM = primitiveMetadata("custom");
    private static final Metadata FMAP = primitiveMetadata("fmap");
    private static final Metadata FLAT_MAP = primitiveMetadata("flatMap");
    private static final Metadata INT = primitiveMetadata("int");
    private static final Metadata LONG = primitiveMetadata("long");
    private static final Metadata BOOLEAN = primitiveMetadata("boolean");
    private static final Metadata DOUBLE = primitiveMetadata("double");
    private static final Metadata FLOAT = primitiveMetadata("float");
    private static final Metadata GAUSSIAN = primitiveMetadata("gaussian");
    private static final Metadata AGGREGATE = primitiveMetadata("aggregate");
    private static final Metadata PRODUCT8 = primitiveMetadata("product8");

    private final SizeSelector sizeSelector;

    private TracingInterpreter(Context context) {
        this.sizeSelector = SizeSelectors.sizeSelector(context.getSizeParameters());
    }

    private <A> Result<RandomState, Trace<A>> traceResult(RandomState randomState, Metadata metadata, A resultValue) {
        return result(randomState, trace(resultValue, metadata));
    }

    private <A> Result<RandomState, Trace<A>> traceResult(Metadata metadata, Result<? extends RandomState, A> resultValue) {
        return result(resultValue.getNextState(), trace(resultValue.getValue(), metadata));
    }

    private <A> CompiledGenerator<Trace<A>> traced(Metadata metadata, CompiledGenerator<A> g) {
        return input -> traceResult(metadata, g.run(input));
    }

    public <A> CompiledGenerator<Trace<A>> compile(Generator<A> generator) {

        if (generator instanceof Generator.Constant) {
            return traced(PURE, constantImpl(((Generator.Constant<A>) generator).getValue()));
        }

        if (generator instanceof Generator.Custom) {
            return traced(CUSTOM, customImpl(((Generator.Custom<A>) generator).getFn()));
        }

        if (generator instanceof Generator.Mapped) {
            return handleMapped((Generator.Mapped<?, A>) generator);
        }

        if (generator instanceof Generator.FlatMapped) {
            return handleFlatMapped((Generator.FlatMapped<?, A>) generator);
        }

        if (generator instanceof Generator.NextInt) {
            //noinspection unchecked
            return traced(INT, (CompiledGenerator<A>) nextIntImpl());
        }

        if (generator instanceof Generator.NextLong) {
            //noinspection unchecked
            return traced(LONG, (CompiledGenerator<A>) nextLongImpl());
        }

        if (generator instanceof Generator.NextBoolean) {
            //noinspection unchecked
            return traced(BOOLEAN, (CompiledGenerator<A>) nextBooleanImpl());
        }

        if (generator instanceof Generator.NextDouble) {
            //noinspection unchecked
            return traced(DOUBLE, (CompiledGenerator<A>) nextDoubleImpl());
        }

        if (generator instanceof Generator.NextFloat) {
            //noinspection unchecked
            return traced(FLOAT, (CompiledGenerator<A>) nextFloatImpl());
        }

        if (generator instanceof Generator.NextIntBounded) {
            int bound = ((Generator.NextIntBounded) generator).getBound();
            //noinspection unchecked
            return traced(intInterval(0, bound, true),
                    (CompiledGenerator<A>) nextIntBoundedImpl(bound));
        }

        if (generator instanceof Generator.NextIntExclusive) {
            Generator.NextIntExclusive instruction1 = (Generator.NextIntExclusive) generator;
            int origin = instruction1.getOrigin();
            int bound = instruction1.getBound();
            //noinspection unchecked
            return traced(intInterval(origin, bound, true),
                    (CompiledGenerator<A>) nextIntExclusiveImpl(origin, bound));
        }

        if (generator instanceof Generator.NextIntBetween) {
            Generator.NextIntBetween instruction1 = (Generator.NextIntBetween) generator;
            int min = instruction1.getMin();
            int max = instruction1.getMax();
            //noinspection unchecked
            return traced(intInterval(min, max, false),
                    (CompiledGenerator<A>) nextIntBetweenImpl(min, max));
        }

        if (generator instanceof Generator.NextIntIndex) {
            Generator.NextIntIndex instruction1 = (Generator.NextIntIndex) generator;
            int bound = instruction1.getBound();
            //noinspection unchecked
            return traced(interval("index", 0, bound, true),
                    (CompiledGenerator<A>) nextIntIndexImpl(bound));
        }

        if (generator instanceof Generator.NextLongBounded) {
            long bound = ((Generator.NextLongBounded) generator).getBound();
            //noinspection unchecked
            return traced(longInterval(0, bound, true),
                    (CompiledGenerator<A>) nextLongBoundedImpl(bound));
        }

        if (generator instanceof Generator.NextLongExclusive) {
            Generator.NextLongExclusive instruction1 = (Generator.NextLongExclusive) generator;
            long origin = instruction1.getOrigin();
            long bound = instruction1.getBound();
            //noinspection unchecked
            return traced(longInterval(origin, bound, true),
                    (CompiledGenerator<A>) nextLongExclusiveImpl(origin, bound));
        }

        if (generator instanceof Generator.NextLongBetween) {
            Generator.NextLongBetween instruction1 = (Generator.NextLongBetween) generator;
            long min = instruction1.getMin();
            long max = instruction1.getMax();
            //noinspection unchecked
            return traced(longInterval(min, max, false),
                    (CompiledGenerator<A>) nextLongBetweenImpl(min, max));
        }

        if (generator instanceof Generator.NextLongIndex) {
            Generator.NextLongIndex instruction1 = (Generator.NextLongIndex) generator;
            long bound = instruction1.getBound();
            //noinspection unchecked
            return traced(interval("index", 0, bound, true),
                    (CompiledGenerator<A>) nextLongIndexImpl(bound));
        }

        if (generator instanceof Generator.NextGaussian) {
            //noinspection unchecked
            return traced(GAUSSIAN, (CompiledGenerator<A>) nextGaussianImpl());
        }

        if (generator instanceof Generator.NextBytes) {
            Generator.NextBytes instruction1 = (Generator.NextBytes) generator;
            int count = instruction1.getCount();
            //noinspection unchecked
            return traced(primitiveMetadata("bytes[ " + count + "]"),
                    (CompiledGenerator<A>) nextBytesImpl(count));
        }

        if (generator instanceof Generator.Labeled) {
            Generator.Labeled instruction1 = (Generator.Labeled) generator;
            //noinspection unchecked
            return traced(StandardMetadata.labeled(instruction1.getLabel()),
                    (CompiledGenerator<A>) compile(instruction1.getOperand()));
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


    private <In, Out> CompiledGenerator<Trace<Out>> handleMapped(Generator.Mapped<In, Out> instruction) {
        return mappedImpl(t -> trace(instruction.getFn().apply(t.getResult()),
                FMAP, singletonList(t)),
                compile(instruction.getOperand()));
    }

    private <In, Out> CompiledGenerator<Trace<Out>> handleFlatMapped(Generator.FlatMapped<In, Out> instruction) {
        // TODO: fix this function; it is incorrect
        CompiledGenerator<Trace<In>> compile = compile(instruction.getOperand());
        return flatMappedImpl(t -> {
                    In r1 = t.getResult();
                    Generator<Out> apply = instruction.getFn().apply(r1);

                    return compile(apply);
                },
                compile);
    }

    private <A> CompiledGenerator<Trace<A>> handleSized(Generator.Sized<A> instruction) {
        // TODO: fix this implementation
        return sizedImpl(sizeSelector, rs -> compile(instruction.getFn().apply(rs)));
    }

    private <Elem, Builder, Out> CompiledGenerator<Trace<Out>> handleAggregate(Generator.Aggregate<Elem, Builder, Out> instruction) {
        @SuppressWarnings("UnnecessaryLocalVariable")
        AggregateImpl<Trace<Elem>, TraceCollector<Builder>, Trace<Out>> aggregator = aggregateImpl(
                () -> new TraceCollector<>(instruction.getInitialBuilderSupplier().get()),
                (tc, tracedElem) -> {
                    tc.state = instruction.getAddFn().apply(tc.state, tracedElem.getResult());
                    tc.traces.add(tracedElem);
                    return tc;
                },
                tc -> trace(instruction.getBuildFn().apply(tc.state), AGGREGATE, tc.traces),
                map(this::compile, instruction.getInstructions()));
        return aggregator;
    }

    private <A, B, C, D, E, F, G, H> CompiledGenerator<Trace<Tuple8<A, B, C, D, E, F, G, H>>> handleProduct8(
            Generator.Product8<A, B, C, D, E, F, G, H> instruction) {
        CompiledGenerator<Trace<A>> ca = compile(instruction.getA());
        CompiledGenerator<Trace<B>> cb = compile(instruction.getB());
        CompiledGenerator<Trace<C>> cc = compile(instruction.getC());
        CompiledGenerator<Trace<D>> cd = compile(instruction.getD());
        CompiledGenerator<Trace<E>> ce = compile(instruction.getE());
        CompiledGenerator<Trace<F>> cf = compile(instruction.getF());
        CompiledGenerator<Trace<G>> cg = compile(instruction.getG());
        CompiledGenerator<Trace<H>> ch = compile(instruction.getH());

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
                    trace(tuple, PRODUCT8, asList(ta, tb, tc, td, te, tf, tg, th)));
        });
    }

//    private <A, B, C, D, E, F, G, H> CompiledGenerator<Trace<Tuple8<A, B, C, D, E, F, G, H>>> handleProduct8(
//            Generator.Product8<A, B, C, D, E, F, G, H> instruction) {
//        CompiledGenerator<Trace<A>> ca = compile(instruction.getA());
//        CompiledGenerator<Trace<B>> cb = compile(instruction.getB());
//        CompiledGenerator<Trace<C>> cc = compile(instruction.getC());
//        CompiledGenerator<Trace<D>> cd = compile(instruction.getD());
//        CompiledGenerator<Trace<E>> ce = compile(instruction.getE());
//        CompiledGenerator<Trace<F>> cf = compile(instruction.getF());
//        CompiledGenerator<Trace<G>> cg = compile(instruction.getG());
//        CompiledGenerator<Trace<H>> ch = compile(instruction.getH());
//
//        Product8Impl<Trace<A>, Trace<B>, Trace<C>, Trace<D>, Trace<E>, Trace<F>, Trace<G>, Trace<H>> tp8 =
//                product8Impl(ca, cb, cc, cd, ce, cf, cg, ch);
//
//        Fn1<RandomState, Result<? extends RandomState, Trace<Tuple8<A, B, C, D, E, F, G, H>>>> fmap = tp8.fmap(r1 -> {
//            Tuple8<Trace<A>, Trace<B>, Trace<C>, Trace<D>, Trace<E>, Trace<F>, Trace<G>, Trace<H>> value = r1.getValue();
//            return result(r1.getNextState(),
//                    trace(tuple(value._1().getResult(),
//                            value._2().getResult(),
//                            value._3().getResult(),
//                            value._4().getResult(),
//                            value._5().getResult(),
//                            value._6().getResult(),
//                            value._7().getResult(),
//                            value._8().getResult()),
//                            PRODUCT8,
//                            asList(value._1(), value._2(), value._3(), value._4(),
//                                    value._5(), value._6(), value._7(), value._8())));
//        });
//    }

    private static class TraceCollector<State> {
        ArrayList<Trace<?>> traces;
        State state;

        TraceCollector(State state) {
            this.traces = new ArrayList<>();
            this.state = state;
        }
    }

    private static Metadata intInterval(int min, int max, boolean exclusive) {
        return interval("int", min, max, exclusive);
    }

    private static Metadata longInterval(long min, long max, boolean exclusive) {
        return interval("long", min, max, exclusive);
    }

    private static Metadata interval(String label, long min, long max, boolean exclusive) {
        return primitiveMetadata(label + " [" + min + ", " + max + (exclusive ? ")" : "]"));
    }
}
