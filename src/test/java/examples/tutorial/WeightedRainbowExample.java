package examples.tutorial;

import static dev.marksman.kraftwerk.Generators.chooseOneOfWeightedValues;
import static dev.marksman.kraftwerk.Weighted.weighted;

public class WeightedRainbowExample {
    public static void main(String[] args) {
        chooseOneOfWeightedValues(weighted(7, "red"),
                weighted(6, "orange"),
                weighted(5, "yellow"),
                weighted(4, "green"),
                weighted(3, "blue"),
                weighted(2, "indigo"),
                weighted(1, "violet"))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //red
        //orange
        //orange
        //violet
        //red
        //orange
        //yellow
        //red
        //red
        //green
    }
}
