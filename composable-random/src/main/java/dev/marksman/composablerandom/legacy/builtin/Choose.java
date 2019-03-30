package dev.marksman.composablerandom.legacy.builtin;

import com.jnape.palatable.lambda.functions.builtin.fn2.Filter;
import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.OldGenerator;
import dev.marksman.composablerandom.legacy.OldFrequencyEntry;

import java.util.*;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static dev.marksman.composablerandom.OldGenerator.constant;
import static dev.marksman.composablerandom.domain.Choices.choices;
import static java.util.Arrays.asList;

class Choose {

    @SafeVarargs
    static <A> OldGenerator<A> chooseOneOf(A first, A... more) {
        ArrayList<A> choices = new ArrayList<>();
        choices.add(first);
        choices.addAll(asList(more));
        return chooseOneFrom(choices);
    }

    @SafeVarargs
    static <A> OldGenerator<A> chooseOneOf(OldGenerator<? extends A> first, OldGenerator<? extends A>... more) {
        return null;
    }

    @SafeVarargs
    static <A> OldGenerator<ArrayList<A>> chooseAtLeastOneOf(A first, A... more) {
        return chooseAtLeastOneFrom(choices(cons(first, asList(more))));
    }

    @SafeVarargs
    static <A> OldGenerator<ArrayList<A>> chooseAtLeastOneOf(OldGenerator<? extends A> first, OldGenerator<? extends A>... more) {
        return null;
    }

    @SafeVarargs
    static <A> OldGenerator<ArrayList<A>> chooseSomeOf(A first, A... more) {
        return chooseSomeFrom(choices(cons(first, asList(more))));
    }

    @SafeVarargs
    static <A> OldGenerator<ArrayList<A>> chooseSomeOf(OldGenerator<? extends A> first, OldGenerator<? extends A>... more) {
        return null;
    }

    static <A> OldGenerator<A> chooseOneFrom(Collection<A> items) {
        requireNonEmptyChoices("chooseOneFrom", items);
        return chooseOneFrom(choices(items));
    }

    static <A> OldGenerator<A> chooseOneFrom(DiscreteDomain<A> domain) {
        long size = domain.getSize();
        if (size == 1) {
            return constant(domain.getValue(0));
        } else {
            return Primitives.generateLongExclusive(size).fmap(domain::getValue).withLabel("chooseOneFrom");
        }
    }

    static <A> OldGenerator<ArrayList<A>> chooseAtLeastOneFrom(Collection<A> items) {
        requireNonEmptyChoices("chooseAtLeastOneFrom", items);
        return chooseAtLeastOneFrom(choices(items));
    }

    static <A> OldGenerator<ArrayList<A>> chooseAtLeastOneFrom(DiscreteDomain<A> domain) {
        return null;
    }

    static <A> OldGenerator<ArrayList<A>> chooseSomeFrom(Collection<A> items) {
        requireNonEmptyChoices("chooseSomeFrom", items);
        return chooseSomeFrom(choices(items));
    }

    static <A> OldGenerator<ArrayList<A>> chooseSomeFrom(DiscreteDomain<A> domain) {
        return null;
    }

    static <K, V> OldGenerator<Map.Entry<K, V>> chooseEntryFrom(Map<K, V> map) {
        Set<Map.Entry<K, V>> entries = map.entrySet();
        requireNonEmptyChoices("chooseEntryFrom", entries);
        return chooseOneFrom(entries);
    }

    static <K, V> OldGenerator<K> chooseKeyFrom(Map<K, V> map) {
        Set<K> keys = map.keySet();
        requireNonEmptyChoices("chooseKeyFrom", keys);
        return chooseOneFrom(keys);
    }

    static <K, V> OldGenerator<V> chooseValueFrom(Map<K, V> map) {
        Collection<V> values = map.values();
        requireNonEmptyChoices("chooseValueFrom", values);
        return chooseOneFrom(values);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    static <A> OldGenerator<A> frequency(OldFrequencyEntry<? extends A> first, OldFrequencyEntry<? extends A>... more) {
        return frequencyImpl(cons(first, asList(more)));
    }

    static <A> OldGenerator<A> frequency(Collection<OldFrequencyEntry<? extends A>> entries) {
        return frequencyImpl(entries);
    }

    @SuppressWarnings("unchecked")
    private static <A> OldGenerator<A> frequencyImpl(Iterable<OldFrequencyEntry<? extends A>> entries) {
        Iterable<OldFrequencyEntry<? extends A>> fs = Filter.filter(f -> f.getWeight() > 0, entries);
        if (!fs.iterator().hasNext()) {
            throw new IllegalArgumentException("no items with positive weights");
        }
        long total = 0L;
        TreeMap<Long, OldGenerator<? extends A>> tree = new TreeMap<>();
        for (OldFrequencyEntry<? extends A> f : fs) {
            total += f.getWeight();
            tree.put(total, f.getGenerator());
        }

        return (OldGenerator<A>) Primitives.generateLongExclusive(total)
                .flatMap(n -> tree.ceilingEntry(1 + n).getValue());
    }

    private static <A> void requireNonEmptyChoices(String methodName, Iterable<A> items) {
        if (!items.iterator().hasNext()) {
            throw new IllegalArgumentException(methodName + " requires at least one choice");
        }
    }

}
