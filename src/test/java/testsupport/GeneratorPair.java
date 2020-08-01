package testsupport;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Seed;
import dev.marksman.kraftwerk.core.StandardSeed;

import java.util.Random;

import static dev.marksman.kraftwerk.core.StandardSeed.initStandardSeed;

public final class GeneratorPair {
    private final long initialSeedValue;
    private final Random random;
    private final Seed seed;

    private GeneratorPair(long initialSeedValue, Random random, Seed seed) {
        this.initialSeedValue = initialSeedValue;
        this.random = random;
        this.seed = seed;
    }

    public static GeneratorPair generatorPair(long initialSeedValue) {
        Random random = new Random();
        random.setSeed(initialSeedValue);
        StandardSeed standardSeed = initStandardSeed(initialSeedValue);
        return new GeneratorPair(initialSeedValue, random, standardSeed);
    }

    public static GeneratorPair newRandomGeneratorPair() {
        return generatorPair(new Random().nextLong());
    }

    public long getInitialSeedValue() {
        return initialSeedValue;
    }

    public Random getRandom() {
        return random;
    }

    public Seed getSeed() {
        return seed;
    }

    public GeneratorPair withSeed(Seed rs) {
        return new GeneratorPair(initialSeedValue, random, rs);
    }

    public GeneratorPair updateSeed(Fn1<Seed, Seed> f) {
        return withSeed(f.apply(seed));
    }

    public String info() {
        return "initial seed = " + initialSeedValue;
    }
}
