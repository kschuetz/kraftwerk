package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static dev.marksman.kraftwerk.Result.result;
import static dev.marksman.kraftwerk.ValueSupply.valueSupply;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValueSupplyTest {
    @Test
    void returnsTheSameSequenceEveryTime() {
        ValueSupply<Long> supply = valueSupply(s -> result(s, s.getSeedValue()), Seed.random());
        assertEquals(Vector.copyFrom(supply.take(100)), Vector.copyFrom(supply.take(100)));
    }

    @Test
    void toStreamReturnsSameSequence() {
        ValueSupply<Long> supply = valueSupply(s -> result(s, s.getSeedValue()), Seed.random());
        ImmutableVector<Long> firstStreamValues = Vector.copyFrom(supply.stream().limit(100).collect(Collectors.toList()));
        ImmutableVector<Long> secondStreamValues = Vector.copyFrom(supply.stream().limit(100).collect(Collectors.toList()));

        assertEquals(firstStreamValues, secondStreamValues);
        assertEquals(Vector.copyFrom(supply.take(100)), firstStreamValues);
    }
}
