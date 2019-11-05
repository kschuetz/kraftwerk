package examples;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.domain.Characters;

import static dev.marksman.kraftwerk.GeneratedStream.streamFrom;
import static dev.marksman.kraftwerk.Generator.generateOrderedSequence;

public class OrderedSequenceExample {

    public static void main(String[] args) {
        ImmutableNonEmptyVector<String> source = Characters.alphaUpper().fmap(Object::toString);
        Generator<String> gen = generateOrderedSequence(0, 3, source)
                .fmap(ss -> String.join("", ss));

        streamFrom(gen).next(50).forEach(System.out::println);
    }

}
