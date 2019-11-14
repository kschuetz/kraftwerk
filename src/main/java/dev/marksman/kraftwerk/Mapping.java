package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Reverse.reverse;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

class Mapping {

    static <A, B> Generator<B> mapped(Fn1<? super A, ? extends B> fn, Generator<A> source) {
        return new Mapped<>(source, fn::apply);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Mapped<In, A> implements Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("fmap");

        private final Generator<In> source;
        private final Fn1<In, A> fn;

        @Override
        public Generate<A> prepare(Parameters parameters) {
            Generate<In> g = source.prepare(parameters);
            return input -> g.apply(input).fmap(fn);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <B> Generator<B> fmap(Fn1<? super A, ? extends B> fn) {
            Iterable<Function<Object, Object>> fs = Vector.of(o -> fn.apply((A) o),
                    o -> this.fn.apply((In) o));
            return new ManyMapped(source, fs);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ManyMapped<In, A> implements Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("fmap");

        private final Generator<In> source;
        private final Iterable<Function<Object, Object>> functions;

        @SuppressWarnings("unchecked")
        @Override
        public Generate<A> prepare(Parameters parameters) {
            Generate<In> g = source.prepare(parameters);
            Fn1<Object, Object> fn = buildFn();
            return input -> g.apply(input).fmap(x -> (A) fn.apply(x));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <B> Generator<B> fmap(Fn1<? super A, ? extends B> fn) {
            return new ManyMapped<>(source, cons(o -> fn.apply((A) o), functions));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

        private Fn1<Object, Object> buildFn() {
            ArrayList<Function<Object, Object>> fnChain = toCollection(ArrayList::new, reverse(functions));
            return o -> foldLeft((x, fn) -> fn.apply(x), o, fnChain);
        }

    }
}
