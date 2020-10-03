package examples.tutorial;

import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.kraftwerk.Generator;

import static dev.marksman.kraftwerk.Generators.generateDoubleFractional;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateString;
import static dev.marksman.kraftwerk.Generators.generateTuple;

public class CombiningThreeGenerators {
    public static void main(String[] args) {
        Generator<Tuple3<Integer, String, Double>> generator = generateTuple(generateInt(),
                generateString(),
                generateDoubleFractional());

        generator.run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // HList{ 1730204138 :: A(@'y)p#e: :: 0.11402224544546236 }
        // HList{ 1909756109 :: ';B :: 0.9884475029496926 }
        // HList{ 1809180523 :: "W>.<eS :: 0.5097816977203855 }
        // HList{ -540828092 :: ^Tld^2a#C}>N6U@ :: 0.7904007899645681 }
        // HList{ -829429249 ::  :: 0.3125739749760317 }
    }
}
