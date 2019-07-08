package examples;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.domain.Characters;

import static dev.marksman.composablerandom.Generate.generateOrderedSequence;
import static dev.marksman.composablerandom.GeneratedStream.streamFrom;

public class OrderedSequenceExample {

    public static void main(String[] args) {
        ImmutableNonEmptyVector<String> source = Characters.alphaUpper().fmap(Object::toString);
        Generate<String> gen = generateOrderedSequence(0, 3, source)
                .fmap(ss -> String.join("", ss));

        streamFrom(gen).next(50).forEach(System.out::println);
    }

}
