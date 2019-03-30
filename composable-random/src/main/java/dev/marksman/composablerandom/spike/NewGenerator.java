package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.monad.Monad;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;

import static dev.marksman.composablerandom.spike.Instruction.flatMapped;
import static dev.marksman.composablerandom.spike.Instruction.mapped;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NewGenerator<A> implements Monad<A, NewGenerator> {

    private final Instruction<A> instruction;

    @Override
    public final <B> NewGenerator<B> fmap(Function<? super A, ? extends B> fn) {
        return generator(mapped(fn, instruction));
    }

    @Override
    public final <B> NewGenerator<B> flatMap(Function<? super A, ? extends Monad<B, NewGenerator>> fn) {
        return generator(flatMapped(a -> ((NewGenerator<B>) fn.apply(a))
                        .getInstruction(),
                instruction));
    }

    @Override
    public final <B> NewGenerator<B> pure(B b) {
        return generator(Instruction.pure(b));
    }

    public static <A> NewGenerator<A> generator(Instruction<A> instruction) {
        return new NewGenerator<>(instruction);
    }

    public static <A> NewGenerator<A> constant(A a) {
        return generator(Instruction.pure(a));
    }

}
