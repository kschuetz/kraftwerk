package dev.marksman.composablerandom.domain;

import dev.marksman.composablerandom.Domain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Choices<A> implements Domain<A> {
    private final ArrayList<A> items;

    @Override
    public long getSize() {
        return items.size();
    }

    @Override
    public A getValue(long index) {
        return items.get((int) index);
    }

    public static <A> Choices<A> choices(Iterable<A> items) {
        ArrayList<A> as = new ArrayList<>();
        items.forEach(as::add);
        if(as.size() < 1) {
            throw new IllegalArgumentException("Choices must contain at least one value");
        }
        return new Choices<>(as);
    }
}
