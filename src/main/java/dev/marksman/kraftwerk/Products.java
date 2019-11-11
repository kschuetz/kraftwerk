package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.*;
import dev.marksman.collectionviews.Vector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Result.result;
import static dev.marksman.kraftwerk.Trace.trace;

class Products {

    static <A, B, Out> Generator<Out> product(Generator<A> a,
                                              Generator<B> b,
                                              Fn2<A, B, Out> combine) {
        return new Product2<>(a, b, combine);
    }

    static <A, B, C, Out> Generator<Out> product(Generator<A> a,
                                                 Generator<B> b,
                                                 Generator<C> c,
                                                 Fn3<A, B, C, Out> combine) {
        return new Product3<>(a, b, c, combine);
    }

    static <A, B, C, D, Out> Generator<Out> product(Generator<A> a,
                                                    Generator<B> b,
                                                    Generator<C> c,
                                                    Generator<D> d,
                                                    Fn4<A, B, C, D, Out> combine) {
        return new Product4<>(a, b, c, d, combine);
    }

    static <A, B, C, D, E, Out> Generator<Out> product(Generator<A> a,
                                                       Generator<B> b,
                                                       Generator<C> c,
                                                       Generator<D> d,
                                                       Generator<E> e,
                                                       Fn5<A, B, C, D, E, Out> combine) {
        return new Product5<>(a, b, c, d, e, combine);
    }

    static <A, B, C, D, E, F, Out> Generator<Out> product(Generator<A> a,
                                                          Generator<B> b,
                                                          Generator<C> c,
                                                          Generator<D> d,
                                                          Generator<E> e,
                                                          Generator<F> f,
                                                          Fn6<A, B, C, D, E, F, Out> combine) {
        return new Product6<>(a, b, c, d, e, f, combine);
    }

    static <A, B, C, D, E, F, G, Out> Generator<Out> product(Generator<A> a,
                                                             Generator<B> b,
                                                             Generator<C> c,
                                                             Generator<D> d,
                                                             Generator<E> e,
                                                             Generator<F> f,
                                                             Generator<G> g,
                                                             Fn7<A, B, C, D, E, F, G, Out> combine) {
        return new Product7<>(a, b, c, d, e, f, g, combine);
    }

    static <A, B, C, D, E, F, G, H, Out> Generator<Out> product(Generator<A> a,
                                                                Generator<B> b,
                                                                Generator<C> c,
                                                                Generator<D> d,
                                                                Generator<E> e,
                                                                Generator<F> f,
                                                                Generator<G> g,
                                                                Generator<H> h,
                                                                Fn8<A, B, C, D, E, F, G, H, Out> combine) {
        return new Product8<>(a, b, c, d, e, f, g, h, combine);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product2<A, B, Out> implements Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product2");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Fn2<A, B, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                return result(rb.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product3<A, B, C, Out> implements Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product3");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Fn3<A, B, C, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                return result(rc.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue()));
            };
        }

        @Override
        public Generate<Trace<Out>> prepareTraced(Parameters parameters) {
            Generate<Trace<A>> tracedA = a.prepareTraced(parameters);
            Generate<Trace<B>> tracedB = b.prepareTraced(parameters);
            Generate<Trace<C>> tracedC = c.prepareTraced(parameters);
            return input -> {
                Result<? extends Seed, Trace<A>> ra = tracedA.apply(input);
                Result<? extends Seed, Trace<B>> rb = tracedB.apply(ra.getNextState());
                Result<? extends Seed, Trace<C>> rc = tracedC.apply(rb.getNextState());
                Trace<A> ta = ra.getValue();
                Trace<B> tb = rb.getValue();
                Trace<C> tc = rc.getValue();
                return result(rc.getNextState(),
                        trace(combine.apply(ta.getResult(),
                                tb.getResult(),
                                tc.getResult()),
                                this,
                                Vector.of(ta, tb, tc)));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product4<A, B, C, D, Out> implements Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product4");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Fn4<A, B, C, D, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                return result(rd.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product5<A, B, C, D, E, Out> implements Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product5");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Fn5<A, B, C, D, E, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, E>> runE = e.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                Result<? extends Seed, E> re = runE.apply(rd.getNextState());
                return result(re.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue(),
                                re.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product6<A, B, C, D, E, F, Out> implements Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product6");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Fn6<A, B, C, D, E, F, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, E>> runE = e.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, F>> runF = f.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                Result<? extends Seed, E> re = runE.apply(rd.getNextState());
                Result<? extends Seed, F> rf = runF.apply(re.getNextState());
                return result(rf.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue(),
                                re.getValue(),
                                rf.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product7<A, B, C, D, E, F, G, Out> implements Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product7");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Generator<G> g;
        private final Fn7<A, B, C, D, E, F, G, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, E>> runE = e.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, F>> runF = f.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, G>> runG = g.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                Result<? extends Seed, E> re = runE.apply(rd.getNextState());
                Result<? extends Seed, F> rf = runF.apply(re.getNextState());
                Result<? extends Seed, G> rg = runG.apply(rf.getNextState());
                return result(rg.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue(),
                                re.getValue(),
                                rf.getValue(),
                                rg.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product8<A, B, C, D, E, F, G, H, Out> implements Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product8");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Generator<G> g;
        private final Generator<H> h;
        private final Fn8<A, B, C, D, E, F, G, H, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, E>> runE = e.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, F>> runF = f.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, G>> runG = g.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, H>> runH = h.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                Result<? extends Seed, E> re = runE.apply(rd.getNextState());
                Result<? extends Seed, F> rf = runF.apply(re.getNextState());
                Result<? extends Seed, G> rg = runG.apply(rf.getNextState());
                Result<? extends Seed, H> rh = runH.apply(rg.getNextState());
                return result(rh.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue(),
                                re.getValue(),
                                rf.getValue(),
                                rg.getValue(),
                                rh.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }
}
