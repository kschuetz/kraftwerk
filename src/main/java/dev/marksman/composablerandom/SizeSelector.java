package dev.marksman.composablerandom;

public interface SizeSelector {
    Result<? extends RandomState, Integer> selectSize(RandomState input);
}
