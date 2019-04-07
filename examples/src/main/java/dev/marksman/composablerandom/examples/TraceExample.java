package dev.marksman.composablerandom.examples;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.TracePrinter;
import dev.marksman.composablerandom.examples.generators.Street;
import dev.marksman.composablerandom.random.StandardGen;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.TracePrinter.tracePrinter;
import static dev.marksman.composablerandom.TracingInterpreter.tracingInterpreter;
import static dev.marksman.composablerandom.examples.generators.Street.generateStreet;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

public class TraceExample {

    public static void main(String[] args) {
        StandardGen standardGen = initStandardGen();

        TracePrinter tracePrinter = tracePrinter();
        Generator<Street> street = generateStreet().labeled("street");

        streamFrom(tracingInterpreter().compile(street), standardGen).next(1).forEach(value -> {
            tracePrinter.render(value).forEach(System.out::println);
        });

        streamFrom(street, standardGen).next(1).forEach(System.out::println);
    }
}
