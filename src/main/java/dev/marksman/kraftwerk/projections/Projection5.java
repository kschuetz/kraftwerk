package dev.marksman.kraftwerk.projections;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Projection5<A, B, C, D, E> {
    Maybe<A> a;
    Maybe<B> b;
    Maybe<C> c;
    Maybe<D> d;
    Maybe<E> e;
}
