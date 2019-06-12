package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.NonEmptyVector;

import java.util.ArrayList;
import java.util.Collection;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

class Shuffle {

    public static Generator<ArrayList<Integer>> generateShuffled(int count) {
        return generateShuffled(count, id());
    }

    public static <A> Generator<ArrayList<A>> generateShuffled(int count, Fn1<Integer, A> fn) {
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

    public static <A> Generator<ArrayList<A>> generateShuffled(NonEmptyVector<A> domain) {
        int size = Math.max(domain.size(), 0);
        return generateShuffled((int) size, domain::unsafeGet);
    }

    private static <A> ArrayList<A> newInputInstance(int count, Fn1<Integer, A> fn) {
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
