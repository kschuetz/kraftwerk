package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompositeInterpreter implements Interpreter {
    private final Interpreter first;
    private final Interpreter second;

    @Override
    public <A> CompiledGenerator<A> handle(InterpreterContext context, Generator<A> generator) {
        InterpreterContext ctx1 = new InterpreterContext() {
            @Override
            public Parameters getParameters() {
                return context.getParameters();
            }

            @Override
            public <B> CompiledGenerator<B> callNextHandler(Generator<B> generator) {
                return second.handle(context, generator);
            }

            @Override
            public <B> CompiledGenerator<B> recurse(Generator<B> generator) {
                return context.recurse(generator);
            }
        };
        return first.handle(ctx1, generator);
    }

    public static CompositeInterpreter compositeInterpreter(Interpreter first, Interpreter second) {
        return new CompositeInterpreter(first, second);
    }
}
