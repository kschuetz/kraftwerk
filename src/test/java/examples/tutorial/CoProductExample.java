package examples.tutorial;

import dev.marksman.kraftwerk.Generators;

import java.time.Year;

import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.generateEither;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateLocalDateForYear;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateMaybe;
import static dev.marksman.kraftwerk.Generators.generateThese;
import static dev.marksman.kraftwerk.Generators.generateTuple;

public class CoProductExample {
    public static void main(String[] args) {
        generateTuple(generateMaybe(generateLocalDateForYear(Year.now())),
                generateEither(generateInt(), Generators.generateDoubleFractional()),
                generateThese(generateLong(), chooseOneOfValues("foo", "bar", "baz")))
                .run()
                .take(10)
                .forEach(System.out::println);

        //sample output:
        //HList{ Just 2020-07-10 :: Left{l=1290020209} :: These{b=baz} }
        //HList{ Just 2020-04-09 :: Right{r=0.480723935139732} :: These{a=3160121273074965838} }
        //HList{ Just 2020-03-18 :: Left{l=891956632} :: These{b=bar} }
        //HList{ Just 2020-09-24 :: Right{r=0.10862715298091574} :: These{both=HList{ -7826076449853900017 :: bar }} }
        //HList{ Just 2020-02-07 :: Right{r=0.5608597418874514} :: These{a=-860230134582980058} }
        //HList{ Just 2020-02-17 :: Left{l=2112321697} :: These{b=bar} }
        //HList{ Just 2020-09-07 :: Right{r=0.709292621079071} :: These{b=baz} }
        //HList{ Just 2020-01-16 :: Left{l=1919118581} :: These{both=HList{ -2545935683596921432 :: baz }} }
        //HList{ Just 2020-03-22 :: Left{l=-1905433010} :: These{b=bar} }
        //HList{ Just 2020-10-02 :: Right{r=0.6120825155638645} :: These{b=baz} }
    }
}
