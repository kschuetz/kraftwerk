package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.composablerandom.*;
import dev.marksman.composablerandom.spike.Product3ShrinkTree;
import dev.marksman.composablerandom.spike.ShrinkNode;
import dev.marksman.composablerandom.spike.ShrinkTree;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static dev.marksman.composablerandom.spike.ShrinkNode.shrinkNode;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product3Impl<A, B, C, Out> implements GeneratorImpl<Out> {
    private final GeneratorImpl<A> a;
    private final GeneratorImpl<B> b;
    private final GeneratorImpl<C> c;
    private final Fn3<A, B, C, Out> combine;

    @Override
    public Result<? extends Seed, Out> run(Seed input) {
        Result<? extends Seed, A> r1 = a.run(input);
        Result<? extends Seed, B> r2 = b.run(r1.getNextState());
        Result<? extends Seed, C> r3 = c.run(r2.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue());
        return result(r3.getNextState(), result);
    }

    @Override
    public Result<? extends Seed, ImmutableNonEmptyFiniteIterable<Out>> runShrinking(Seed input) {
        Result<? extends Seed, ImmutableNonEmptyFiniteIterable<A>> r1 = a.runShrinking(input);
        Result<? extends Seed, ImmutableNonEmptyFiniteIterable<B>> r2 = b.runShrinking(r1.getNextState());
        Result<? extends Seed, ImmutableNonEmptyFiniteIterable<C>> r3 = c.runShrinking(r2.getNextState());
        return result(r3.getNextState(),
                r1.getValue().cross(r2.getValue().cross(r3.getValue()))
                        .fmap(t -> combine.apply(t._1(), t._2()._1(), t._2()._2())));
    }

    @Override
    public Result<? extends Seed, ImmutableNonEmptyFiniteIterable<ShrinkNode<Out>>> runShrinking2(Seed input) {
        Result<? extends Seed, ImmutableNonEmptyFiniteIterable<ShrinkNode<A>>> r1 = a.runShrinking2(input);
        Result<? extends Seed, ImmutableNonEmptyFiniteIterable<ShrinkNode<B>>> r2 = b.runShrinking2(r1.getNextState());
        Result<? extends Seed, ImmutableNonEmptyFiniteIterable<ShrinkNode<C>>> r3 = c.runShrinking2(r2.getNextState());
        return result(r3.getNextState(),
                r1.getValue().cross(r2.getValue().cross(r3.getValue()))
                        .fmap(t -> {
                            Out apply = combine.apply(t._1().getValue(), t._2()._1().getValue(), t._2()._2().getValue());
                            return shrinkNode(apply);
                        }));

    }

    @Override
    public Result<? extends Seed, ShrinkTree<Out>> runShrinking3(Seed input) {
        Result<? extends Seed, ShrinkTree<A>> r1 = a.runShrinking3(input);
        Result<? extends Seed, ShrinkTree<B>> r2 = b.runShrinking3(r1.getNextState());
        Result<? extends Seed, ShrinkTree<C>> r3 = c.runShrinking3(r2.getNextState());

        Product3ShrinkTree<A, B, C, Out> tree = new Product3ShrinkTree<>(r1.getValue(), r2.getValue(), r3.getValue(),
                r1.getValue(), r2.getValue(), r3.getValue(), combine, 0);

        return result(r3.getNextState(), tree);
    }

    public static <A, B, C, Out> Product3Impl<A, B, C, Out> product3Impl(GeneratorImpl<A> a,
                                                                         GeneratorImpl<B> b,
                                                                         GeneratorImpl<C> c,
                                                                         Fn3<A, B, C, Out> combine) {
        return new Product3Impl<>(a, b, c, combine);
    }

    public static <A, B, C, Out> GeneratorImpl<Trace<Out>> tracedProduct3Impl(Generator<Out> source,
                                                                              GeneratorImpl<Trace<A>> a,
                                                                              GeneratorImpl<Trace<B>> b,
                                                                              GeneratorImpl<Trace<C>> c,
                                                                              Fn3<A, B, C, Out> combine) {
        return product3Impl(a, b, c,
                (ta, tb, tc) -> trace(combine.apply(ta.getResult(), tb.getResult(), tc.getResult()),
                        source, asList(ta, tb, tc)));
    }

}
