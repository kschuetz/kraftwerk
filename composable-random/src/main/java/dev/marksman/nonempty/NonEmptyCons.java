package dev.marksman.nonempty;

import com.jnape.palatable.lambda.functor.Functor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static dev.marksman.nonempty.ImmutableCollection.immutableCollection;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class NonEmptyCons<A> implements NonEmptyList<A>, Functor<A, NonEmptyCons<?>> {
    private final A head;
    private final ImmutableCollection<A> tail;


    @Override
    public int size() {
        return 1 + tail.size();
    }

    @Override
    public A getHead() {
        return head;
    }

    @Override
    public ImmutableCollection<A> getTail() {
        return tail;
    }

    @Override
    public Iterator<A> iterator() {
        return cons(head, tail).iterator();
    }

    @Override
    public ImmutableCollection<A> slice(int startIndex) {
        if (startIndex == 0) {
            return null;
        } else {
            return null; //tail.slice(startIndex - 1);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B> NonEmptyCons<B> fmap(Function<? super A, ? extends B> fn) {
        return new NonEmptyCons<>(fn.apply(head), (ImmutableCollection<B>) tail.fmap(fn));
    }

    static <A> NonEmptyCons<A> nonEmptyCons(A head, Collection<A> tail) {
        return new NonEmptyCons<>(head, immutableCollection(tail));
    }

    static <A> NonEmptyCons<A> nonEmptyCons(NonEmptyCollection<A> source) {
        if (source instanceof NonEmptyCons<?>) {
            return (NonEmptyCons<A>) source;
        } else {
            return nonEmptyCons(source.getHead(), null);
        }
    }
}
