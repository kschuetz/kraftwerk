package examples.tutorial;

import static software.kes.kraftwerk.Weighted.weighted;
import static software.kes.kraftwerk.frequency.FrequencyMap.frequencyMapFirstValue;

public class RainbowFrequencyMapExample {
    public static void main(String[] args) {
        frequencyMapFirstValue(weighted(7, "red"))
                .addValue(weighted(6, "orange"))
                .addValue(weighted(5, "yellow"))
                .addValue(weighted(4, "green"))
                .addValue(weighted(3, "blue"))
                .addValue(weighted(2, "indigo"))
                .addValue("violet")
                .toGenerator()
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //red
        //orange
        //yellow
        //green
        //green
        //orange
        //yellow
        //yellow
        //blue
        //yellow
    }
}
