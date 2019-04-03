package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

class Shuffle {

    public static Generator<ArrayList<Integer>> generateShuffled(int count) {
        return generateShuffled(count, id());
    }

    public static <A> Generator<ArrayList<A>> generateShuffled(int count, Function<Integer, A> fn) {
//        return Generator.toGenerator(stateIn -> {
//            ArrayList<A> target = newInputInstance(count, fn);
//            RandomState stateOut = shuffleInPlace(stateIn, target);
//            return result(stateOut, target);
//        });
        // TODO: custom primitive
        return null;
    }

    public static <A> Generator<ArrayList<A>> generateShuffled(Collection<A> input) {
        ArrayList<A> inputList = new ArrayList<A>(input.size());
        inputList.addAll(input);
        return generateShuffled(inputList.size(), inputList::get);
    }

    public static <A> Generator<ArrayList<A>> generateShuffled(A[] input) {
        int size = input.length;
        ArrayList<A> inputList = new ArrayList<>(size);
        for (A a : input) {
            inputList.add(a);
        }
        return generateShuffled(inputList.size(), inputList::get);
    }

    public static <A> Generator<ArrayList<A>> generateShuffled(DiscreteDomain<A> domain) {
        long size = Math.max(domain.getSize(), 0);
        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("DiscreteDomain too large; size should not exceed Integer.MAX_VALUE");
        }
        return generateShuffled((int) size, domain::getValue);
    }

    private static <A> ArrayList<A> newInputInstance(int count, Function<Integer, A> fn) {
        ArrayList<A> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(fn.apply(i));
        }
        return result;
    }

    private static <A> RandomState shuffleInPlace(RandomState inputState, ArrayList<A> target) {
        int size = target.size();
        if (size < 2) {
            // No changes
            return inputState;
        } else {
            // TODO:  implement shuffleInPlace
            return inputState;
        }
    }

}
