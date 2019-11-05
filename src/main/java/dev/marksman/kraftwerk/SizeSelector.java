package dev.marksman.kraftwerk;

public interface SizeSelector {
    Result<? extends Seed, Integer> selectSize(Seed input);
}
