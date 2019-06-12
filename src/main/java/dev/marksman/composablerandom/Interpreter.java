package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn2;

import static dev.marksman.composablerandom.CompositeInterpreter.compositeInterpreter;

public interface Interpreter {

    <A> CompiledGenerator<A> handle(InterpreterContext context, Generator<A> generator);

    default Interpreter andThen(Interpreter other) {
        return compositeInterpreter(this, other);
    }

    default Interpreter overrideWith(Interpreter other) {
        return other.andThen(this);
    }

    default <A> Interpreter andThen(Fn2<InterpreterContext, Generator<A>, CompiledGenerator<A>> other) {
        return andThen(interpreter(other));
    }

    default <A> Interpreter overrideWith(Fn2<InterpreterContext, Generator<A>, CompiledGenerator<A>> other) {
        return overrideWith(interpreter(other));
    }

    default <A> CompiledGenerator<A> compile(Parameters parameters, Generator<A> generator) {
        InterpreterContext context = new InterpreterContext() {
            @Override
            public Parameters getParameters() {
                return parameters;
            }

            @Override
            public <B> CompiledGenerator<B> recurse(Generator<B> generator) {
                return compile(parameters, generator);
            }

            @Override
            public <B> CompiledGenerator<B> callNextHandler(Generator<B> generator) {
                throw new IllegalStateException("No handler for generator: " + generator);
            }
        };
        return handle(context, generator);
    }

    static <A> Interpreter interpreter(Fn2<InterpreterContext, Generator<A>, CompiledGenerator<A>> handler) {
        return new Interpreter() {
            public <B> CompiledGenerator<B> handle(InterpreterContext context, Generator<B> generator) {
                //noinspection unchecked
                return (CompiledGenerator<B>) handler.apply(context, (Generator<A>) generator);
            }
        };
    }
}
