package software.kes.kraftwerk;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.collectionviews.NonEmptyVector;
import software.kes.collectionviews.Vector;
import software.kes.enhancediterables.FiniteIterable;
import software.kes.enhancediterables.NonEmptyFiniteIterable;
import software.kes.kraftwerk.core.BuildingBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;

final class Shuffle {
    private Shuffle() {
    }

    static Generator<Vector<Integer>> generateShuffled(int count) {
        return generateShuffled(count, id());
    }

    static <A> Generator<Vector<A>> generateShuffled(int count, Fn1<Integer, A> fn) {
        if (count <= 0) {
            return Generators.constant(Vector.empty());
        } else {
            return generateNonEmptyShuffled(count, fn)
                    .fmap(upcast());
        }
    }

    static <A> Generator<NonEmptyVector<A>> generateNonEmptyShuffled(int count, Fn1<Integer, A> fn) {
        if (count < 1) {
            throw new IllegalArgumentException("count must be >= 1");
        } else if (count == 1) {
            return Generators.constant(Vector.of(fn.apply(0)));
        } else {
            return generatorParameters -> input -> {
                ArrayList<A> target = newInputInstance(count, fn);
                Seed stateOut = shuffleInPlace(input, target);
                return Result.result(stateOut, NonEmptyVector.wrapOrThrow(target));
            };
        }
    }

    static <A> Generator<Vector<A>> generateShuffled(FiniteIterable<A> input) {
        ArrayList<A> inputList = new ArrayList<>();
        for (A a : input) {
            inputList.add(a);
        }
        return generateShuffled(inputList.size(), inputList::get);
    }

    static <A> Generator<NonEmptyVector<A>> generateShuffled(NonEmptyFiniteIterable<A> input) {
        ArrayList<A> inputList = new ArrayList<>();
        for (A a : input) {
            inputList.add(a);
        }
        return generateNonEmptyShuffled(inputList.size(), inputList::get);
    }

    static <A> Generator<Vector<A>> generateShuffled(Collection<A> input) {
        ArrayList<A> inputList = new ArrayList<>(input.size());
        inputList.addAll(input);
        return generateShuffled(inputList.size(), inputList::get);
    }

    static <A> Generator<Vector<A>> generateShuffled(A[] input) {
        int size = input.length;
        ArrayList<A> inputList = new ArrayList<>(size);
        inputList.addAll(Arrays.asList(input));
        return generateShuffled(inputList.size(), inputList::get);
    }

    static <A> Generator<Vector<A>> generateShuffled(Vector<A> input) {
        return generateShuffled(input.size(), input::unsafeGet);
    }

    static <A> Generator<NonEmptyVector<A>> generateShuffled(NonEmptyVector<A> input) {
        return generateNonEmptyShuffled(input.size(), input::unsafeGet);
    }

    private static <A> ArrayList<A> newInputInstance(int count, Fn1<Integer, A> fn) {
        ArrayList<A> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(fn.apply(i));
        }
        return result;
    }

    private static <A> Seed shuffleInPlace(Seed inputState, ArrayList<A> target) {
        int size = target.size();
        if (size < 2) {
            // No changes
            return inputState;
        } else {
            Seed state = inputState;
            for (int i = size - 1; i > 0; i--) {
                Result<? extends Seed, Integer> next = BuildingBlocks.nextIntBounded(i + 1, state);
                int j = next.getValue();
                if (i != j) {
                    A temp = target.get(i);
                    target.set(i, target.get(j));
                    target.set(j, temp);
                }
                state = next.getNextState();
            }
            return state;
        }
    }
}
