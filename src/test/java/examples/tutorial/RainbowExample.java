package examples.tutorial;

import static software.kes.kraftwerk.Generators.chooseOneOfValues;

public class RainbowExample {
    public static void main(String[] args) {
        chooseOneOfValues("red", "orange", "yellow", "green", "blue", "indigo", "violet")
                .run()
                .take(10)
                .forEach(System.out::println);

        //sample output:
        //violet
        //green
        //orange
        //violet
        //yellow
        //red
        //yellow
        //green
        //blue
        //violet
    }
}
