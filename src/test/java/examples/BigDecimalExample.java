package examples;

import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;

import java.math.BigDecimal;

import static dev.marksman.kraftwerk.Generators.generateBigDecimal;
import static dev.marksman.kraftwerk.Generators.generateBigDecimalExclusive;
import static dev.marksman.kraftwerk.ValueSupplyIterator.streamFrom;

public class BigDecimalExample {

    public static void main(String[] args) {
        Generator<Tuple5<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> examples =
                Generators.tupled(generateBigDecimalExclusive(5, BigDecimal.valueOf(100_000_000)),
                        generateBigDecimalExclusive(0, BigDecimal.valueOf(100)),
                        generateBigDecimalExclusive(-3, BigDecimal.valueOf(100_000_000)),
                        generateBigDecimalExclusive(5, BigDecimal.valueOf(123.456), BigDecimal.valueOf(789.0123)),
                        generateBigDecimal(5, BigDecimal.valueOf(-1000), BigDecimal.valueOf(1000)));

        streamFrom(examples).next(50).forEach(System.out::println);
    }

}
