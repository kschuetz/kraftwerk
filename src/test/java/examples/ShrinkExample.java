package examples;

import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.composablerandom.DefaultInterpreter;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.Seed;
import dev.marksman.composablerandom.StandardParameters;
import dev.marksman.composablerandom.spike.ShrinkTree;

import static dev.marksman.composablerandom.DefaultInterpreter.defaultInterpreter;
import static dev.marksman.composablerandom.Generator.generateInt;
import static dev.marksman.composablerandom.Initialize.randomInitialSeed;
import static dev.marksman.composablerandom.StandardParameters.defaultParameters;

public class ShrinkExample {

    public static void main(String[] args) {
        Generator<Integer> g = generateInt();
        DefaultInterpreter defaultInterpreter = defaultInterpreter();
        StandardParameters parameters = defaultParameters();

        Seed seed = randomInitialSeed();

        ShrinkTree<Tuple3<Integer, Integer, Integer>> tree = defaultInterpreter.compile(parameters, g.fmap(n -> n % 10).triple())
                .runShrinking3(seed).getValue();

        System.out.println("value: " + tree.getValue());
        tree.getHappyPath().take(200).forEach(x -> {
            System.out.println("  " + x);
        });

        System.out.println("----");
    }
}
