package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.Generator;

import static dev.marksman.composablerandom.spike.CompositeInterpreter.compositeInterpreter;

public interface Interpreter {

    <A> CompiledGenerator<A> handle(InterpreterContext context,
                                    Fn1<Generator<A>, CompiledGenerator<A>> recurse,
                                    Fn1<Generator<A>, CompiledGenerator<A>> callNextHandler,
                                    Generator<A> generator);

    default Interpreter andThen(Interpreter other) {
        return compositeInterpreter(this, other);
    }

    default Interpreter overrideWith(Interpreter other) {
        return other.andThen(this);
    }

//    static <A> Interpreter interpreter(Fn2<InterpreterContext, Generator<A>, CompiledGenerator<A>> handler) {
//        return new Interpreter() {
//            @Override
//            public <B> CompiledGenerator<B> handle(InterpreterContext context, Generator<B> generator) {
//                //noinspection unchecked
//                return (CompiledGenerator<B>) handler.apply(context, (Generator<A>) generator);
//            }
//        };
//    }
}
