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

class Choose {

    @SafeVarargs
    static <A> Generator<A> chooseOneOf(A first, A... more) {
        ArrayList<A> choices = new ArrayList<>();
        choices.add(first);
        choices.addAll(asList(more));
        return chooseOneFrom(choices);
    }

    @SafeVarargs
    static <A> Generator<A> chooseOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return null;
    }

    @SafeVarargs
    static <A> Generator<ArrayList<A>> chooseAtLeastOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return null;
    }

    @SafeVarargs
    static <A> Generator<ArrayList<A>> chooseSomeOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return null;
    }

    static <A> Generator<A> chooseOneFrom(Iterable<A> items) {
        if (!items.iterator().hasNext()) {
            throw new IllegalArgumentException("chooseOneFrom requires at least one choice");
        }
        return fromDomain(choices(items));
    }

    static <A> Generator<ArrayList<A>> chooseAtLeastOneFrom(Iterable<A> items) {
        return null;
    }

    static <A> Generator<ArrayList<A>> chooseSomeFrom(Iterable<A> items) {
        return null;
    }

    static <A> Generator<A> fromDomain(DiscreteDomain<A> domain) {
        long size = domain.getSize();
        if (size == 1) {
            return constant(domain.getValue(1));
        } else {
            return Primitives.generateLongExclusive(size).fmap(domain::getValue);
        }
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    static <A> Generator<A> frequency(FrequencyEntry<? extends A> first, FrequencyEntry<? extends A>... more) {
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
