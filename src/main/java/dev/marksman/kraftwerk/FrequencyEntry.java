package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.product.Product2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.Generator.constant;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FrequencyEntry<A> implements Product2<Integer, Generator<A>> {
    private final int weight;
    private final Generator<A> generate;

    @Override
    public Integer _1() {
        return weight;
    }

    @Override
    public Generator<A> _2() {
        return generate;
    }

    public static <A> FrequencyEntry<A> entry(int weight, Generator<A> gen) {
        return new FrequencyEntry<>(weight, gen);
    }

    public static <A> FrequencyEntry<A> entry(Generator<A> gen) {
        return entry(1, gen);
    }

    public static <A> FrequencyEntry<A> entryForValue(int weight, A value) {
        return new FrequencyEntry<>(weight, constant(value));
    }

    public static <A> FrequencyEntry<A> entryForValue(A value) {
        return new FrequencyEntry<>(1, constant(value));
    }

    public static <A> FrequencyEntry<A> fromProduct(Product2<Integer, Generator<? extends A>> entry) {
        //noinspection unchecked
        return new FrequencyEntry(entry._1(), entry._2());
    }

}
