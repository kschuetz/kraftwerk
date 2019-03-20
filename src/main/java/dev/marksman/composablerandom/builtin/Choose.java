package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.functions.builtin.fn2.Filter;
import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;

import java.util.ArrayList;
import java.util.TreeMap;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.domain.Choices.choices;
import static java.util.Arrays.asList;

public class Choose {

    @SafeVarargs
    public static <A> Generator<A> oneOf(A first, A... more) {
        ArrayList<A> choices = new ArrayList<>();
        choices.add(first);
        choices.addAll(asList(more));
        return chooseFrom(choices);
    }

    public static <A> Generator<A> chooseFrom(Iterable<A> items) {
        if (!items.iterator().hasNext()) {
            throw new IllegalArgumentException("chooseFrom requires at least one choice");
        }
        return fromDomain(choices(items));
    }

    public static <A> Generator<A> fromDomain(DiscreteDomain<A> discreteDomain) {
        long size = discreteDomain.getSize();
        if (size == 1) {
            return constant(discreteDomain.getValue(1));
        } else {
            return Primitives.generateLongExclusive(size).fmap(discreteDomain::getValue);
        }
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <A> Generator<A> frequency(FrequencyEntry<? extends A> first, FrequencyEntry<? extends A>... more) {
        Iterable<FrequencyEntry<? extends A>> fs = Filter.filter(f -> f.getWeight() > 0, cons(first, asList(more)));
        if (!fs.iterator().hasNext()) {
            throw new IllegalArgumentException("no items with positive weights");
        }
        long total = 0L;
        TreeMap<Long, Generator<? extends A>> tree = new TreeMap<>();
        for (FrequencyEntry<? extends A> f : fs) {
            total += f.getWeight();
            tree.put(total, f.getGenerator());
        }

        return (Generator<A>) Primitives.generateLongExclusive(total)
                .flatMap(n -> tree.ceilingEntry(1 + n).getValue());
    }

}
