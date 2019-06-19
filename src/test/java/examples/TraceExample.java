package examples;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.TracePrinter;
import dev.marksman.composablerandom.random.StandardGen;
import examples.components.Person;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.TracePrinter.tracePrinter;
import static dev.marksman.composablerandom.TracingInterpreter.tracingInterpreter;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;
import static examples.components.Person.generatePerson;

public class TraceExample {

    public static void main(String[] args) {
        StandardGen standardGen = initStandardGen();

        TracePrinter tracePrinter = tracePrinter();
        Generate<Person> person = generatePerson().labeled("person");

        streamFrom(tracingInterpreter().compile(person), standardGen).next(1).forEach(value -> {
            tracePrinter.render(value).forEach(System.out::println);
        });

        streamFrom(person, standardGen).next(1).forEach(System.out::println);
    }
}
