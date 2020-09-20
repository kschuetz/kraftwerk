package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;

import java.util.ArrayList;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Reverse.reverse;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

final class Mapping {
    private Mapping() {
    }

    static <A, B> Generator<B> mapped(Fn1<? super A, ? extends B> fn, Generator<A> source) {
        return new Mapped<>(source, fn::apply);
    }

    private static class Mapped<In, A> implements Generator<A> {
        private static final Maybe<String> LABEL = Maybe.just("fmap");

        private final Generator<In> source;
        private final Fn1<In, A> fn;

        private Mapped(Generator<In> source, Fn1<In, A> fn) {
            this.source = source;
            this.fn = fn;
        }

        @Override
        public GenerateFn<A> createGenerateFn(GeneratorParameters generatorParameters) {
            GenerateFn<In> g = source.createGenerateFn(generatorParameters);
            return input -> g.apply(input).fmap(fn);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <B> Generator<B> fmap(Fn1<? super A, ? extends B> fn) {
            Iterable<Function<Object, Object>> fs = Vector.of(o -> fn.apply((A) o),
                    o -> this.fn.apply((In) o));
            return new ManyMapped<>(source, fs);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    private static class ManyMapped<In, A> implements Generator<A> {
        private static final Maybe<String> LABEL = Maybe.just("fmap");

        private final Generator<In> source;
        private final Iterable<Function<Object, Object>> functions;

        private ManyMapped(Generator<In> source, Iterable<Function<Object, Object>> functions) {
            this.source = source;
            this.functions = functions;
        }

        @SuppressWarnings("unchecked")
        @Override
        public GenerateFn<A> createGenerateFn(GeneratorParameters generatorParameters) {
            GenerateFn<In> g = source.createGenerateFn(generatorParameters);
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
