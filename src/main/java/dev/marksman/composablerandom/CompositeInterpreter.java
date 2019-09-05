package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompositeInterpreter implements Interpreter {
    private final Interpreter first;
    private final Interpreter second;

    @Override
    public <A> GeneratorState<A> handle(InterpreterContext context, Generator<A> gen) {
        InterpreterContext ctx1 = new InterpreterContext() {
            @Override
            public Parameters getParameters() {
                return context.getParameters();
            }

            @Override
            public <B> GeneratorState<B> callNextHandler(Generator<B> gen) {
                return second.handle(context, gen);
            }

            @Override
            public <B> GeneratorState<B> recurse(Generator<B> gen) {
                return context.recurse(gen);
            }
        };
        return first.handle(ctx1, gen);
    }

    public static CompositeInterpreter compositeInterpreter(Interpreter first, Interpreter second) {
        return new CompositeInterpreter(first, second);
    }
}
