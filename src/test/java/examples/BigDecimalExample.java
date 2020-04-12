package examples;

import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.constraints.BigDecimalRange;

import java.math.BigDecimal;

import static dev.marksman.kraftwerk.Generators.generateBigDecimal;

public class BigDecimalExample {

    public static void main(String[] args) {
        Generator<Tuple5<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> examples =
                Generators.tupled(generateBigDecimal(5, BigDecimalRange.exclusive(BigDecimal.valueOf(100_000_000))),
                        generateBigDecimal(0, BigDecimalRange.exclusive(BigDecimal.valueOf(100))),
                        generateBigDecimal(-3, BigDecimalRange.exclusive(BigDecimal.valueOf(100_000_000))),
                        generateBigDecimal(5, BigDecimalRange.exclusive(BigDecimal.valueOf(123.456), BigDecimal.valueOf(789.0123))),
                        generateBigDecimal(5, BigDecimalRange.exclusive(BigDecimal.valueOf(-1000), BigDecimal.valueOf(1000))));

        examples
                .run()
                .take(50)
                .forEach(System.out::println);
    }

}
