package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.kraftwerk.core.BuildingBlocks;
import dev.marksman.kraftwerk.core.StandardSeed;

import java.util.Random;

public interface Seed {
    long getSeedValue();

    Seed perturb(long value);

    Seed setNextSeedValue(long value);

    default Tuple2<Seed, Seed> split() {
        return BuildingBlocks.split(this);
    }

    static Seed create(long value) {
        return StandardSeed.initStandardSeed(value);
    }

    static Seed random() {
        return create(new Random().nextLong());
    }
}
