package testsupport;

import dev.marksman.composablerandom.Generator;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.Sample.sample;

public class Assert {
    public static <A> void assertForAll(Generator<A> generator, Function<A, Boolean> condition) {
        assertTrue(all(condition, sample(generator)));
    }
}
