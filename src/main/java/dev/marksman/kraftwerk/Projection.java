package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.projections.Projection5;

import static dev.marksman.kraftwerk.Constant.constant;

class Projection {

    static <A, B, C, D, E> Generator<Projection5<A, B, C, D, E>> projection5(Generator<Integer> columnCount,
                                                                             Generator<A> generatorA,
                                                                             Generator<B> generatorB,
                                                                             Generator<C> generatorC,
                                                                             Generator<D> generatorD,
                                                                             Generator<E> generatorE) {
//        columnCount
//                .flatMap(Projection::generateMask)
//                .flatMap(mask -> {
//
//                    return null;
//                });

        return null;
    }

    private static Generator<Integer> generateMask(int colCount) {
        // TODO
        return constant(1);
    }
}
