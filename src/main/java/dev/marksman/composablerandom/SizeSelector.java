package dev.marksman.composablerandom;

public interface SizeSelector {
    Result<? extends LegacySeed, Integer> selectSize(LegacySeed input);
}
