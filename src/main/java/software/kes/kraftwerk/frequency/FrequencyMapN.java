package software.kes.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Weighted;

import java.util.TreeMap;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static software.kes.kraftwerk.Generators.generateLongIndex;

final class FrequencyMapN<A> implements FrequencyMap<A> {
    private final Iterable<Weighted<Generator<A>>> entries;
    private Generator<A> cachedGenerator;

    @SuppressWarnings("unchecked")
    private FrequencyMapN(Iterable<? extends Weighted<? extends Generator<? extends A>>> entries) {
        this.entries = (Iterable<Weighted<Generator<A>>>) entries;
    }

    static <A> FrequencyMapN<A> frequencyMapN(Weighted<? extends Generator<? extends A>> first,
                                              Iterable<Weighted<? extends Generator<? extends A>>> rest) {
        return new FrequencyMapN<>(cons(first, rest));
    }

    static <A> Generator<A> addLabel(Generator<A> gen) {
        return gen.labeled("frequency map");
    }

    @Override
    public boolean isEmpty() {
        return false;
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
        for (Weighted<Generator<A>> entry : entries) {
            total += entry.getWeight();
            tree.put(total, entry.getValue());
        }

        return addLabel(generateLongIndex(total)
                .flatMap(n -> tree.ceilingEntry(1 + n).getValue()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public FrequencyMap<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        if (weightedGenerator.getWeight() < 1) {
            return this;
        } else {
            return new FrequencyMapN<>(cons((Weighted<Generator<A>>) weightedGenerator, entries));
        }
    }

    @Override
    public FrequencyMap<A> combine(FrequencyMap<A> other) {
        return foldLeft(FrequencyMap::add, other, entries);
    }

    @Override
    public <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn) {
        Iterable<Weighted<Generator<B>>> mapped = Map.map(entry -> entry.fmap(gen -> gen.fmap(fn)), entries);
        return new FrequencyMapN<>(mapped);
    }

    @Override
    public FrequencyMap<A> multiply(int positiveFactor) {
        if (positiveFactor == 1) {
            return this;
        } else {
            return new FrequencyMapN<>(Map.map(entry -> entry.multiplyBy(positiveFactor), entries));
        }
    }
}
