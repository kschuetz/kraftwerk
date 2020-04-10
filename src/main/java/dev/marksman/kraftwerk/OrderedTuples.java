package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

final class OrderedTuples {

    private OrderedTuples() {

    }

    static <A extends Comparable<A>> Generator<Tuple2<A, A>> generateOrderedPair(Generator<A> generator) {
        return generator.pair().fmap(OrderedTuples::sort);
    }

    private static <A extends Comparable<A>> Tuple2<A, A> sort(Tuple2<A, A> pair) {
        return pair._1().compareTo(pair._2()) > 0
                ? pair.invert()
                : pair;
    }

}
