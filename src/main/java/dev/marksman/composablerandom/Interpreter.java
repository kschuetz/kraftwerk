package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn2;

import static dev.marksman.composablerandom.CompositeInterpreter.compositeInterpreter;

public interface Interpreter {

    <A> GeneratorState<A> handle(InterpreterContext context, Generator<A> gen);

    default Interpreter andThen(Interpreter other) {
        return compositeInterpreter(this, other);
    }

    default Interpreter overrideWith(Interpreter other) {
        return other.andThen(this);
    }

    default <A> Interpreter andThen(Fn2<InterpreterContext, Generator<A>, GeneratorState<A>> other) {
        return andThen(interpreter(other));
    }

    default <A> Interpreter overrideWith(Fn2<InterpreterContext, Generator<A>, GeneratorState<A>> other) {
        return overrideWith(interpreter(other));
    }

    default <A> GeneratorState<A> compile(Parameters parameters, Generator<A> gen) {
        InterpreterContext context = new InterpreterContext() {
            @Override
            public Parameters getParameters() {
                return parameters;
            }

            @Override
            public <B> GeneratorState<B> recurse(Generator<B> gen) {
                return compile(parameters, gen);
            }

            @Override
            public <B> GeneratorState<B> callNextHandler(Generator<B> gen) {
                throw new IllegalStateException("No handler for generator: " + gen);
            }
        };
        return handle(context, gen);
    }

    static <A> Interpreter interpreter(Fn2<InterpreterContext, Generator<A>, GeneratorState<A>> handler) {
        return new Interpreter() {
            public <B> GeneratorState<B> handle(InterpreterContext context, Generator<B> gen) {
                //noinspection unchecked
                return (GeneratorState<B>) handler.apply(context, (Generator<A>) gen);
            }
        };
    }
}
