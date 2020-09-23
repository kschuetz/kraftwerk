package examples.tutorial;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.CharRange;
import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.generateChar;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateMap;

public class MapExample {
    public static void main(String[] args) {
        Generator<Character> keyGenerator = generateChar(CharRange.from('A').to('Z'));
        Generator<Integer> valueGenerator = generateInt(IntRange.from(1).to(10));
        generateMap(keyGenerator, valueGenerator)
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //{B=2, R=3, S=3, F=7, W=2, I=2, Y=9, O=3}
        //{Q=6, C=1, T=3, E=2, G=6, I=10, J=9, L=5, M=7}
        //{}
        //{P=10, A=5, B=7, S=7, T=4, W=3, G=3, H=1, Y=7, M=2, N=5, O=1}
        //{T=7, U=6, E=4, W=6, I=8, K=7, N=3}
        //{G=7, H=6, I=6, J=5, M=4, N=10}
        //{}
        //{}
        //{A=9, C=6, E=2, I=1, J=6, K=4, L=2, M=7, N=1, P=9, Q=3, S=4, U=9, W=1, X=8}
        //{B=8, D=4, T=1, E=10, G=1, Y=8, I=6, L=10, M=2, O=1}
    }
}
