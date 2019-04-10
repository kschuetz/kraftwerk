package dev.marksman.discretedomain;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class MemberList<A> extends SmallDomain<A> {
    private final ArrayList<A> members;

    @Override
    public long getSize() {
        return members.size();
    }

    @Override
    public A getValue(long index) {
        return members.get((short) index);
    }

    static <A> Maybe<SmallDomain<A>> union(DiscreteDomain<A> first, DiscreteDomain<A> second) {
        long firstSize = first.getSize();
        long secondSize = second.getSize();
        if (firstSize >= MAX_SMALL_DOMAIN_SIZE || secondSize >= MAX_SMALL_DOMAIN_SIZE) {
            return nothing();
        }

        int sizeHint = (int) Math.max(firstSize, secondSize);
        ArrayList<A> as = new ArrayList<>(sizeHint);
        HashSet<A> seen = new HashSet<>(sizeHint);
        int size = 0;
        for (A elem : first) {
            if (!seen.contains(elem)) {
                seen.add(elem);
                as.add(elem);
                size += 1;
            }
        }
        for (A elem : second) {
            if (!seen.contains(elem)) {
                if (size >= MAX_SMALL_DOMAIN_SIZE) {
                    return nothing();
                }

                seen.add(elem);
                as.add(elem);
                size += 1;
            }
        }
        return just(new MemberList<>(as));
    }

    static <A> Maybe<SmallDomain<A>> intersection(SmallDomain<A> first, SmallDomain<A> second) {
        SmallDomain<A> larger;
        SmallDomain<A> smaller;
        if (first.getSize() > second.getSize()) {
            larger = first;
            smaller = second;
        } else {
            larger = second;
            smaller = first;
        }

        HashSet<A> seen1 = new HashSet<>((int) larger.getSize());
        for (A elem : larger) {
            seen1.add(elem);
        }
        ArrayList<A> as = new ArrayList<>((int) smaller.getSize());
        HashSet<A> seen2 = new HashSet<>((int) larger.getSize());
        for (A elem : smaller) {
            if (seen1.contains(elem) && !seen2.contains(elem)) {
                seen2.add(elem);
                as.add(elem);
            }
        }
        if (as.size() == 0) {
            return nothing();
        } else {
            return just(new MemberList<>(as));
        }
    }
}
