package dev.marksman.composablerandom.domain;

import dev.marksman.composablerandom.DiscreteDomain;

import static dev.marksman.composablerandom.DiscreteDomain.discreteDomain;

public class Numbers {

    public static DiscreteDomain<Integer> ints() {
        return discreteDomain(Math.abs((long) Integer.MIN_VALUE) + 1 + Integer.MAX_VALUE,
                n -> (int) (Integer.MIN_VALUE + n));
    }

    public static DiscreteDomain<Integer> positiveInts() {
        return discreteDomain(Integer.MAX_VALUE - 1, n -> n.intValue() + 1);
    }

    public static DiscreteDomain<Integer> negativeInts() {
        return discreteDomain(Integer.MAX_VALUE, n -> 0 - n.intValue());
    }

    public static DiscreteDomain<Integer> evenInts() {
        return discreteDomain(Integer.MAX_VALUE / 2, n -> n.intValue() * 2);
    }

    public static DiscreteDomain<Integer> oddInts() {
        return discreteDomain(Integer.MAX_VALUE / 2, n -> 1 + n.intValue() * 2);
    }

    public static DiscreteDomain<Integer> intPowersOfTwo() {
        return discreteDomain(31, n -> 1 << n);
    }

    public static DiscreteDomain<Long> longPowersOfTwo() {
        return discreteDomain(63, n -> (long) 1 << n.intValue());
    }

}
