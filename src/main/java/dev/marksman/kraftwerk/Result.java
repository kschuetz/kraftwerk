package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * An immutable object that contains both the result of a computation, and the value for the next state.
 *
 * @param <S> the state type - typically a {@link Seed}
 * @param <A> the value type - typically the value produced by a {@link GenerateFn}
 */
public final class Result<S, A> implements Product2<S, A>, Functor<A, Result<?, ?>>, Bifunctor<S, A, Result<?, ?>> {
    private final S nextState;
    private final A value;

    private Result(S nextState, A value) {
        this.nextState = nextState;
        this.value = value;
    }

    public static <S, A> Result<S, A> result(S nextState, A value) {
        return new Result<>(nextState, value);
    }

    public static <S, A> Result<S, A> result(Product2<S, A> p) {
        return new Result<>(p._1(), p._2());
    }

    @Override
    public S _1() {
        return nextState;
    }

    @Override
    public A _2() {
        return value;
    }

    /**
     * Returns a new {@code Result} that is the same result value as this one, but with the next state replaced.
     *
     * @param newNextState the new value for the next state
     * @return a {@code Result<S, A>}
     */
    public Result<S, A> withNextState(S newNextState) {
        return result(newNextState, value);
    }

    /**
     * Returns a new {@code Result} that is the same next state as this one, but with the result value replaced.
     *
     * @param newValue the new value
     * @return a {@code Result<S, A>}
     */
    public Result<S, A> withValue(A newValue) {
        return result(nextState, newValue);
    }

    /**
     * Returns a new {@code Result} that is the same as this one, with the result value transformed by a function.
     *
     * @param fn  a function that transforms the result value
     * @param <B> the new type of the result value
     * @return a {@code Result<S, B>}
     */
    @Override
    public <B> Result<S, B> fmap(Fn1<? super A, ? extends B> fn) {
        return result(nextState, fn.apply(value));
    }

    /**
     * Returns a new {@code Result} with both the result value and next state of this one transformed by functions.
     *
     * @param lFn a function that transforms the next state
     * @param rFn a function that transforms the result value
     * @param <C> the new type of the next state
     * @param <D> the new type of the result value
     * @return a {@code Result<C, D>}
     */
    @Override
    public <C, D> Result<C, D> biMap(Fn1<? super S, ? extends C> lFn, Fn1<? super A, ? extends D> rFn) {
        return result(lFn.apply(nextState), rFn.apply(value));
    }

    public S getNextState() {
        return this.nextState;
    }

    public A getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result<?, ?> result = (Result<?, ?>) o;

        if (!nextState.equals(result.nextState)) return false;
        return value != null ? value.equals(result.value) : result.value == null;
    }

    @Override
    public int hashCode() {
        int result = nextState.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "nextState=" + nextState +
                ", value=" + value +
                '}';
    }
}
