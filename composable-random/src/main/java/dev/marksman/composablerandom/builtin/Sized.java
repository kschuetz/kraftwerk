package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.SizeParameters;

import java.util.function.Function;

import static dev.marksman.composablerandom.Generator.contextDependent;
import static dev.marksman.composablerandom.builtin.Primitives.*;

class Sized {

    private static int DEFAULT_RANGE = 16;
    private static Generator<Integer> GENERATE_FROM_DEFAULT_RANGE = generateIntExclusive(DEFAULT_RANGE);
    private static Generator<Boolean> BOOST_PREFERRED = generateBoolean(2, 7);

    static <A> Generator<A> sized(Function<Integer, Generator<A>> g) {
        return contextDependent(s0 -> {
            SizeParameters sp = s0.getContext().getSizeParameters();

            return sp.getMinSize()
                    .match(_a -> sp.getMaxSize()
                                    .match(_b -> sp.getPreferredSize()
                                                    .match(_c -> noSizeParameters(g),
                                                            preferred -> preferredOnly(preferred, g)),
                                            maxSize -> sp.getPreferredSize()
                                                    .match(_d -> maxOnly(maxSize, g),
                                                            preferred -> maxPreferred(maxSize, preferred, g))),
                            minSize -> sp.getMaxSize()
                                    .match(_e -> sp.getPreferredSize()
                                                    .match(_f -> minOnly(minSize, g),
                                                            preferred -> minPreferred(minSize, preferred, g)),
                                            maxSize -> sp.getPreferredSize()
                                                    .match(_g -> minMax(minSize, maxSize, g),
                                                            preferred -> minMaxPreferred(minSize, maxSize, preferred, g))
                                    ))
                    .run(s0);
        });
    }

    private static <A> Generator<A> minMaxPreferred(int min, int max, int preferred, Function<Integer, Generator<A>> g) {
        return BOOST_PREFERRED
                .flatMap(usePreferred -> {
                    if (usePreferred) return g.apply(preferred);
                    else return minMax(min, max, g);
                });
    }

    private static <A> Generator<A> minMax(int min, int max, Function<Integer, Generator<A>> g) {
        return generateInt(min, max).flatMap(g);
    }

    private static <A> Generator<A> minPreferred(int min, int preferred, Function<Integer, Generator<A>> g) {
        return BOOST_PREFERRED
                .flatMap(usePreferred -> {
                    if (usePreferred) return g.apply(preferred);
                    else return minOnly(min, g);
                });
    }

    private static <A> Generator<A> minOnly(int min, Function<Integer, Generator<A>> g) {
        return GENERATE_FROM_DEFAULT_RANGE.flatMap(s -> g.apply(min + s));
    }

    private static <A> Generator<A> maxPreferred(int max, int preferred, Function<Integer, Generator<A>> g) {
        return BOOST_PREFERRED
                .flatMap(usePreferred -> {
                    if (usePreferred) return g.apply(preferred);
                    else return maxOnly(max, g);
                });
    }

    private static <A> Generator<A> maxOnly(int max, Function<Integer, Generator<A>> g) {
        return generateInt(0, max).flatMap(g);
    }

    private static <A> Generator<A> preferredOnly(int preferred, Function<Integer, Generator<A>> g) {
        return g.apply(preferred);
    }

    private static <A> Generator<A> noSizeParameters(Function<Integer, Generator<A>> g) {
        return GENERATE_FROM_DEFAULT_RANGE.flatMap(g);
    }

}
