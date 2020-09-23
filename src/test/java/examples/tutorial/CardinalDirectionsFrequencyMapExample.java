package examples.tutorial;

import dev.marksman.kraftwerk.Generator;

import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;

public class CardinalDirectionsFrequencyMapExample {
    public static void main(String[] args) {
        Generator<String> cardinals = chooseOneOfValues("N", "S", "W", "E");
        Generator<String> interCardinals = chooseOneOfValues("NW", "NE", "SW", "SE");

        frequencyMap(cardinals.weighted(8))
                .add(interCardinals)
                .toGenerator()
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //W
        //N
        //W
        //SE
        //W
        //SW
        //N
        //E
        //E
        //E
    }
}
