package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import dev.marksman.composablerandom.OldGenerator;
import dev.marksman.composablerandom.legacy.builtin.OldGenerators;

import java.util.TreeMap;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.checkMultiplier;

class FrequencyMapN<A> implements FrequencyMap<A> {
    private final Iterable<Tuple2<Integer, OldGenerator<A>>> entries;

    private OldGenerator<A> cachedGenerator;

    private FrequencyMapN(Iterable<Tuple2<Integer, OldGenerator<A>>> entries) {
        this.entries = entries;
    }

    @Override
    public OldGenerator<A> generator() {
        synchronized (this) {
            if (cachedGenerator == null) cachedGenerator = buildGenerator();
        }
        return cachedGenerator;
    }

    private OldGenerator<A> buildGenerator() {
        long total = 0L;
        TreeMap<Long, OldGenerator<A>> tree = new TreeMap<>();
        for (Tuple2<Integer, OldGenerator<A>> entry : entries) {
            total += entry._1();
            tree.put(total, entry._2());
        }

        return OldGenerators.generateLongExclusive(total)
                .flatMap(n -> tree.ceilingEntry(1 + n).getValue());
    }

    @Override
    public FrequencyMap<A> add(int weight, OldGenerator<? extends A> generator) {
        if (weight < 1) return this;
        else {
            @SuppressWarnings("unchecked")
            Iterable<Tuple2<Integer, OldGenerator<A>>> newEntries = cons(tuple(weight, (OldGenerator<A>) generator), entries);
            return new FrequencyMapN<>(newEntries);
        }
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return foldLeft((acc, entry) -> acc.add(entry._1(), entry._2()), other, entries);
    }

    @Override
    public <B> FrequencyMap<B> fmap(Function<? super A, ? extends B> fn) {
        return new FrequencyMapN<>(Map.map(t -> tuple(t._1(), t._2().fmap(fn)), entries));
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        checkMultiplier(positiveFactor);
        if (positiveFactor == 1) return this;
        else return new FrequencyMapN<>(Map.map(t -> tuple(positiveFactor * t._1(), t._2()), entries));
    }

    static <A> FrequencyMapN<A> frequencyMapN(Tuple2<Integer, OldGenerator<A>> first, Iterable<Tuple2<Integer, OldGenerator<A>>> rest) {
        return new FrequencyMapN<>(cons(first, rest));
    }
}
