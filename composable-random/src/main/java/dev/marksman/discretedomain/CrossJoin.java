package dev.marksman.discretedomain;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

class CrossJoin<A, B> implements DiscreteDomain<Tuple2<A, B>> {
    private final DiscreteDomain<A> first;
    private final DiscreteDomain<B> second;
    private final long size;
    private final long firstSize;

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public Tuple2<A, B> getValue(long index) {
        return tuple(first.getValue(index / firstSize),
                second.getValue(index % firstSize));
    }

    private CrossJoin(DiscreteDomain<A> first, DiscreteDomain<B> second) {
        this.first = first;
        this.second = second;
        // TODO: check size
        this.size = first.getSize() * second.getSize();
        this.firstSize = first.getSize();
    }

    static <A, B> CrossJoin<A, B> crossJoin(DiscreteDomain<A> first, DiscreteDomain<B> second) {
        return new CrossJoin<>(first, second);
    }
}
