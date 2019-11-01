package dev.marksman.composablerandom;

import static dev.marksman.composablerandom.Result.result;

public interface SizeSelector {
    Result<? extends LegacySeed, Integer> legacySelectSize(LegacySeed input);

    default Result<? extends Seed, Integer> selectSize(Seed input) {
        // TODO
        return result(input, 5);
    }
}
