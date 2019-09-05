package examples;

import dev.marksman.composablerandom.Generator;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.Generator.*;
import static dev.marksman.composablerandom.MaybeWeights.justWeight;
import static dev.marksman.composablerandom.MaybeWeights.nothingWeight;
import static dev.marksman.composablerandom.frequency.FrequencyMap.frequencyMap;

public class CompoundStringExample {

    public static void main(String[] args) {
        Generator<String> sentence = compoundStringBuilder()
                .add(chooseOneOfValues("The", "A"))
                .add(frequencyMap(3, chooseOneOfValues("quick", "fast", "speedy"))
                        .add(1, chooseOneOfValues("slow", "reluctant", "sleepy"))
                        .toGenerator())
                .add(frequencyMap(5, "brown")
                        .addValue(4, "red")
                        .addValue(3, "orange")
                        .addValue(2, "white")
                        .addValue(1, "purple").toGenerator())
                .add(chooseOneOfValues("fox", "wolf", "tiger"))
                .addMaybe(chooseOneOfValues("quickly", "reluctantly", "smugly")
                        .maybe(justWeight(3).toNothing(2)))
                .add("jumped over the")
                .add(chooseOneOfValues("lazy", "sleeping"))
                .addMaybe(constant("no good").maybe(nothingWeight(4)))
                .add(chooseOneOfValues("dogs", "horses", "people"))
                .withSeparator(" ")
                .withEndDelimiter(chooseOneOfValues(".", "!"))
                .build();

        streamFrom(sentence).next(50).forEach(System.out::println);
    }
}
