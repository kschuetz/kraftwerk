package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.builtin.Generators;

import java.util.TreeMap;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static dev.marksman.composablerandom.FrequencyEntry.entry;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.checkMultiplier;

class FrequencyMapN<A> implements FrequencyMap<A> {
    private final Iterable<FrequencyEntry<A>> entries;

    private Generator<A> cachedGenerator;

    private FrequencyMapN(Iterable<FrequencyEntry<A>> entries) {
        this.entries = entries;
    }

    @Override
    public Generator<A> toGenerator() {
        synchronized (this) {
            if (cachedGenerator == null) cachedGenerator = buildGenerator();
        }
        return cachedGenerator;
    }

    private Generator<A> buildGenerator() {
        long total = 0L;
        TreeMap<Long, Generator<A>> tree = new TreeMap<>();
        for (FrequencyEntry<A> entry : entries) {
            System.out.println("entry = " + entry);
            total += entry.getWeight();
            tree.put(total, entry.getGenerator());
        }

        return Generators.generateLongExclusive(total)
                .flatMap(n -> tree.ceilingEntry(1 + n).getValue());
    }

    @Override
    public FrequencyMap<A> add(int weight, Generator<? extends A> generator) {
        if (weight < 1) return this;
        else {
            @SuppressWarnings("unchecked")
            Iterable<FrequencyEntry<A>> newEntries = cons(entry(weight, (Generator<A>) generator), entries);
            return new FrequencyMapN<>(newEntries);
        }
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return foldLeft((acc, entry) -> acc.add(entry._1(), entry._2()), other, entries);
    }

    @Override
    public <B> FrequencyMap<B> fmap(Function<? super A, ? extends B> fn) {
        return new FrequencyMapN<>(Map.map(t -> entry(t._1(), t._2().fmap(fn)), entries));
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        checkMultiplier(positiveFactor);
        if (positiveFactor == 1) return this;
        else return new FrequencyMapN<>(Map.map(t -> entry(positiveFactor * t._1(), t._2()), entries));
    }

    static <A> FrequencyMapN<A> frequencyMapN(FrequencyEntry<A> first, Iterable<FrequencyEntry<A>> rest) {
        return new FrequencyMapN<>(cons(first, rest));
    }
}
