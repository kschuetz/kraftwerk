package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.monad.Monad;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;

import static dev.marksman.composablerandom.Instruction.flatMapped;
import static dev.marksman.composablerandom.Instruction.mapped;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Generator<A> implements Monad<A, Generator> {

    private final Instruction<A> instruction;

    @Override
    public final <B> Generator<B> fmap(Function<? super A, ? extends B> fn) {
        return generator(mapped(fn, instruction));
    }

    @Override
    public final <B> Generator<B> flatMap(Function<? super A, ? extends Monad<B, Generator>> fn) {
        return generator(flatMapped(a -> ((Generator<B>) fn.apply(a))
                        .getInstruction(),
                instruction));
    }

    @Override
    public final <B> Generator<B> pure(B b) {
        return generator(Instruction.pure(b));
    }

    public static <A> Generator<A> generator(Instruction<A> instruction) {
        return new Generator<>(instruction);
    }

    public static <A> Generator<A> constant(A a) {
        return generator(Instruction.pure(a));
    }

}
