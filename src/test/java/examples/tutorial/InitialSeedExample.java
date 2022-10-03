package examples.tutorial;

import software.kes.kraftwerk.Seed;
import software.kes.kraftwerk.constraints.IntRange;

import static software.kes.kraftwerk.Generators.generateInt;

public class InitialSeedExample {
    public static void main(String[] args) {
        Seed initialSeed = Seed.create(123456L);
        generateInt(IntRange.from(1).to(100))
                .run(initialSeed)
                .take(5)
                .forEach(System.out::println);

        // output:
        // 24
        // 48
        // 68
        // 86
        // 39
        // These will be the same on every run because we are using the same initial seed.
    }
}
