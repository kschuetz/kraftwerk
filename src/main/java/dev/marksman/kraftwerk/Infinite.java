package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.core.BuildingBlocks;

import static dev.marksman.kraftwerk.Tap.tap;
import static dev.marksman.kraftwerk.ValueSupply.valueSupply;

final class Infinite {
    private Infinite() {
    }

    static <A> Generator<ValueSupply<A>> generateInfiniteIterable(Generator<A> gen) {
        return tap(gen, (g1, input) -> {
            Result<Seed, Long> initialState = BuildingBlocks.nextLong(input);
            return valueSupply(g1, Seed.create(initialState._2()));
        });
    }
}
