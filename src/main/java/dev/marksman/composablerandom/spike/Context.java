package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import com.jnape.palatable.lambda.functions.builtin.fn1.Uncons;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static java.util.Collections.emptyList;


@AllArgsConstructor
public class Context {
    private static final Context DEFAULT_CONTEXT = context(emptyList());

    private final Iterable<Maybe<Integer>> sizeStack;

    public Maybe<Integer> getSize() {
        return head(sizeStack).flatMap(Id.id());
    }

    public Context withSize(int n) {
        return new Context(cons(just(n), sizeStack));
    }

    public Context withNoSize() {
        return new Context(cons(nothing(), sizeStack));
    }

    public Context restoreSize() {
        return new Context(Uncons.uncons(sizeStack).orElseThrow(() -> new IllegalStateException("size stack is empty"))._2());
    }

    private static Context context(Iterable<Maybe<Integer>> sizeStack) {
        return new Context(sizeStack);
    }

    public static Context defaultContext() {
        return DEFAULT_CONTEXT;
    }
}
