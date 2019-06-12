package testsupport;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.random.StandardGen;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneratorPair {
    private final long initialSeedValue;
    private final Random random;
    private final RandomState randomState;

    public long getInitialSeedValue() {
        return initialSeedValue;
    }

    public Random getRandom() {
        return random;
    }

    public RandomState getRandomState() {
        return randomState;
    }

    public GeneratorPair withRandomState(RandomState rs) {
        return new GeneratorPair(initialSeedValue, random, rs);
    }

    public GeneratorPair updateRandomState(Fn1<RandomState, RandomState> f) {
        return withRandomState(f.apply(randomState));
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
