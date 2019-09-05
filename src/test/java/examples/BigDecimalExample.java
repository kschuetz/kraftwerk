package examples;

import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.composablerandom.Generator;

import java.math.BigDecimal;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.Generator.generateBigDecimal;
import static dev.marksman.composablerandom.Generator.generateBigDecimalExclusive;

public class BigDecimalExample {

    public static void main(String[] args) {
        Generator<Tuple5<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> examples =
                Generator.tupled(generateBigDecimalExclusive(5, BigDecimal.valueOf(100_000_000)),
                        generateBigDecimalExclusive(0, BigDecimal.valueOf(100)),
                        generateBigDecimalExclusive(-3, BigDecimal.valueOf(100_000_000)),
                        generateBigDecimalExclusive(5, BigDecimal.valueOf(123.456), BigDecimal.valueOf(789.0123)),
                        generateBigDecimal(5, BigDecimal.valueOf(-1000), BigDecimal.valueOf(1000)));

        streamFrom(examples).next(50).forEach(System.out::println);
    }

}
