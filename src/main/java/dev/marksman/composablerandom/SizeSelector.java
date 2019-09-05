package dev.marksman.composablerandom;

public interface SizeSelector {
    Result<? extends Seed, Integer> selectSize(Seed input);
}
