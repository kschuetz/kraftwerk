package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn2;

import static dev.marksman.composablerandom.CompositeInterpreter.compositeInterpreter;

public interface Interpreter {

    <A> Generator<A> handle(InterpreterContext context, Generate<A> gen);

    default Interpreter andThen(Interpreter other) {
        return compositeInterpreter(this, other);
    }

    default Interpreter overrideWith(Interpreter other) {
        return other.andThen(this);
    }

    default <A> Interpreter andThen(Fn2<InterpreterContext, Generate<A>, Generator<A>> other) {
        return andThen(interpreter(other));
    }

    default <A> Interpreter overrideWith(Fn2<InterpreterContext, Generate<A>, Generator<A>> other) {
        return overrideWith(interpreter(other));
    }

    default <A> Generator<A> compile(Parameters parameters, Generate<A> gen) {
        InterpreterContext context = new InterpreterContext() {
            @Override
            public Parameters getParameters() {
                return parameters;
            }

            @Override
            public <B> Generator<B> recurse(Generate<B> gen) {
                return compile(parameters, gen);
            }

            @Override
            public <B> Generator<B> callNextHandler(Generate<B> gen) {
                throw new IllegalStateException("No handler for generator: " + gen);
            }
        };
        return handle(context, gen);
    }

    static <A> Interpreter interpreter(Fn2<InterpreterContext, Generate<A>, Generator<A>> handler) {
        return new Interpreter() {
            public <B> Generator<B> handle(InterpreterContext context, Generate<B> gen) {
                //noinspection unchecked
                return (Generator<B>) handler.apply(context, (Generate<A>) gen);
            }
        };
    }
}
