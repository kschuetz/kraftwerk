package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompositeInterpreter implements Interpreter {
    private final Interpreter first;
    private final Interpreter second;

    @Override
    public <A> CompiledGenerator<A> handle(InterpreterContext context,
                                           Fn1<Generator<A>, CompiledGenerator<A>> recurse,
                                           Fn1<Generator<A>, CompiledGenerator<A>> callNextHandler,
                                           Generator<A> generator) {
        return null;
    }

    //    @Override
//    public <A> CompiledGenerator<A> handle(InterpreterContext context, Generator<A> generator) {
//        InterpreterContext ctx1 = new InterpreterContext() {
//            @Override
//            public SizeSelector getSizeSelector() {
//                return context.getSizeSelector();
//            }
//
//            @Override
//            public <B> CompiledGenerator<B> callNextHandler(Generator<B> generator) {
//                return second.handle(context, generator);
//            }
//
//            @Override
//            public <B> CompiledGenerator<B> recurse(Generator<B> generator) {
//                return context.recurse(generator);
//            }
//        };
//        return first.handle(ctx1, generator);
//    }

    public static CompositeInterpreter compositeInterpreter(Interpreter first, Interpreter second) {
        return new CompositeInterpreter(first, second);
    }
}
