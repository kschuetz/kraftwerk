package dev.marksman.nonempty;

import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import com.jnape.palatable.lambda.iteration.ImmutableIterator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Drop.drop;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ConcreteSets {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Set1<A> implements NonEmptySet<A> {
        private final A value1;

        @Override
        public A getHead() {
            return value1;
        }

        @Override
        public Iterable<A> getTail() {
            return Collections.emptyList();
        }

        @Override
        public Iterator<A> iterator() {
            return singletonList(value1).iterator();
        }

        @Override
        public boolean contains(A item) {
            return false;
        }

        @Override
        public NonEmptySet<A> add(A item) {
            if (contains(item)) return this;
            else return new Set2<>(value1, item);
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Set2<A> implements NonEmptySet<A> {
        private final A value1;
        private final A value2;

        @Override
        public A getHead() {
            return value1;
        }

        @Override
        public Iterable<A> getTail() {
            return singletonList(value2);
        }

        @Override
        public Iterator<A> iterator() {
            return asList(value1, value2).iterator();
        }

        @Override
        public boolean contains(A item) {
            return value1.equals(item) || value2.equals(item);
        }

        @Override
        public NonEmptySet<A> add(A item) {
            if (contains(item)) return this;
            else return new Set3<>(value1, value2, item);
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Set3<A> implements NonEmptySet<A> {
        private final A value1;
        private final A value2;
        private final A value3;

        @Override
        public A getHead() {
            return value1;
        }

        @Override
        public Iterable<A> getTail() {
            return asList(value2, value3);
        }

        @Override
        public Iterator<A> iterator() {
            return asList(value1, value2, value3).iterator();
        }

        @Override
        public boolean contains(A item) {
            return value1.equals(item) || value2.equals(item) || value3.equals(item);
        }

        @Override
        public NonEmptySet<A> add(A item) {
            if (contains(item)) return this;
            else return new Set4<>(value1, value2, value3, item);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Set4<A> implements NonEmptySet<A> {
        private final A value1;
        private final A value2;
        private final A value3;
        private final A value4;

        @Override
        public A getHead() {
            return value1;
        }

        @Override
        public Iterable<A> getTail() {
            return Collections.emptyList();
        }

        @Override
        public Iterator<A> iterator() {
            return asList(value1, value2, value3, value4).iterator();
        }

        @Override
        public boolean contains(A item) {
            return value1.equals(item) || value2.equals(item) || value3.equals(item) || value4.equals(item);
        }

        @Override
        public NonEmptySet<A> add(A item) {
            if (contains(item)) return this;
            else return new Set5<>(value1, value2, value3, value4, item);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Set5<A> implements NonEmptySet<A> {
        private final A value1;
        private final A value2;
        private final A value3;
        private final A value4;
        private final A value5;

        @Override
        public A getHead() {
            return value1;
        }

        @Override
        public Iterable<A> getTail() {
            return asList(value2, value3, value4, value5);
        }

        @Override
        public Iterator<A> iterator() {
            return asList(value1, value2, value3, value4, value5).iterator();
        }

        @Override
        public boolean contains(A item) {
            return value1.equals(item) || value2.equals(item) || value3.equals(item) || value4.equals(item) ||
                    value5.equals(item);
        }

        @Override
        public NonEmptySet<A> add(A item) {
            if (contains(item)) return this;
            else return new Set6<>(value1, value2, value3, value4, value5, item);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Set6<A> implements NonEmptySet<A> {
        private final A value1;
        private final A value2;
        private final A value3;
        private final A value4;
        private final A value5;
        private final A value6;

        @Override
        public A getHead() {
            return value1;
        }

        @Override
        public Iterable<A> getTail() {
            return asList(value2, value3, value4, value5, value6);
        }

        @Override
        public Iterator<A> iterator() {
            return asList(value1, value2, value3, value4, value5, value6).iterator();
        }

        @Override
        public boolean contains(A item) {
            return value1.equals(item) || value2.equals(item) || value3.equals(item) || value4.equals(item) ||
                    value5.equals(item) || value6.equals(item);
        }

        @Override
        public NonEmptySet<A> add(A item) {
            if (contains(item)) return this;
            else return new Set7<>(value1, value2, value3, value4, value5, value6, item);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Set7<A> implements NonEmptySet<A> {
        private final A value1;
        private final A value2;
        private final A value3;
        private final A value4;
        private final A value5;
        private final A value6;
        private final A value7;

        @Override
        public A getHead() {
            return value1;
        }

        @Override
        public Iterable<A> getTail() {
            return asList(value2, value3, value4, value5, value6, value7);
        }

        @Override
        public Iterator<A> iterator() {
            return asList(value1, value2, value3, value4, value5, value6, value7).iterator();
        }

        @Override
        public boolean contains(A item) {
            return value1.equals(item) || value2.equals(item) || value3.equals(item) || value4.equals(item) ||
                    value5.equals(item) || value6.equals(item) || value7.equals(item);
        }

        @Override
        public NonEmptySet<A> add(A item) {
            if (contains(item)) return this;
            else return new Set8<>(value1, value2, value3, value4, value5, value6, value7, item);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Set8<A> implements NonEmptySet<A> {
        private final A value1;
        private final A value2;
        private final A value3;
        private final A value4;
        private final A value5;
        private final A value6;
        private final A value7;
        private final A value8;

        @Override
        public A getHead() {
            return value1;
        }

        @Override
        public Iterable<A> getTail() {
            return asList(value2, value3, value4, value5, value6, value7, value8);
        }

        @Override
        public Iterator<A> iterator() {
            return asList(value1, value2, value3, value4, value5, value6, value8).iterator();
        }

        @Override
        public boolean contains(A item) {
            return value1.equals(item) || value2.equals(item) || value3.equals(item) || value4.equals(item) ||
                    value5.equals(item) || value6.equals(item) || value7.equals(item) || value8.equals(item);
        }

        @Override
        public NonEmptySet<A> add(A item) {
            if (contains(item)) return this;
            else {
                return SetN.setN(item, this);
            }
        }
    }

    @AllArgsConstructor
    static class SetN<A> implements NonEmptySet<A> {
        private final HashSet<A> internalSet;

        @Override
        public boolean contains(A item) {
            return internalSet.contains(item);
        }

        @Override
        public NonEmptySet<A> add(A item) {
            if (contains(item)) return this;
            else return setN(item, this);
        }

        @Override
        public A getHead() {
            return internalSet.iterator().next();
        }

        @Override
        public Iterable<A> getTail() {
            return drop(1, this);
        }

        @Override
        public Iterator<A> iterator() {
            Iterator<A> internalIterator = internalSet.iterator();
            return new ImmutableIterator<A>() {
                @Override
                public boolean hasNext() {
                    return internalIterator.hasNext();
                }

                @Override
                public A next() {
                    return internalIterator.next();
                }
            };
        }

        static <A> NonEmptySet<A> setN(A first, Iterable<A> rest) {
            HashSet<A> internal = new HashSet<>();
            internal.add(first);
            rest.forEach(internal::add);
            return new SetN<>(internal);
        }

    }

    static <A> NonEmptySet<A> nonEmptySet(A first, Iterable<A> rest) {
        HashSet<A> internal = new HashSet<>();
        rest.forEach(internal::add);
        if (internal.size() < 8) {
            NonEmptySet<A> initial = new Set1<>(first);
            return FoldLeft.foldLeft(NonEmptySet::add, initial, rest);
        } else {
            internal.add(first);
            return new SetN<>(internal);
        }
    }

    static <A> NonEmptySet<A> nonEmptySet(NonEmptyIterable<A> source) {
        return nonEmptySet(source.getHead(), source.getTail());
    }
}
