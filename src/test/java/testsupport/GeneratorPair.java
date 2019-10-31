package testsupport;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.random.StandardGen;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneratorPair {
    private final long initialSeedValue;
    private final Random random;
    private final LegacySeed seed;

    public long getInitialSeedValue() {
        return initialSeedValue;
    }

    public Random getRandom() {
        return random;
    }

    public LegacySeed getSeed() {
        return seed;
    }

    public GeneratorPair withSeed(LegacySeed rs) {
        return new GeneratorPair(initialSeedValue, random, rs);
    }

    public GeneratorPair updateSeed(Fn1<LegacySeed, LegacySeed> f) {
        return withSeed(f.apply(seed));
    }

    public String info() {
        return "initial seed = " + initialSeedValue;
    }

    public static GeneratorPair generatorPair(long initialSeedValue) {
        Random random = new Random();
        random.setSeed(initialSeedValue);
        StandardGen standardGen = StandardGen.initStandardGen(initialSeedValue);
        return new GeneratorPair(initialSeedValue, random, standardGen);
    }

    public static GeneratorPair newRandomGeneratorPair() {
        return generatorPair(new Random().nextLong());
    }

}
