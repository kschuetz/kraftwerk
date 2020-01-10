package examples;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.kraftwerk.domain.Characters;

import static dev.marksman.kraftwerk.Generators.generateOrderedSequence;

public class OrderedSequenceExample {

    public static void main(String[] args) {
        ImmutableNonEmptyVector<String> source = Characters.alphaUpper().fmap(Object::toString);

        generateOrderedSequence(0, 3, source)
                .fmap(ss -> String.join("", ss))
                .run()
                .take(50)
                .forEach(System.out::println);
    }

}
