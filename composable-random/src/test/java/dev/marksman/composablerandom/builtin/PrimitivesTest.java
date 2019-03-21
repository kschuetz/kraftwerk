package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.State;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.VerifyUniformity.x2IsUniform;

class PrimitivesTest {

    @Test
    void generateIntIsUniform() {
        Generator<Integer> generator = Generators.generateInt(0, 100);
        ArrayList<Integer> sample = sample(5000, generator);

        int[] hist = histogram(101, sample);

//        Arrays.stream(hist).forEach(i -> System.out.print(i + ", "));
//        System.out.println();

        boolean isUniform = x2IsUniform(hist, 0.05);
//        System.out.println("isUniform = " + isUniform);

        assertTrue(isUniform);
    }


    private static <A> ArrayList<A> sample(int n, Generator<A> g) {
        State initial = State.state(initStandardGen());
        ArrayList<A> output = new ArrayList<>(n);
        take(n, g.infiniteStream(initial)).forEach(output::add);
        return output;
    }

    private static int[] histogram(int bins, Iterable<Integer> data) {
        int[] counts = new int[bins];
        data.forEach(i -> {
            if (i >= 0 && i < bins) counts[i]++;
        });
        return counts;
    }
}