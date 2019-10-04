package dev.marksman.shrink.util;

import com.jnape.palatable.lambda.functions.Fn0;
import dev.marksman.enhancediterables.ImmutableIterable;

public class LazyConcat {

    public static <A> ImmutableIterable<A> lazyConcat(ImmutableIterable<A> xs, Fn0<ImmutableIterable<A>> other) {
        return xs.concat(() -> other.apply().iterator());
    }

}
