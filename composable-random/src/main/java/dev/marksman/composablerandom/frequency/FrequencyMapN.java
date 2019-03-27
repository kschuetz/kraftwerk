package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.builtin.Generators;

import java.util.TreeMap;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;

class FrequencyMapN<A> implements FrequencyMap<A> {
    private final Iterable<Tuple2<Integer, Generator<A>>> entries;

    private Generator<A> cachedGenerator;

    private FrequencyMapN(Iterable<Tuple2<Integer, Generator<A>>> entries) {
        this.entries = entries;
    }

    @Override
    public Generator<A> generator() {
        synchronized (this) {
            if (cachedGenerator == null) cachedGenerator = buildGenerator();
        }
        return cachedGenerator;
    }

    private Generator<A> buildGenerator() {
        long total = 0L;
        TreeMap<Long, Generator<A>> tree = new TreeMap<>();
        for (Tuple2<Integer, Generator<A>> entry : entries) {
            total += entry._1();
            tree.put(total, entry._2());
        }

        return Generators.generateLongExclusive(total)
                .flatMap(n -> tree.ceilingEntry(1 + n).getValue());
    }

    @Override
    public FrequencyMap<A> add(int weight, Generator<? extends A> generator) {
        if (weight < 1) return this;
        else {
            @SuppressWarnings("unchecked")
            Iterable<Tuple2<Integer, Generator<A>>> newEntries = cons(tuple(weight, (Generator<A>) generator), entries);
            return new FrequencyMapN<>(newEntries);
        }
    }

    static <A> FrequencyMapN<A> frequencyMapN(Tuple2<Integer, Generator<A>> first, Iterable<Tuple2<Integer, Generator<A>>> rest) {
        return new FrequencyMapN<>(cons(first, rest));
    }
}
