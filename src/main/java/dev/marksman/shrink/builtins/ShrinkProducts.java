package dev.marksman.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.shrink.Shrink;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.shrink.util.LazyConcat.lazyConcat;

public class ShrinkProducts {

    public static <A, B> Shrink<Tuple2<A, B>> shrinkTuple2(Shrink<A> sa, Shrink<B> sb) {
        return shrinkProduct2(sa, sb, Tuple2::_1, Tuple2::_2, Tuple2::tuple);
    }

    public static <A, B, Out> Shrink<Out> shrinkProduct2(Shrink<A> sa, Shrink<B> sb,
                                                         Fn1<Out, A> getA,
                                                         Fn1<Out, B> getB,
                                                         Fn2<A, B, Out> combine) {
        return input -> {
            A t1 = getA.apply(input);
            B t2 = getB.apply(input);

            return lazyConcat(sa.apply(t1)
                            .fmap(a -> combine.apply(a, t2)),
                    () -> sb.apply(t2)
                            .fmap(b -> combine.apply(t1, b)));
        };
    }

    public static <A, B, Out> Shrink<Out> shrinkProduct2Alt(Shrink<A> sa, Shrink<B> sb,
                                                            Iso<Out, Out, Product2<A, B>, Product2<A, B>> iso) {
        Iso<Product2<A, B>, Product2<A, B>, Out, Out> mirror = iso.mirror();
        return input -> {


            Product2<A, B> p = view(iso, input);
            A t1 = p._1();
            B t2 = p._2();

            return lazyConcat(sa.apply(t1)
                            .fmap(a -> view(mirror, tuple(a, t2))),
                    () -> sb.apply(t2)
                            .fmap(b -> view(mirror, tuple(t1, b))));
        };
    }

}
