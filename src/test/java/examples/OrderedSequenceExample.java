package examples;

import software.kes.collectionviews.ImmutableNonEmptyVector;
import software.kes.kraftwerk.constraints.IntRange;
import software.kes.kraftwerk.domain.Characters;

import static software.kes.kraftwerk.Generators.generateOrderedSequence;

public class OrderedSequenceExample {

    public static void main(String[] args) {
        ImmutableNonEmptyVector<String> source = Characters.alphaUpper().fmap(Object::toString);

        generateOrderedSequence(IntRange.inclusive(0, 3), source)
                .fmap(ss -> String.join("", ss))
                .run()
                .take(50)
                .forEach(System.out::println);
    }

}
