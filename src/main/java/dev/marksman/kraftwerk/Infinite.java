package dev.marksman.kraftwerk;

import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;
import dev.marksman.kraftwerk.core.BuildingBlocks;

import static dev.marksman.kraftwerk.ValueSupply.valueSupply;

class Infinite {

    static <A> Generator<ImmutableNonEmptyIterable<A>> generateInfiniteIterable(Generator<A> gen) {
        return Tap.tap(gen, (g1, input) -> {
            Result<Seed, Long> initialState = BuildingBlocks.nextLong(input);
            return valueSupply(g1, Seed.create(initialState._2()));
        });
    }

}
