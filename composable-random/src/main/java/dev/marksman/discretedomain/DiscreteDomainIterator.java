package dev.marksman.discretedomain;

import com.jnape.palatable.lambda.internal.iteration.ImmutableIterator;

class DiscreteDomainIterator<A> extends ImmutableIterator<A> {
    private final DiscreteDomain<A> domain;
    private final long size;
    private long index;

    DiscreteDomainIterator(DiscreteDomain<A> domain) {
        this.domain = domain;
        this.size = domain.getSize();
        this.index = 0L;
    }

    @Override
    public boolean hasNext() {
        return index < size;
    }

    @Override
    public A next() {
        A result = domain.getValue(index);
        index += 1;
        return result;
    }
}
