package software.kes.kraftwerk;

import org.junit.jupiter.api.Test;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.Vector;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static software.kes.kraftwerk.ValueSupply.valueSupply;

class ValueSupplyTest {
    @Test
    void returnsTheSameSequenceEveryTime() {
        ValueSupply<Long> supply = valueSupply(s -> Result.result(s, s.getSeedValue()), Seed.random());
        assertEquals(Vector.copyFrom(supply.take(100)), Vector.copyFrom(supply.take(100)));
    }

    @Test
    void toStreamReturnsSameSequence() {
        ValueSupply<Long> supply = valueSupply(s -> Result.result(s, s.getSeedValue()), Seed.random());
        ImmutableVector<Long> firstStreamValues = Vector.copyFrom(supply.stream().limit(100).collect(Collectors.toList()));
        ImmutableVector<Long> secondStreamValues = Vector.copyFrom(supply.stream().limit(100).collect(Collectors.toList()));

        assertEquals(firstStreamValues, secondStreamValues);
        assertEquals(Vector.copyFrom(supply.take(100)), firstStreamValues);
    }
}
