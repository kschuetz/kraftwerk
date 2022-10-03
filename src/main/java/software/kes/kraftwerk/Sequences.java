package software.kes.kraftwerk;

import com.jnape.palatable.lambda.traversable.LambdaIterable;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.Vector;
import software.kes.collectionviews.VectorBuilder;
import software.kes.kraftwerk.constraints.IntRange;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static software.kes.kraftwerk.Generators.constant;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.constraints.IntRange.inclusive;

final class Sequences {
    private Sequences() {
    }

    static <A> Generator<ImmutableVector<A>> generateOrderedSequence(Generator<Integer> countForEachElement,
                                                                     ImmutableVector<A> orderedElems) {
        int size = orderedElems.size();
        if (orderedElems.isEmpty()) {
            return constant(Vector.empty());
        } else {
            return countForEachElement
                    .vectorOfSize(size)
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

    static <A> Generator<ImmutableVector<A>> generateOrderedSequence(IntRange countRangeForElements,
                                                                     ImmutableVector<A> orderedElems) {
        int minCountEachElement = countRangeForElements.minInclusive();
        int maxCountEachElement = countRangeForElements.maxInclusive();
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
                return generateOrderedSequence(generateInt(inclusive(min, max)), orderedElems);
            }
        }
    }
}
