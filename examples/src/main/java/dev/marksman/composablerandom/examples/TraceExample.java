package dev.marksman.composablerandom.examples;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.Trace;
import dev.marksman.composablerandom.TracePrinter;
import dev.marksman.composablerandom.random.StandardGen;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.TracePrinter.tracePrinter;
import static dev.marksman.composablerandom.TracingInterpreter.tracingInterpreter;
import static dev.marksman.composablerandom.examples.Street.generateStreet;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

public class TraceExample {

    public static void main(String[] args) {
        TracePrinter tracePrinter = tracePrinter();
        Generator<Street> street = generateStreet().labeled("street");
        CompiledGenerator<Trace<Street>> traced = tracingInterpreter()
                .compile(street);

        StandardGen standardGen = initStandardGen();

        Trace<Street> value = traced.run(standardGen).getValue();
        tracePrinter.render(value).forEach(System.out::println);

        streamFrom(street, standardGen).next(1).forEach(System.out::println);
    }
}
