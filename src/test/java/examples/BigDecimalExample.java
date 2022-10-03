package examples;

import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.constraints.BigDecimalRange;

import java.math.BigDecimal;

import static software.kes.kraftwerk.Generators.generateBigDecimal;
import static software.kes.kraftwerk.Generators.generateTuple;

public class BigDecimalExample {
    public static void main(String[] args) {
        Generator<Tuple5<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> examples =
                generateTuple(generateBigDecimal(5, BigDecimalRange.exclusive(BigDecimal.valueOf(100_000_000))),
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
