package examples.tutorial;

import static software.kes.kraftwerk.Generators.chooseOneOfValues;
import static software.kes.kraftwerk.Generators.chooseOneOfWeighted;

public class CardinalDirectionsExample {
    public static void main(String[] args) {
        chooseOneOfWeighted(chooseOneOfValues("N", "S", "W", "E").weighted(8),
                chooseOneOfValues("NW", "NE", "SW", "SE").weighted(1))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //W
        //S
        //E
        //N
        //W
        //S
        //SW
        //W
        //SE
        //N
    }
}
