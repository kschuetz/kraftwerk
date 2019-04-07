package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import dev.marksman.composablerandom.frequency.FrequencyMapBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.domain.Choices.choices;
import static dev.marksman.composablerandom.frequency.FrequencyMap.frequencyMap;
import static dev.marksman.composablerandom.frequency.FrequencyMapBuilder.frequencyMapBuilder;
import static java.util.Arrays.asList;

class Choose {

    @SafeVarargs
    static <A> Generator<A> chooseOneOfValues(A first, A... more) {
        ArrayList<A> choices = new ArrayList<>();
        choices.add(first);
        choices.addAll(asList(more));
        return chooseOneFromCollection(choices);
    }

    @SafeVarargs
    static <A> Generator<A> chooseOneOf(Generator<A> first, Generator<? extends A>... more) {
        return foldLeft(FrequencyMap::add, frequencyMap(first), asList(more)).toGenerator();
    }

    @SafeVarargs
    static <A> Generator<ArrayList<A>> chooseAtLeastOneOfValues(A first, A... more) {
        return chooseAtLeastOneFromDomain(choices(cons(first, asList(more))));
    }

    @SafeVarargs
    static <A> Generator<ArrayList<A>> chooseAtLeastOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return null;
    }

    @SafeVarargs
    static <A> Generator<ArrayList<A>> chooseSomeOf(A first, A... more) {
        return chooseSomeFromDomain(choices(cons(first, asList(more))));
    }

    @SafeVarargs
    static <A> Generator<ArrayList<A>> chooseSomeOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return null;
    }

    static <A> Generator<A> chooseOneFromCollection(Collection<A> items) {
        requireNonEmptyChoices("chooseOneFrom", items);
        return chooseOneFromDomain(choices(items));
    }

    static <A> Generator<A> chooseOneFromDomain(DiscreteDomain<A> domain) {
        long size = domain.getSize();
        if (size == 1) {
            return constant(domain.getValue(0));
        } else {
            return Generator.generateLongIndex(size).fmap(domain::getValue);
        }
    }

    static <A> Generator<ArrayList<A>> chooseAtLeastOneFromCollection(Collection<A> items) {
        requireNonEmptyChoices("chooseAtLeastOneFrom", items);
        return chooseAtLeastOneFromDomain(choices(items));
    }

    static <A> Generator<ArrayList<A>> chooseAtLeastOneFromDomain(DiscreteDomain<A> domain) {
        return null;
    }

    static <A> Generator<ArrayList<A>> chooseSomeFromDomain(Collection<A> items) {
        requireNonEmptyChoices("chooseSomeFrom", items);
        return chooseSomeFromDomain(choices(items));
    }

    static <A> Generator<ArrayList<A>> chooseSomeFromDomain(DiscreteDomain<A> domain) {
        return null;
    }

    static <K, V> Generator<Map.Entry<K, V>> chooseEntryFromMap(Map<K, V> map) {
        Set<Map.Entry<K, V>> entries = map.entrySet();
        requireNonEmptyChoices("chooseEntryFrom", entries);
        return chooseOneFromCollection(entries);
    }

    static <K, V> Generator<K> chooseKeyFromMap(Map<K, V> map) {
        Set<K> keys = map.keySet();
        requireNonEmptyChoices("chooseKeyFrom", keys);
        return chooseOneFromCollection(keys);
    }

    static <K, V> Generator<V> chooseValueFromMap(Map<K, V> map) {
        Collection<V> values = map.values();
        requireNonEmptyChoices("chooseValueFrom", values);
        return chooseOneFromCollection(values);
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
