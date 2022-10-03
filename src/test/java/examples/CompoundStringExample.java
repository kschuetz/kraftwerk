package examples;

import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Generators;
import software.kes.kraftwerk.frequency.FrequencyMap;

import static software.kes.kraftwerk.Weighted.weighted;
import static software.kes.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static software.kes.kraftwerk.weights.MaybeWeights.justs;
import static software.kes.kraftwerk.weights.MaybeWeights.nothings;

public class CompoundStringExample {

    public static void main(String[] args) {
        Generator<String> sentence = Generators.stringGeneratorBuilder()
                .add(Generators.chooseOneOfValues("The", "A"))
                .add(frequencyMap(Generators.chooseOneOfValues("quick", "fast", "speedy").weighted(3))
                        .add(Generators.chooseOneOfValues("slow", "reluctant", "sleepy"))
                        .toGenerator())
                .add(FrequencyMap.frequencyMapFirstValue(weighted(5, "brown"))
                        .addValue(weighted(4, "red"))
                        .addValue(weighted(3, "orange"))
                        .addValue(weighted(2, "white"))
                        .addValue(weighted(1, "purple")).toGenerator())
                .add(Generators.chooseOneOfValues("fox", "wolf", "tiger"))
                .addMaybe(Generators.chooseOneOfValues("quickly", "reluctantly", "smugly")
                        .maybe(justs(3).toNothings(2)))
                .add("jumped over the")
                .add(Generators.chooseOneOfValues("lazy", "sleeping"))
                .addMaybe(Generators.constant("no good").maybe(nothings(4).toJusts(1)))
                .add(Generators.chooseOneOfValues("dogs", "horses", "people"))
                .withSeparator(" ")
                .withEndDelimiter(Generators.chooseOneOfValues(".", "!"))
                .build();

        sentence
                .run()
                .take(50)
                .forEach(System.out::println);
    }
}
