package dev.marksman.composablerandom.examples;

import dev.marksman.composablerandom.TracePrinter;
import dev.marksman.composablerandom.tracing.Trace;

import static dev.marksman.composablerandom.TracePrinter.tracePrinter;
import static dev.marksman.composablerandom.examples.Street.generateStreet;
import static dev.marksman.composablerandom.legacy.OldGeneratedStream.streamFrom;
import static dev.marksman.composablerandom.legacy.builtin.OldGenerators.generateInt;
import static dev.marksman.composablerandom.legacy.builtin.OldGenerators.pair;

public class TraceExample {

    public static void main(String[] args) {
        TracePrinter tracePrinter = tracePrinter();
        Trace<Street> result = streamFrom(generateStreet().withTrace()).next();
        tracePrinter.render(result).forEach(System.out::println);

        Trace<?> next = streamFrom(pair(generateInt()).withTrace()).next();
        System.out.println("next = " + next);
        tracePrinter.render(next).forEach(System.out::println);
    }
}
