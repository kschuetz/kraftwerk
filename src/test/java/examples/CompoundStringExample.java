package examples;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;

import static dev.marksman.kraftwerk.GeneratedStream.streamFrom;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static dev.marksman.kraftwerk.weights.MaybeWeights.justWeight;
import static dev.marksman.kraftwerk.weights.MaybeWeights.nothingWeight;

public class CompoundStringExample {

    public static void main(String[] args) {
        Generator<String> sentence = Generators.compoundStringBuilder()
                .add(Generators.chooseOneOfValues("The", "A"))
                .add(frequencyMap(3, Generators.chooseOneOfValues("quick", "fast", "speedy"))
                        .add(1, Generators.chooseOneOfValues("slow", "reluctant", "sleepy"))
                        .toGenerator())
                .add(frequencyMap(5, "brown")
                        .addValue(4, "red")
                        .addValue(3, "orange")
                        .addValue(2, "white")
                        .addValue(1, "purple").toGenerator())
                .add(Generators.chooseOneOfValues("fox", "wolf", "tiger"))
                .addMaybe(Generators.chooseOneOfValues("quickly", "reluctantly", "smugly")
                        .maybe(justWeight(3).toNothing(2)))
                .add("jumped over the")
                .add(Generators.chooseOneOfValues("lazy", "sleeping"))
                .addMaybe(Generators.constant("no good").maybe(nothingWeight(4)))
                .add(Generators.chooseOneOfValues("dogs", "horses", "people"))
                .withSeparator(" ")
                .withEndDelimiter(Generators.chooseOneOfValues(".", "!"))
                .build();

        streamFrom(sentence).next(50).forEach(System.out::println);
    }
}
