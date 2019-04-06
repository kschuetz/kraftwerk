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
import static dev.marksman.composablerandom.instructions.PureImpl.pureImpl;
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

    public <A> CompiledGenerator<Trace<A>> compile(Instruction<A> instruction) {

        if (instruction instanceof Instruction.Constant) {
            return traced(PURE, pureImpl(((Instruction.Constant<A>) instruction).getValue()));
        }

        if (instruction instanceof Instruction.Custom) {
            return traced(CUSTOM, customImpl(((Instruction.Custom<A>) instruction).getFn()));
        }

        if (instruction instanceof Instruction.Mapped) {
            return handleMapped((Instruction.Mapped<?, A>) instruction);
        }

        if (instruction instanceof Instruction.FlatMapped) {
            return handleFlatMapped((Instruction.FlatMapped<?, A>) instruction);
        }

        if (instruction instanceof Instruction.NextInt) {
            //noinspection unchecked
            return traced(INT, (CompiledGenerator<A>) nextIntImpl());
        }

        if (instruction instanceof Instruction.NextLong) {
            //noinspection unchecked
            return traced(LONG, (CompiledGenerator<A>) nextLongImpl());
        }

        if (instruction instanceof Instruction.NextBoolean) {
            //noinspection unchecked
            return traced(BOOLEAN, (CompiledGenerator<A>) nextBooleanImpl());
        }

        if (instruction instanceof Instruction.NextDouble) {
            //noinspection unchecked
            return traced(DOUBLE, (CompiledGenerator<A>) nextDoubleImpl());
        }

        if (instruction instanceof Instruction.NextFloat) {
            //noinspection unchecked
            return traced(FLOAT, (CompiledGenerator<A>) nextFloatImpl());
        }

        if (instruction instanceof Instruction.NextIntBounded) {
            int bound = ((Instruction.NextIntBounded) instruction).getBound();
            //noinspection unchecked
            return traced(intInterval(0, bound, true),
                    (CompiledGenerator<A>) nextIntBoundedImpl(bound));
        }

        if (instruction instanceof Instruction.NextIntExclusive) {
            Instruction.NextIntExclusive instruction1 = (Instruction.NextIntExclusive) instruction;
            int origin = instruction1.getOrigin();
            int bound = instruction1.getBound();
            //noinspection unchecked
            return traced(intInterval(origin, bound, true),
                    (CompiledGenerator<A>) nextIntExclusiveImpl(origin, bound));
        }

        if (instruction instanceof Instruction.NextIntBetween) {
            Instruction.NextIntBetween instruction1 = (Instruction.NextIntBetween) instruction;
            int min = instruction1.getMin();
            int max = instruction1.getMax();
            //noinspection unchecked
            return traced(intInterval(min, max, false),
                    (CompiledGenerator<A>) nextIntBetweenImpl(min, max));
        }

        if (instruction instanceof Instruction.NextIntIndex) {
            Instruction.NextIntIndex instruction1 = (Instruction.NextIntIndex) instruction;
            int bound = instruction1.getBound();
            //noinspection unchecked
            return traced(interval("index", 0, bound, true),
                    (CompiledGenerator<A>) nextIntIndexImpl(bound));
        }

        if (instruction instanceof Instruction.NextLongBounded) {
            long bound = ((Instruction.NextLongBounded) instruction).getBound();
            //noinspection unchecked
            return traced(longInterval(0, bound, true),
                    (CompiledGenerator<A>) nextLongBoundedImpl(bound));
        }

        if (instruction instanceof Instruction.NextLongExclusive) {
            Instruction.NextLongExclusive instruction1 = (Instruction.NextLongExclusive) instruction;
            long origin = instruction1.getOrigin();
            long bound = instruction1.getBound();
            //noinspection unchecked
            return traced(longInterval(origin, bound, true),
                    (CompiledGenerator<A>) nextLongExclusiveImpl(origin, bound));
        }

        if (instruction instanceof Instruction.NextLongBetween) {
            Instruction.NextLongBetween instruction1 = (Instruction.NextLongBetween) instruction;
            long min = instruction1.getMin();
            long max = instruction1.getMax();
            //noinspection unchecked
            return traced(longInterval(min, max, false),
                    (CompiledGenerator<A>) nextLongBetweenImpl(min, max));
        }

        if (instruction instanceof Instruction.NextLongIndex) {
            Instruction.NextLongIndex instruction1 = (Instruction.NextLongIndex) instruction;
            long bound = instruction1.getBound();
            //noinspection unchecked
            return traced(interval("index", 0, bound, true),
                    (CompiledGenerator<A>) nextLongIndexImpl(bound));
        }

        if (instruction instanceof Instruction.NextGaussian) {
            //noinspection unchecked
            return traced(GAUSSIAN, (CompiledGenerator<A>) nextGaussianImpl());
        }

        if (instruction instanceof Instruction.NextBytes) {
            Instruction.NextBytes instruction1 = (Instruction.NextBytes) instruction;
            int count = instruction1.getCount();
            //noinspection unchecked
            return traced(primitiveMetadata("bytes[ " + count + "]"),
                    (CompiledGenerator<A>) nextBytesImpl(count));
        }

        if (instruction instanceof Instruction.Labeled) {
            Instruction.Labeled instruction1 = (Instruction.Labeled) instruction;
            //noinspection unchecked
            return traced(StandardMetadata.labeled(instruction1.getLabel()),
                    (CompiledGenerator<A>) compile(instruction1.getOperand()));
        }

        if (instruction instanceof Instruction.Sized) {
            return handleSized((Instruction.Sized<A>) instruction);
        }

        if (instruction instanceof Instruction.Aggregate) {
            //noinspection unchecked
            return handleAggregate((Instruction.Aggregate) instruction);
        }

        if (instruction instanceof Instruction.Product8) {
            //noinspection unchecked
            return handleProduct8((Instruction.Product8) instruction);
        }

        throw new IllegalStateException("Unimplemented instruction");
    }


    private <In, Out> CompiledGenerator<Trace<Out>> handleMapped(Instruction.Mapped<In, Out> instruction) {
        return mappedImpl(t -> trace(instruction.getFn().apply(t.getResult()),
                FMAP, singletonList(t)),
                compile(instruction.getOperand()));
    }

    private <In, Out> CompiledGenerator<Trace<Out>> handleFlatMapped(Instruction.FlatMapped<In, Out> instruction) {
        // TODO: fix this function; it is incorrect
        CompiledGenerator<Trace<In>> compile = compile(instruction.getOperand());
        return flatMappedImpl(t -> {
                    In r1 = t.getResult();
                    Instruction<Out> apply = instruction.getFn().apply(r1);

                    return compile(apply);
                },
                compile);
    }

    private <A> CompiledGenerator<Trace<A>> handleSized(Instruction.Sized<A> instruction) {
        // TODO: fix this implementation
        return sizedImpl(sizeSelector, rs -> compile(instruction.getFn().apply(rs)));
    }

    private <Elem, Builder, Out> CompiledGenerator<Trace<Out>> handleAggregate(Instruction.Aggregate<Elem, Builder, Out> instruction) {
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
            Instruction.Product8<A, B, C, D, E, F, G, H> instruction) {
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
//            Instruction.Product8<A, B, C, D, E, F, G, H> instruction) {
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
