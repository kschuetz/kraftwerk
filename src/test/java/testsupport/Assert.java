package testsupport;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.Sample.sample;

public class Assert {
    public static <A> void assertForAll(Generator<A> gen, Fn1<A, Boolean> condition) {
        assertTrue(all(condition, sample(gen)));
    }

    public static <A> void assertForAll(int sampleCount, Generator<A> gen, Fn1<A, Boolean> condition) {
        assertTrue(all(condition, sample(sampleCount, gen)));
    }
}
