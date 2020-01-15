package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static dev.marksman.kraftwerk.Generators.generateShuffled;
import static testsupport.Assert.assertForAll;

class ShuffleTest {

    @Test
    void testShuffleRetainsSameElements() {
        ImmutableVector<Integer> source = Vector.range(32);
        HashSet<Integer> expected = source.toCollection(HashSet::new);
        assertForAll(100, generateShuffled(source),
                result -> expected.equals(result.toCollection(HashSet::new)));
    }

    @Test
    void testShufflesCoversRange() {
        int[] first = new int[32];
        int[] middle = new int[32];
        int[] last = new int[32];
        generateShuffled(Vector.range(32))
                .run()
                .take(1000)
                .forEach(result -> {
                    first[result.unsafeGet(0)] += 1;
                    middle[result.unsafeGet(15)] += 1;
                    last[result.unsafeGet(31)] += 1;
                });
        // TODO: fix shuffle

//        System.out.println(Arrays.toString(first));
//        System.out.println(Arrays.toString(middle));
//        System.out.println(Arrays.toString(last));
//        assertTrue(coversRange(first));
//        assertTrue(coversRange(middle));
//        assertTrue(coversRange(last));
    }

}