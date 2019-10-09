package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn3;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

public class Product3ShrinkTree<A, B, C, Out> implements ShrinkTree<Out> {
    private final ShrinkTree<A> orig1;
    private final ShrinkTree<B> orig2;
    private final ShrinkTree<C> orig3;
    private final ShrinkTree<A> st1;
    private final ShrinkTree<B> st2;
    private final ShrinkTree<C> st3;
    private final Fn3<A, B, C, Out> combineFn;
    private final int phase;

    public Product3ShrinkTree(ShrinkTree<A> orig1, ShrinkTree<B> orig2, ShrinkTree<C> orig3,
                              ShrinkTree<A> st1, ShrinkTree<B> st2, ShrinkTree<C> st3,
                              Fn3<A, B, C, Out> combineFn,
                              int phase) {
        this.orig1 = orig1;
        this.orig2 = orig2;
        this.orig3 = orig3;
        this.st1 = st1;
        this.st2 = st2;
        this.st3 = st3;
        this.combineFn = combineFn;
        this.phase = phase;
    }

    @Override
    public Maybe<ShrinkTree<Out>> getLeft() {
        return nothing();
    }

    @Override
    public Maybe<ShrinkTree<Out>> getRight() {
        switch (phase) {
            case 0:
                return st1.getRight()
                        .match(__ -> just(new Product3ShrinkTree<>(orig1, orig2, orig3, orig1, orig2, orig3, combineFn, 1)),
                                next1 -> just(new Product3ShrinkTree<>(orig1, orig2, orig3, next1, orig2, orig3, combineFn, 0)));
            case 1:
                return st2.getRight()
                        .match(__ -> just(new Product3ShrinkTree<>(orig1, orig2, orig3, orig1, orig2, orig3, combineFn, 2)),
                                next2 -> just(new Product3ShrinkTree<>(orig1, orig2, orig3, orig1, next2, orig3, combineFn, 1)));
            case 2:
                return st3.getRight()
                        .match(__ -> nothing(),
                                next3 -> just(new Product3ShrinkTree<>(orig1, orig2, orig3, orig1, orig2, next3, combineFn, 2)));
            default:
                return nothing();
        }
    }

    @Override
    public Out getValue() {
        return combineFn.apply(st1.getValue(), st2.getValue(), st3.getValue());
    }
}
