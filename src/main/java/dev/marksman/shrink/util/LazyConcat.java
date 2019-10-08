package dev.marksman.shrink.util;

import com.jnape.palatable.lambda.functions.Fn0;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

public class LazyConcat {

    public static <A> ImmutableFiniteIterable<A> lazyConcat(ImmutableFiniteIterable<A> xs, Fn0<ImmutableFiniteIterable<A>> other) {
        return xs.concat(() -> other.apply().iterator());
    }

}
