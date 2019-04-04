package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import dev.marksman.composablerandom.frequency.FrequencyMapBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.domain.Choices.choices;
import static dev.marksman.composablerandom.frequency.FrequencyMapBuilder.frequencyMapBuilder;
import static java.util.Arrays.asList;

public class Choose {

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
    static <A> Generator<ArrayList<A>> chooseAtLeastOneOf(A first, A... more) {
        return chooseAtLeastOneFrom(choices(cons(first, asList(more))));
    }

    @SafeVarargs
    static <A> Generator<ArrayList<A>> chooseAtLeastOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return null;
    }

    @SafeVarargs
    static <A> Generator<ArrayList<A>> chooseSomeOf(A first, A... more) {
        return chooseSomeFrom(choices(cons(first, asList(more))));
    }

    @SafeVarargs
    static <A> Generator<ArrayList<A>> chooseSomeOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return null;
    }

    static <A> Generator<A> chooseOneFrom(Collection<A> items) {
        requireNonEmptyChoices("chooseOneFrom", items);
        return chooseOneFrom(choices(items));
    }

    static <A> Generator<A> chooseOneFrom(DiscreteDomain<A> domain) {
        long size = domain.getSize();
        if (size == 1) {
            return constant(domain.getValue(0));
        } else {
            return Primitives.generateLongIndex(size).fmap(domain::getValue);
        }
    }

    static <A> Generator<ArrayList<A>> chooseAtLeastOneFrom(Collection<A> items) {
        requireNonEmptyChoices("chooseAtLeastOneFrom", items);
        return chooseAtLeastOneFrom(choices(items));
    }

    static <A> Generator<ArrayList<A>> chooseAtLeastOneFrom(DiscreteDomain<A> domain) {
        return null;
    }

    static <A> Generator<ArrayList<A>> chooseSomeFrom(Collection<A> items) {
        requireNonEmptyChoices("chooseSomeFrom", items);
        return chooseSomeFrom(choices(items));
    }

    static <A> Generator<ArrayList<A>> chooseSomeFrom(DiscreteDomain<A> domain) {
        return null;
    }

    static <K, V> Generator<Map.Entry<K, V>> chooseEntryFrom(Map<K, V> map) {
        Set<Map.Entry<K, V>> entries = map.entrySet();
        requireNonEmptyChoices("chooseEntryFrom", entries);
        return chooseOneFrom(entries);
    }

    static <K, V> Generator<K> chooseKeyFrom(Map<K, V> map) {
        Set<K> keys = map.keySet();
        requireNonEmptyChoices("chooseKeyFrom", keys);
        return chooseOneFrom(keys);
    }

    static <K, V> Generator<V> chooseValueFrom(Map<K, V> map) {
        Collection<V> values = map.values();
        requireNonEmptyChoices("chooseValueFrom", values);
        return chooseOneFrom(values);
    }

    static <A> Generator<A> frequency(FrequencyMap<A> frequencyMap) {
        return frequencyMap.toGenerator();
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    static <A> Generator<A> frequency(FrequencyEntry<A> first, FrequencyEntry<A>... more) {
        return frequencyImpl(cons(first, asList(more)));
    }

    static <A> Generator<A> frequency(Collection<FrequencyEntry<A>> entries) {
        return frequencyImpl(entries);
    }

    private static <A> Generator<A> frequencyImpl(Iterable<FrequencyEntry<A>> entries) {
        return FoldLeft.<FrequencyEntry<A>, FrequencyMapBuilder<A>>foldLeft(
                FrequencyMapBuilder::add,
                frequencyMapBuilder(), entries)
                .build()
                .toGenerator();
    }

    private static <A> void requireNonEmptyChoices(String methodName, Iterable<A> items) {
        if (!items.iterator().hasNext()) {
            throw new IllegalArgumentException(methodName + " requires at least one choice");
        }
    }

}
