package examples.tutorial;

import dev.marksman.kraftwerk.constraints.IntRange;

import java.util.Collections;

import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class ContrivedFlatMapExample {
    public static void main(String[] args) {
        generateInt(IntRange.from(3).to(10))
                .flatMap(size ->
                        chooseOneOfValues("$", "£", "€", "¥")
                                .fmap(ch -> String.join("", Collections.nCopies(size, ch))))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //¥¥¥¥¥¥¥¥¥
        //£££
        //$$$$$$
        //££££££
        //££££££
        //€€€€€€€€
        //$$$$$$$$
        //¥¥¥¥¥¥¥¥¥
        //££££££££££
        //$$$$
    }
}
