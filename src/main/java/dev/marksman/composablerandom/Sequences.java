package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.traversable.LambdaIterable;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static dev.marksman.composablerandom.Generate.constant;
import static dev.marksman.composablerandom.Generate.generateInt;

class Sequences {

    static <A> Generate<ImmutableVector<A>> generateOrderedSequence(Generate<Integer> countForEachElement,
                                                                    ImmutableVector<A> orderedElems) {
        int size = orderedElems.size();
        if (orderedElems.isEmpty()) {
            return constant(Vector.empty());
        } else {
            return countForEachElement
                    .vectorOfN(size)
                    .fmap(counts -> {
                        VectorBuilder<A> result = Vector.builder();
                        for (int i = 0; i < size; i++) {
                            A elem = orderedElems.unsafeGet(i);
                            int count = counts.unsafeGet(i);
                            for (int j = 0; j < count; j++) {
                                result = result.add(elem);
                            }
                        }
                        return result.build();
                    });
        }
    }

    static <A> Generate<ImmutableVector<A>> generateOrderedSequence(int minCountEachElement,
                                                                    int maxCountEachElement,
                                                                    ImmutableVector<A> orderedElems) {
        if (orderedElems.isEmpty() || maxCountEachElement < 1) {
            return constant(Vector.empty());
        } else {
            int min = Math.max(0, minCountEachElement);
            int max = Math.max(maxCountEachElement, min);
            if (max == min) {
                return constant(max == 1
                        ? orderedElems
                        : Vector.copyFrom(LambdaIterable.wrap(orderedElems)
                        .flatMap(elem -> LambdaIterable.wrap(replicate(max, elem)))
                        .unwrap()));
            } else {
                return generateOrderedSequence(generateInt(min, max), orderedElems);
            }
        }
    }

}
